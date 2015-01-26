//
// CODE TAKEN FROM http://svn.berlios.de/svnroot/repos/far-unlimited/trunk/FARPlus/
//
// Author: Sergey Shishmintzev, Kiev 2005-2006
// based on MSDN article http://msdn.microsoft.com/msdnmag/issues/02/03/hood/
// Licence: LGPL'ed for FARPlus,
//
// Contributor(s):
//   Sergey Shishmintzev <sergey.shishmintzev@gmail.com>
//   Dennis Trachuk <dennis.trachuk@nm.ru>||<dennis.trachuk@bk.ru>
//
#pragma pack(8)
#include "stdafx.h"
#include <dbghelp.h>
#pragma pack()
#pragma comment( lib, "DbgHelp.Lib" )
#include <tchar.h>
#include <stdio.h>
#include <malloc.h>
#include <vector>
using namespace std;
#include "DumpStack.h"

static vector<string> strOutput;

#define MODULE_NAME_LEN 64
#define SYMBOL_NAME_LEN 128


void OnOutput(const char* text, ...)
{
	va_list vl;
	char szOut[1024];
	va_start( vl, text);
	_vsnprintf(szOut, sizeof(szOut) - 1, (LPCSTR)text, vl);
	va_end( vl );
	OutputDebugString(szOut);
	strOutput.push_back(szOut);
}

void DumpOutput(LPCSTR lpszModule, LPCSTR lpszDumpPath, _PONSTACKREPORTING pOnStackReportingFn)
{
	OutputDebugString("\nDUMP OUTPUT:\n");

	HANDLE hFile; 
	char szModule[MODULE_NAME_LEN + 100];
	char szFullFileName[MODULE_NAME_LEN + 256];
	char szTime[MODULE_NAME_LEN + 100];
	memset(szModule, 0, sizeof(szModule));
	memset(szFullFileName, 0, sizeof(szFullFileName));

	
	if(lstrlen(lpszModule) != 0) {
		_snprintf(szModule, MODULE_NAME_LEN, "%s", lpszModule);
	}
	if(lstrlen(szModule) == 0) {
		lstrcpy(szModule, "UNKNOWN");
	}
	lstrcat(szModule, "_DumpStack_");
	SYSTEMTIME strST;
	memset(&strST, 0, sizeof(SYSTEMTIME));
	GetLocalTime(&strST);
	wsprintf(szTime, "%02d_%02d_%04d_%02d_%02d_%02d", strST.wMonth, strST.wDay, strST.wYear, strST.wHour, strST.wMinute, strST.wSecond);
	lstrcat(szModule, szTime);
	lstrcat(szModule, ".log");


	if(lstrlen(lpszDumpPath) != 0) {
		lstrcpy(szFullFileName, lpszDumpPath);
		if(szFullFileName[lstrlen(szFullFileName) - 1] != '\\')
			szFullFileName[lstrlen(szFullFileName) - 1] = '\\';
	}
	else {
		char szFileName[1024];
		char drive[_MAX_DRIVE];
		char dir[_MAX_DIR];
		char fname[_MAX_FNAME];
		char ext[_MAX_EXT];
		GetModuleFileName(NULL, szFileName, sizeof(szFileName) - 1);
		_splitpath(szFileName, drive, dir, fname, ext);
		wsprintf(szFullFileName, "%s%s%", drive, dir);
		if(szFullFileName[lstrlen(szFullFileName) - 1] != '\\')
			szFullFileName[lstrlen(szFullFileName) - 1] = '\\';
	}
	lstrcat(szFullFileName, szModule);

	hFile = CreateFile(szFullFileName, GENERIC_WRITE, FILE_SHARE_READ, NULL, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, NULL);
	if (hFile == INVALID_HANDLE_VALUE) { 
		char szOut[512];
		wsprintf(szOut, "Could not open dump file '%s'", szFullFileName);
		OutputDebugString(szOut);
		pOnStackReportingFn(EVENTLOG_ERROR_TYPE, szOut);
	} 
	else {
		if(!strOutput.empty()) {
			vector<string>::iterator itOut;
			CHAR buffer[1024];
			DWORD dwBytesWritten = 0;
			for(itOut = strOutput.begin(); itOut != strOutput.end(); itOut++) {
				_snprintf(buffer, 1024, "%s", (*itOut).c_str());
				OutputDebugStringA(buffer);
				if(!WriteFile(hFile, (LPCVOID)buffer, lstrlen(buffer), &dwBytesWritten, NULL)) {
					char szOut[512];
					wsprintf(szOut, "Could not write to dump file '%s'", szFullFileName);
					OutputDebugString(szOut);
					pOnStackReportingFn(EVENTLOG_ERROR_TYPE, szOut);
					break;
				}
			}
		}
		FlushFileBuffers(hFile);
		CloseHandle(hFile);
	}
	char szOut[512];
	wsprintf(szOut, "Stack Dump completed. File '%s'", szFullFileName);
	OutputDebugString(szOut);
	pOnStackReportingFn(EVENTLOG_WARNING_TYPE, szOut);
	OutputDebugString("\nEND OF DUMP\n");
}



char* PointToName(const char *Path)
{
	const char *NamePtr=Path;
	while (*Path) {
		if (*Path=='\\' || *Path=='/' || *Path==':')
			NamePtr=Path+1;
		Path++;
	}
	return (char *) NamePtr;
}

/////////////////////////////////////////////////////////////////////////////
// Routine to produce stack dump

struct FARPLUS_SYMBOL_INFO
{
	DWORD dwAddress;
	DWORD dwOffset;
	BOOL  bModule;
	CHAR  szModule[ MODULE_NAME_LEN ];
	BOOL  bSymbol;
	CHAR  szSymbol[ SYMBOL_NAME_LEN ];
	BOOL  bLineInfo;
	DWORD dwLineDisplacement;
	IMAGEHLP_LINE lineInfo;
};


static DWORD __stdcall GetModuleBase( HANDLE hProcess, DWORD dwReturnAddress )
{
	IMAGEHLP_MODULE moduleInfo;

	if ( SymGetModuleInfo( hProcess, dwReturnAddress, &moduleInfo ) )
		return moduleInfo.BaseOfImage;

	MEMORY_BASIC_INFORMATION mbInfo;
	if ( VirtualQueryEx( hProcess, (LPCVOID)dwReturnAddress, &mbInfo, sizeof( mbInfo ) ) ) {
		char szFile[ MAX_PATH ] = { 0 };
		DWORD cch = GetModuleFileName( (HINSTANCE)mbInfo.AllocationBase, szFile, MAX_PATH );
		// Ignore the return code since we can't do anything with it.
		if ( SymLoadModule( hProcess, NULL, cch ? szFile : NULL, NULL, (DWORD)mbInfo.AllocationBase, 0 ) )
			return (DWORD) mbInfo.AllocationBase;
	}

	return SymGetModuleBase( hProcess, dwReturnAddress );
}

// determine number of elements in an array (not bytes)
#define _countof( array ) ( sizeof( array ) / sizeof( array[ 0 ] ) )

static void ResolveSymbol( HANDLE hProcess, DWORD dwAddress, FARPLUS_SYMBOL_INFO &siSymbol )
{
	union
	{
		char rgchSymbol[ sizeof( IMAGEHLP_SYMBOL ) + 255 ];
		IMAGEHLP_SYMBOL sym;
	};

	char  szUndec     [ 256 ];
	char  szWithOffset[ 1024 ]; // see wsprintf help

	LPSTR pszSymbol = NULL;

	IMAGEHLP_MODULE mi;
	mi.SizeOfStruct = sizeof( IMAGEHLP_MODULE );

	ZeroMemory( &siSymbol, sizeof( FARPLUS_SYMBOL_INFO ) );

	siSymbol.dwAddress = dwAddress;

	if ( siSymbol.bModule = SymGetModuleInfo( hProcess, dwAddress, &mi ) )
		strncpy( siSymbol.szModule, PointToName( mi.ImageName ), _countof( siSymbol.szModule ) );
	else
		wsprintfA( siSymbol.szModule, "<no module:code=%8.8X>",  GetLastError());

	__try {
		sym.SizeOfStruct  = sizeof( IMAGEHLP_SYMBOL );
		sym.Address       = dwAddress;
		sym.MaxNameLength = 255;

		if ( siSymbol.bSymbol = SymGetSymFromAddr( hProcess, dwAddress, &(siSymbol.dwOffset), &sym ) ) {
			pszSymbol = sym.Name;

			if ( UnDecorateSymbolName( sym.Name, szUndec, _countof( szUndec ), UNDNAME_NO_MS_KEYWORDS|UNDNAME_NO_ACCESS_SPECIFIERS ) )
				pszSymbol = szUndec;
			else if ( SymUnDName( &sym, szUndec, _countof( szUndec ) ) )
				pszSymbol = szUndec;

			if ( siSymbol.dwOffset != 0 ) {
				wsprintfA( szWithOffset, "%s() + %d bytes", pszSymbol, siSymbol.dwOffset );
				pszSymbol = szWithOffset;
			}
		}
		else {
			pszSymbol = szUndec;
			wsprintfA(pszSymbol, "<no symbol:code=%8.8X>", GetLastError());
		}
		siSymbol.lineInfo.SizeOfStruct = sizeof(IMAGEHLP_LINE);
		siSymbol.bLineInfo = SymGetLineFromAddr( hProcess, dwAddress, &siSymbol.dwLineDisplacement, &siSymbol.lineInfo );
	}
	__except( EXCEPTION_EXECUTE_HANDLER )
	{
		pszSymbol = "<EX: no symbol>";
		siSymbol.dwOffset = dwAddress - mi.BaseOfImage;
	}

	strncpy( siSymbol.szSymbol, pszSymbol, _countof( siSymbol.szSymbol ) );
}

enum BasicType
{
	btNoType = 0,
	btVoid = 1,
	btChar = 2,
	btWChar = 3,
	btInt = 6,
	btUInt = 7,
	btFloat = 8,
	btBCD = 9,
	btBool = 10,
	btLong = 13,
	btULong = 14,
	btCurrency = 25,
	btDate = 26,
	btVariant = 27,
	btComplex = 28,
	btBit = 29,
	btBSTR = 30,
	btHresult = 31
};


class DebugEnumSymbolDump
{ // This class can be used in terms of MPL1.1 (http://www.mozilla.org/MPL/MPL-1.1.html) too.
  // based on http://msdn.microsoft.com/msdnmag/issues/02/03/hood/
  // Noto: GetModuleBase and ResolveSymbol under LGPL.
  // from MSDN:

	typedef struct AdressItem
	{
		AdressItem * prev;
		DWORD_PTR value;
		wchar_t* name, *full_name, *last_name;
		unsigned name_len;

		AdressItem(AdressItem *_prev, DWORD_PTR _value)
		{
			prev = _prev;
			value = _value;
			name = NULL;
			full_name = NULL;
			name_len = 0;
		}

		~AdressItem()
		{
			if (name) { 
				delete [] name; 
				name = NULL; 
			}
			if (full_name) { 
				delete [] full_name; 
				full_name = NULL; 
			}
		}

		void SetName(wchar_t *_name)
		{
			name_len = (UINT)wcslen(_name);
			name = new wchar_t[name_len+1];
			MoveMemory(name, _name, name_len * sizeof(wchar_t));
			name[name_len] = 0;
		}

		void SetName(char *_name)
		{
			name_len = (UINT)strlen(_name);
			name = new wchar_t[name_len+1];
			for (UINT i=0; i< name_len; i++) 
				name[i]=_name[i];
			name[name_len] = 0;
		}

		wchar_t *GetFullname()
		{
			if (full_name) 
				return full_name;
			unsigned l = 0;
			wchar_t *wp;
			AdressItem *pal = this;
			while (pal) {
				if (pal->name) 
					l += pal->name_len + 2;
				pal = pal->prev;
			}
			full_name = wp = new wchar_t[l+1];
			pal = this;
			while (pal) {
				if (pal->name)
				{
					MoveMemory(wp, pal->name, pal->name_len * sizeof(wchar_t));
					wp += pal->name_len;
					if (pal->prev) 
						{ *wp++ = '<'; *wp++ = '-'; }
				}
				pal = pal->prev;
			}
			*wp=0;
			return full_name;
		}

		bool IsIncluded(DWORD_PTR offset)
		{
			AdressItem *pal = this;
			while (pal) {
				if (offset == pal->value) {
					last_name = pal->name;
					return true;
				}
				pal = pal->prev;
			}
			return false;
		}

		AdressItem *pop()
		{
			AdressItem *pal = this, *tmp;
			if (pal) {
				tmp = pal;
				pal = pal->prev;
				delete tmp;
			}
			return pal;
		}

		DWORD_PTR get_value()
		{
			return this ? value : -1;
		}

		void free()
		{
			AdressItem *pal = this, *tmp;
			while (pal) {
				tmp = pal;
				pal = pal->prev;
				delete tmp;
			}
		}
	} *AdressList;


	HANDLE hProcess;
	const STACKFRAME* pStackFrame;
	AdressItem *dumped;
	int max_array_items;
	char itoa_buf[65];


public:
	void DontDump(DWORD_PTR address, wchar_t *full_name )
	{
		if (dumped->get_value() != address) {
			dumped = new AdressItem(dumped, address);
			if (full_name) 
				dumped->SetName(full_name);
		}
	}

	void Newline()
	{
		OnOutput("\r\n" );
	}

	void Print(const char* text)
	{
		OnOutput("%s", text );
	}

	void Println(const char* text)
	{
		OnOutput("%s\r\n", text );
	}

	char *itoI64u(unsigned __int64 value)
	{
		return _ui64toa(value, itoa_buf, 10);
	}

	char *itoI64x(unsigned __int64 value)
	{
		return _ui64toa(value, itoa_buf, 16);
	}

	bool FormatSymbolByAddr(const DWORD addr)
	{
		FARPLUS_SYMBOL_INFO info;
		(void)GetModuleBase( hProcess, addr );
		ResolveSymbol( hProcess, addr, info );
		if ( info.bModule || info.bSymbol || info.bLineInfo ) {
//			OnOutput("%8.8X: (%s) : %s()", addr, info.szModule, info.szSymbol);
			OnOutput("0x%8.8X: (%s)", addr, info.szModule);
			if (info.bLineInfo) {
				OnOutput(" : %s (%u,%u)", info.lineInfo.FileName, info.lineInfo.LineNumber, info.dwLineDisplacement);
			}
			OnOutput(" : %s", info.szSymbol);
			return true;
		}
		else
			return false;
	}

	void FormatStringBuff (const char *text, unsigned len, char quote = '"')
	{
		char qs[2] = {quote, 0};
		unsigned count = 0;
		OnOutput(qs);
		if ( len > 1024 ) 
			len = 1024;
		for (unsigned i=0; count<len && *text; i++, text++) {
			if (*(unsigned char*)text<' ' || *text == '"') {
				OnOutput("\\x%2.2x", (*(unsigned char*)text) & 0xFF);
				count += 4; //\xXX
			}
			else {
				OnOutput("%c", *text);
				count++;
			}
		}
		OnOutput(qs);
	}

	void FormatStringBuffW (const wchar_t *text, unsigned len, char quote = '"')
	{
		char qs[2] = {quote, 0};
		unsigned count = 0;

		OnOutput(qs);
		if ( len > 1024 ) 
			len = 1024;
		for (unsigned i=0; count<len && *text; i++, text++) {
			if (*(unsigned short*)text<' ' || *text == '"') {
				OnOutput("\\x%4.4x", (*(unsigned short*)text) & 0xFFFF);
				count += 6; //\xXXXX
			}
			else {
				OnOutput("%lc", *text);
				count++;
			}
		}
		OnOutput(qs);
	}

	void FormatOutputValue(  const BasicType basicType, const DWORD64 length, PVOID const pAddress )
	{
		if (IsBadReadPtr(pAddress, length)) 
			return;
		// Format appropriately (assuming it's a 1, 2, or 4 bytes (!!!)
		//OnOutput(" {length=%I64d/%X/basicType=%u}", length, 0x12345678, basicType);
		switch (basicType) {
		case btVoid:
			if (FormatSymbolByAddr( (DWORD)pAddress))  
				return;
			break;
		}
		switch (length)
		{
		case 1:
			if ( basicType == btChar ) {
			//OnOutput("= \"%.31s\"", (PSTR)pAddress );
				FormatStringBuff((PSTR)pAddress, 128);
			}
			else
				OnOutput(" = 0x%2.2X", *(PBYTE)pAddress );
			break;
		case 2:
			OnOutput(" = 0x%4.4X", *(PWORD)pAddress );
			break;
		case 4:
			if ( basicType == btFloat ) {
				OnOutput(" = %f", *(PFLOAT)pAddress);
			}
			else if ( basicType == btChar ) {
				if ( !IsBadStringPtr( *(PSTR*)pAddress, 32) ) {
					OnOutput("= \"%.31s\"", *(PDWORD)pAddress );
				}
				else
					OnOutput(" = 0x%8.8X", *(PDWORD)pAddress );
			}
			else if(basicType == btInt) {
				OnOutput(" = %d", *(PDWORD)pAddress );
			}
			else if(basicType == btUInt) {
				OnOutput(" = %u", *(PDWORD)pAddress );
			}
			else
				OnOutput(" = 0x%8.8X", *(PDWORD)pAddress);
			break;
		case 8:
			if ( basicType == btFloat )
				OnOutput(" = %lf", *(double *)pAddress );
			else
				OnOutput(" = %s", itoI64x(*(DWORD64*)pAddress) );
			break;
		default: ;
		}
	}

	void MakeIndent(const unsigned nestingLevel) const
	{
		for ( unsigned j = 0; j <= nestingLevel+1; j++ )
			OnOutput("\t" );
	}

	bool FormatEnumNameValue(const unsigned nestingLevel, const WCHAR * const name, const bool is_signed, const LONG value,	const LONG variable)
	{
		if ( variable == value ) {
			MakeIndent( nestingLevel);
			OnOutput(is_signed ? "== %ls (==%d)\r\n" : "== %ls (==%u)\r\n", name, value);
			return true;
		}
		/*??? else if (value && (variable & value) == value) {
			MakeIndent( nestingLevel );
			OnOutput(param, is_signed ? "| %ls (==%d)\r\n" : "| %ls (==%u)\r\n", name, value);
		}*/
		return false;
	}

	bool FormatVTableShape(const unsigned nestingLevel, const DWORD count, const DWORD_PTR offset)
	{
		if (count) {
			OnOutput("[%d] = {\r\n", count );
			for (UINT i=0; (i < (UINT)max_array_items) && (i < (UINT)count); i++) {
				MakeIndent( nestingLevel + 1 );
				FormatSymbolByAddr( *(DWORD_PTR*)(offset+sizeof(DWORD_PTR)*i) );
				Newline();
			}
			MakeIndent( nestingLevel );
			Println("... }");
		}
		else {
			Println("[0] = { }");
		}
		return true;
	}

	class CSymTypeInfo
	{
		public:
		DebugEnumSymbolDump &desd;
		DWORD dwTypeIndex;
		DWORD64 modBase;
		WCHAR * pwszTypeName;
		BOOL succeeded;
		CSymTypeInfo *type;
		DWORD dwVirtualBaseIndex;

		CSymTypeInfo(DebugEnumSymbolDump *_desd, DWORD64 _modBase, DWORD _dwTypeIndex) : desd(*_desd)
		{
			modBase = _modBase;
			dwTypeIndex = _dwTypeIndex;
			succeeded = _dwTypeIndex != 0;
			pwszTypeName = NULL;
			type = NULL;
			dwVirtualBaseIndex = 0;
		}

		~CSymTypeInfo()
		{
			Clean();
			dwTypeIndex = 0;
			modBase = 0;
		}

		BOOL SymGetTypeInfo(IN  IMAGEHLP_SYMBOL_TYPE_INFO GetType, OUT PVOID pInfo)
		{
			return succeeded = ::SymGetTypeInfo(desd.hProcess, modBase, dwTypeIndex, GetType, pInfo);
		}

		WCHAR *GetName()
		{
			// Get the name of the symbol.  This will either be a Type name (if a UDT),
			// or the structure member name.
			if (pwszTypeName) 
				return pwszTypeName;
			SymGetTypeInfo( TI_GET_SYMNAME, &pwszTypeName );
			return pwszTypeName;
		}

		CSymTypeInfo *GetType()
		{
			if (!type) {
				DWORD typeId = 0;
				SymGetTypeInfo( TI_GET_TYPEID, &typeId );
				if (!succeeded)
					OnOutput("TI_GET_TYPEID(%d) failed %X\r\n", dwTypeIndex, GetLastError());
				type = new CSymTypeInfo(&desd, modBase, typeId);
			}
			return type;
		}

		void Clean()
		{
			if (pwszTypeName) { 
				LocalFree(pwszTypeName); 
				pwszTypeName = NULL; 
			}
			if (type) { 
				delete type; 
				type = NULL; 
			}
		}

		DWORD GetTag()
		{
			DWORD dwSymTag = 0;
			SymGetTypeInfo( TI_GET_SYMTAG,  &dwSymTag);
			return dwSymTag;
		}

		ULONG64 GetLength()
		{
			ULONG64 length = 0;
			SymGetTypeInfo( TI_GET_LENGTH, &length );
			return length;
		}

		DWORD GetCount()
		{
			DWORD count = 0;
			SymGetTypeInfo( TI_GET_COUNT, &count);
			if (!succeeded)
				OnOutput("TI_GET_COUNT(%d) failed %X\r\n", dwTypeIndex, GetLastError());
			return count;
		}

		BasicType GetBasicType()
		{
			BasicType basicType;
			if ( SymGetTypeInfo( TI_GET_BASETYPE, &basicType ) )
				return basicType;

			// Get the real "TypeId" of the child.  We need this for the
			// SymGetTypeInfo( TI_GET_TYPEID ) / GetType() call below.
			if ( succeeded = GetType()->SymGetTypeInfo( TI_GET_BASETYPE, &basicType ))
				return basicType;
			return btNoType;
		}

		BOOL IsVirtualBaseClass()
		{
			DWORD dwFlag;
			return SymGetTypeInfo( TI_GET_VIRTUALBASECLASS, &dwFlag) && dwFlag;
		}

		class ChildrenArray
		{
			public:
			DWORD dwCount;
			TI_FINDCHILDREN_PARAMS* pFindParams;
			CSymTypeInfo *pCurChild;
			CSymTypeInfo *pOwner;

			ChildrenArray(CSymTypeInfo* _owner): pOwner(_owner)
			{
				dwCount = 0;
				pFindParams = NULL;
				pCurChild = NULL;
				pOwner->SymGetTypeInfo( TI_GET_CHILDRENCOUNT, &dwCount );
				if ( !dwCount )     // If no children, we're done
					return;

				// Prepare to get an array of "TypeIds", representing each of the children.
				// SymGetTypeInfo(TI_FINDCHILDREN) expects more memory than just a
				// TI_FINDCHILDREN_PARAMS struct has.
				pFindParams = (TI_FINDCHILDREN_PARAMS *const) LocalAlloc( LMEM_FIXED, 
						sizeof(TI_FINDCHILDREN_PARAMS) + dwCount * sizeof(pFindParams->ChildId[0]));
				pFindParams->Count = dwCount;
				pFindParams->Start = 0;

				// Get the array of TypeIds, one for each child type
				if ( !pOwner->SymGetTypeInfo( TI_FINDCHILDREN, pFindParams ) ) {
					dwCount = 0;
					return;
				}
			}
			~ChildrenArray()
			{
				if (pCurChild) { 
					delete pCurChild; 
					pCurChild = NULL; 
				}
				if (pFindParams) { LocalFree(pFindParams); pFindParams = NULL; }
					dwCount = 0;
			}
			CSymTypeInfo* GetChild(const unsigned index)
			{
				DWORD typeId = pFindParams->ChildId[index];
				if (pCurChild) {
					pCurChild->Clean();
					pCurChild->dwTypeIndex = typeId;
				}
				else
					pCurChild = new CSymTypeInfo(&pOwner->desd, pOwner->modBase, typeId);
				return pCurChild;
			}
		}; // class ChildrenArray

		//////////////////////////////////////////////////////////////////////////////
		// If it's a user defined type (UDT), recurse through its members until we're
		// at fundamental types.  When he hit fundamental types, return
		// bHandled = false, so that FormatSymbolValue() will format them.
		//////////////////////////////////////////////////////////////////////////////

		bool DumpTypeIndex(const unsigned nestingLevel, const DWORD_PTR offset, AdressItem * const palPrev = NULL)
		{
			if (offset && desd.dumped->IsIncluded(offset)) {
				OnOutput(" = 0x%X (see above, %ls)\r\n", offset, desd.dumped->last_name);
				return true;
			}
			if(nestingLevel > 10) {
				OnOutput("...\r\n");
				return true;
			}
			AdressItem palCurrent(palPrev, offset);
			bool handled = false;
			DWORD dwSymTag = GetTag();
			if ( WCHAR *pwszTypeName = GetName() ) {
				OnOutput((dwSymTag==7) ? " '%ls'" : " %ls", pwszTypeName );
				palCurrent.SetName( pwszTypeName );
			}
			//OnOutput("{ti=%u,tag=%u}", dwTypeIndex, dwSymTag);
			switch (dwSymTag)
			{
			case 0: // SymTagNull
				handled = true;
				break;
			case 7: // SymTagData // pwszTypeName is member name
				handled = GetType()->DumpTypeIndex(nestingLevel, offset, &palCurrent);
				break;
			case 12: { // SymTagEnum
				ULONG64 length = GetLength();
				if (IsBadReadPtr((void*)offset, length)) 
					break;
				switch (length) {
				case 1: OnOutput("(%u)", (unsigned int)*(unsigned char*)offset); break;
				case 2: OnOutput("(%hu)", *(unsigned short*)offset); break;
				case 4: OnOutput("(%u)", *(unsigned long*)offset); break;
				case 8: OnOutput("(%s)", desd.itoI64u(*(unsigned long long*)offset)); break;
				default:
					OnOutput("unknown variable size %s\r\n", desd.itoI64u(length));
					return false;
				}
				desd.Newline();
				ChildrenArray children(this);
				if ( !children.dwCount )
					break;
				for ( unsigned i = 0; i < children.dwCount; i++ ) {
					CSymTypeInfo *child_info = children.GetChild(i);
					WCHAR * pwszTypeName = child_info->GetName();
					VARIANT val;
					memset(&val, 0, sizeof(val));
					child_info->SymGetTypeInfo( TI_GET_VALUE, &val );
					switch (val.vt) {
					case VT_I2:
						desd.FormatEnumNameValue( nestingLevel, pwszTypeName, true, val.iVal, *(SHORT*)offset);
						break;
					case VT_UI2:
						desd.FormatEnumNameValue( nestingLevel, pwszTypeName, false, val.iVal, *(SHORT*)offset);
						break;
					case VT_INT:
					case VT_I4:
						desd.FormatEnumNameValue( nestingLevel, pwszTypeName, true, val.lVal, *(LONG*)offset);
						break;
					case VT_UINT:
					case VT_UI4:
						desd.FormatEnumNameValue( nestingLevel, pwszTypeName, false, val.lVal, *(LONG*)offset);
						break;
					default:
						desd.MakeIndent( nestingLevel );
						OnOutput(" {vt=%hu}\r\n", val.vt);
					}
				}
				handled = true;
			}
				break;
			case 11: { // SymTagUDT
				int virtual_base_index = 0; // virtual base classes are doubled
				DWORD dwLastVBC = 0, dwUdtKind;
				SymGetTypeInfo( TI_GET_UDTKIND, &dwUdtKind );
				switch (dwUdtKind) {
				case 0: // UdtStruct
					OnOutput(" struct "); break;
				case 1: // UdtClass
					OnOutput(" class "); break;
				case 2: // UdtUnion
					OnOutput(" union "); break;
				}
				// Determine how many children this type has.
				ChildrenArray children(this);
				if ( !children.dwCount ) {     // If no children, we're done
					handled = false;
					break;
				}

				// Append a line feed
				desd.Newline();

				// Iterate through each of the children
				for ( unsigned i = 0; i < children.dwCount; i++ ) {
					CSymTypeInfo *child_info = children.GetChild(i);
					DWORD_PTR dwFinalOffset = NULL;
					DWORD dwSymTag = child_info->GetTag();
					switch (dwSymTag) {
					case 7: // SymTagData // contain member/enum value
						break;
					case 18: { // SymTagBaseClass
						if (child_info->IsVirtualBaseClass()) {
							if (virtual_base_index == 0) {
								if (dwLastVBC == child_info->GetType()->dwTypeIndex)
									virtual_base_index = 1;
								else {
									dwLastVBC = child_info->GetType()->dwTypeIndex;
									continue; // next child
								}
							}
							else
								virtual_base_index++;
							child_info->dwVirtualBaseIndex = virtual_base_index;
						}
					}
						desd.MakeIndent( nestingLevel );
						desd.Print(child_info->dwVirtualBaseIndex ? "virtual " : "base ");
						//OnOutput("base%d:\t", child_info->GetType()->dwTypeIndex);
						dwFinalOffset = offset;
						break;
					case 25: // SymTagVTable
						dwFinalOffset = offset;
						break;
					case 0: // SymTagNull
					case 5: // SymTagFunction
					case 11: // SymTagUDT // typedef
					case 12: // SymTagEnum // typedef
					case 14: // SymTagPointerType
					case 17: // SymTagTypedef
					case 27: // SymTagTypedef
						continue;
					default:
						OnOutput("\t!!! tag=%d\r\n", dwSymTag);
						continue;
					}
					// Get the size of the child member
					CSymTypeInfo *child_type = child_info->GetType();
					ULONG64 length = child_type->GetLength();
					if ( !child_type->succeeded || length == 0 ) {
						OnOutput("TI_GET_LENGTH Error=0x%x or length=%s\r\n", GetLastError(), desd.itoI64u(length));
						continue;
					}
					// Add appropriate indentation level (since this routine is recursive)
					if (dwSymTag != 18/*SymTagBaseClass*/)
						desd.MakeIndent( nestingLevel );
					if (dwSymTag == 25/*SymTagVTable*/)
						desd.Print("+0x0: ");
					// Get the offset of the child member, relative to its parent
					DWORD dwDataKind;
					child_info->SymGetTypeInfo( TI_GET_DATAKIND, &dwDataKind);
					switch (dwDataKind) {
					case 0: // DataIsUnknown
						if (dwFinalOffset == NULL) {
							desd.Println("DataIsUnknown");
							continue;
						}
						break;
					case 6: { // DataIsGlobal
						ULONG64 ulAddr;
						child_info->SymGetTypeInfo( TI_GET_ADDRESS, &ulAddr );
						ULONG64 length = child_info->GetLength();
						OnOutput("static '%ls' 0x%X (Ptr Len = %s)\r\n", child_info->GetName(), *(DWORD*)(DWORD)ulAddr, desd.itoI64u(length));
						continue; // next child
					}
						break;
					case 7: { // DataIsMember
					// Calculate the address of the member
						DWORD dwMemberOffset;
						child_info->SymGetTypeInfo( TI_GET_OFFSET, &dwMemberOffset );
						dwFinalOffset = offset + dwMemberOffset;
						OnOutput("+0x%X: ", dwMemberOffset);
					}
						break;
					default:
						OnOutput(" {DataKind = %d} \r\n", dwDataKind);
						continue; // next child
					}
					// Recurse for each of the child types
					// If the child wasn't a UDT, format it appropriately
					if ( !child_info->DumpTypeIndex( nestingLevel+1, dwFinalOffset, &palCurrent) ) {
						desd.FormatOutputValue( child_info->GetBasicType(), length, (PVOID)dwFinalOffset );
						desd.Newline();
					}
				} // for ( unsigned i = 0; i < dwChildrenCount; i++ )
			}
				handled = true;
				//desd.DontDump(offset);
				break;
			case 13: // SymTagFunctionType
				handled = desd.FormatSymbolByAddr( offset );
				if (handled) 
				desd.Println("");
				break;
			case 14: { // SymTagPointerType
				ULONG64 length = GetType()->GetLength();
				DWORD target = *(PDWORD)offset;
				OnOutput(" = *0x%X (Ptr Len = %s) ", target, desd.itoI64u(length) );
				//OnOutput("* = 0x%X.%s ", target, desd.itoI64u(length) );
				if (!IsBadReadPtr( (PVOID)target, length )) {
					if (palPrev->IsIncluded(target)) {
						OnOutput(" (recursive)\r\n");
						handled = true;
					}
					else {
						modBase = GetModuleBase(desd.hProcess, target);
						handled = GetType()->DumpTypeIndex( nestingLevel, target, &palCurrent);
						if (handled)
							desd.DontDump(target, palCurrent.GetFullname());
					}
				}
				else {
					desd.Newline();
					handled = true;
				}
			}
				break;
			case 15: { // SymTagArrayType
				DWORD count = GetCount();
				if ( succeeded && count ) {
					ULONG64 el_length = GetLength() / count;
					BasicType bt = GetBasicType();
					switch (bt)
					{
					case btChar:
						OnOutput(" char[%d] = ", count);
						desd.FormatStringBuff((char*)offset, count);
						desd.Newline();
						break;
					case btWChar:
						OnOutput(" wchar_t[%d] = L", count);
						desd.FormatStringBuffW((wchar_t*)offset, count);
						desd.Newline();
						break;
					default:
						OnOutput("[%d].%s{bt=%d} = {\r\n", count, desd.itoI64u(el_length), bt );
						for (UINT i=0; (i < (UINT)desd.max_array_items) && (i < (UINT)count); i++) {
							desd.MakeIndent( nestingLevel );
							GetType()->DumpTypeIndex( nestingLevel+1, offset+el_length*i, &palCurrent);
						}
						desd.MakeIndent( nestingLevel );
						OnOutput("... }\r\n");
					}
				}
				else {
					OnOutput("[0] = { }\r\n");
				}
			}
				handled = true;
				break;
			case 16: { // SymTagBaseType
				ULONG64 length = GetLength();
				if ( succeeded && length != 0 )
					desd.FormatOutputValue( GetBasicType(), length, (PVOID)offset );
				desd.Newline();
			}
				handled = true;
				break;
			case 18: { // SymTagBaseClass
				DWORD dwBase = NULL;
				// get base
				if (dwVirtualBaseIndex) {
					LONG vbt_offset;
					DWORD_PTR pVBTable;
					SymGetTypeInfo( TI_GET_VIRTUALBASEPOINTEROFFSET, &vbt_offset);
					pVBTable = *(DWORD_PTR*)(offset + vbt_offset);
					OnOutput(" `vbtable@%d+%d'", vbt_offset, *(int*)(pVBTable+4));
					dwBase = offset + *(int*)(pVBTable+dwVirtualBaseIndex*sizeof(int)) + vbt_offset;
				}
				else if (SymGetTypeInfo( TI_GET_OFFSET, &dwBase))
					dwBase += offset;
				// dump class
				if (!IsBadReadPtr((void*)dwBase, GetType()->GetLength())) {
					OnOutput(" *0x%X ", dwBase);
					handled = GetType()->DumpTypeIndex( nestingLevel, dwBase, palPrev);
					if (handled && dwBase != offset) {
						wchar_t *buf = (wchar_t *)alloca(palCurrent.name_len+100);
						wsprintfW(buf, L"base class %s", palCurrent.name);
						desd.DontDump( dwBase, buf );
					}
				}
				else
				{
					desd.Println("<base class dump is buggy>");
					handled = true;
				}
			}
				break;
			case 24: // SymTagVTableShape
				handled = desd.FormatVTableShape(nestingLevel, GetCount(), offset);
				desd.DontDump(offset, palPrev->GetFullname() );
				break;
			case 25: // SymTagVTable
				desd.Print("`vftable'");
				palCurrent.SetName("`vftable'");
				if (!IsBadReadPtr(*(PVOID*)offset, sizeof(PVOID))) {
					handled = GetType()->DumpTypeIndex( nestingLevel+1, offset, palPrev);
				}
				if (!handled)
					desd.Newline();
				handled = true;
				break;
			default:
				OnOutput("<unhandled tag=%d>\r\n", dwSymTag );
				handled = false;
			}
			return handled;
		}

	}; // class CSymTypeInfo


	static char * BaseTypeAsString( DWORD baseType )
	{
		switch ( baseType )
		{
		case btNoType: return "UDT"; //return "NoType";
		case btVoid: return "Void";
		case btChar: return "Char";
		case btWChar: return "WChar";
		case btInt: return "Int";
		case btUInt: return "UInt";
		case btFloat: return "Float";
		case btBCD: return "BCD";
		case btBool: return "Bool";
		case btLong: return "Long";
		case btULong: return "ULong";
		case btCurrency: return "Currency";
		case btDate: return "Date";
		case btVariant: return "Variant";
		case btComplex: return "Complex";
		case btBit: return "Bit";
		case btBSTR: return "BSTR";
		case btHresult: return "Hresult";
		default: return "???";
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// Given a SYMBOL_INFO representing a particular variable, displays its
	// contents.  If it's a user defined type, display the members and their
	// values.
	//////////////////////////////////////////////////////////////////////////////
	bool FormatSymbolValue( const PSYMBOL_INFO pSym )
	{
		if (pSym->Tag != 7/*SymTagData*/)
			return false;
		// Indicate if the variable is a local or parameter
		if ( pSym->Flags & IMAGEHLP_SYMBOL_INFO_PARAMETER )
			Print( "Parameter " );
		else if ( pSym->Flags & IMAGEHLP_SYMBOL_INFO_LOCAL )
			Print( "Local Var " );

		// If it's a function, don't do anything.
		if ( pSym->Tag == 5 )   // SymTagFunction from CVCONST.H from the DIA SDK
			return false;

		// Emit the variable name
		OnOutput("\'%s\'", pSym->Name );

		DWORD_PTR pVariable = 0;    // Will point to the variable's data in memory

		if ( pSym->Flags & IMAGEHLP_SYMBOL_INFO_REGRELATIVE ) {
			// if ( pSym->Register == 8 )   // EBP is the value 8 (in DBGHELP 5.1)
			{                               //  This may change!!!
				pVariable = pStackFrame->AddrFrame.Offset;
				pVariable += (DWORD_PTR)pSym->Address;
			}
			// else
			//  return false;
		}
		else if ( pSym->Flags & IMAGEHLP_SYMBOL_INFO_REGISTER ) {
			OnOutput("[SYMBOL_INFO.Register=%d]\r\n", pSym->Register );
			return false;   // Don't try to report register variable
		}
		else {
			pVariable = (DWORD_PTR)pSym->Address;   // It must be a global variable
		}

		// Determine if the variable is a user defined type (UDT).  IF so, bHandled will return true.
		CSymTypeInfo info(this, pSym->ModBase, pSym->TypeIndex);
		AdressItem pal(NULL, pVariable);
		pal.SetName( pSym->Name );

		if ( info.DumpTypeIndex( 0, pVariable, &pal) ) {
			if (info.GetTag() == 11) /*SymTagUDT*/
				DontDump( pVariable, pal.name );
		}
		else {
			// The symbol wasn't a UDT, so do basic, stupid formatting of the
			// variable.  Based on the size, we're assuming it's a char, WORD, or DWORD.
			FormatOutputValue( info.GetBasicType(), pSym->Size, (PVOID)pVariable );
			Newline();
		}
		return true;
	}

	static BOOL CALLBACK EnumerateSymbolsCallback(PSYMBOL_INFO  pSymInfo, ULONG SymbolSize, PVOID UserContext)
	{
		DebugEnumSymbolDump *const pThis = (DebugEnumSymbolDump *)UserContext;
		__try
		{
			pThis->FormatSymbolValue( pSymInfo );
			//Newline();
		}
		__except( 1 )
		{
			OnOutput("  punting on symbol %s\r\n", pSymInfo->Name);
		}
		return TRUE;
	}


public:
	// Constructor
	DebugEnumSymbolDump(const HANDLE h_process, const STACKFRAME* const stack_frame):
		hProcess(h_process), pStackFrame(stack_frame)
	{
		max_array_items = 5;
		dumped = NULL;
	}

	~DebugEnumSymbolDump()
	{
		dumped->free();
		dumped = NULL;
	}

	BOOL SymEnumSymbols(DWORD64 modBase = 0)
	{
		return ::SymEnumSymbols( hProcess, modBase, 0, EnumerateSymbolsCallback, this );
	}
};

void DumpStack( PEXCEPTION_POINTERS pExcPtrs, void * const param, LPCSTR lpszDumpPath, _PONSTACKREPORTING pOnStackReporting)
{
	FARPLUS_SYMBOL_INFO info;
	CONTEXT threadContext, *pthreadContext = NULL;
	const HANDLE hProcess = ::GetCurrentProcess();
	const HANDLE hThread = ::GetCurrentThread();
	int symbols_err;
	memset(&info, 0, sizeof(FARPLUS_SYMBOL_INFO));
	if ( SymInitialize( hProcess, NULL, FALSE )) {
		// force undecorated names to get params
		DWORD dw = SymGetOptions();
		dw &= ~SYMOPT_UNDNAME;
		SymSetOptions(dw);
		symbols_err = 0;
	}
	else
		symbols_err = GetLastError();

	OnOutput("===== begin exception handling =====\r\n" );

	if (pExcPtrs) {
		const EXCEPTION_RECORD *er = pExcPtrs->ExceptionRecord;
		while (er) {
			(void)GetModuleBase(hProcess, (DWORD)er->ExceptionAddress); // load module symbols
			ResolveSymbol( hProcess, (DWORD)er->ExceptionAddress, info );
			OnOutput("  Exception at 0x%8.8X: (%s) : %s\r\n", er->ExceptionAddress, info.szModule, info.szSymbol);
			if (info.bLineInfo) {
				OnOutput("    in %s (%u,%u)\r\n", info.lineInfo.FileName, info.lineInfo.LineNumber, info.dwLineDisplacement);
			}
			OnOutput("  Exception code: 0x%8.8X", er->ExceptionCode);
			switch (er->ExceptionCode) {
			case EXCEPTION_ACCESS_VIOLATION:
				OnOutput(" (Access violation: %s address 0x%8.8X)\r\n", er->ExceptionInformation[0]?"write":"read", er->ExceptionInformation[1]);
				break;
			default:
				OnOutput("  (Parameters: ");
				for (UINT i=0; i < er->NumberParameters; i++)
					OnOutput("%8.8X ", er->ExceptionInformation[i]);
				OnOutput(")\r\n");
			}
			er = er->ExceptionRecord;
			if (er) OnOutput("*** Nested exception ***\r\n");
		}
		pthreadContext = pExcPtrs->ContextRecord;
/*
		OnOutput("Context: EAX:%08X EBX:%08X ECX:%08X EDX:%08X ESI:%08X EDI:%08X\r\n",
				pthreadContext->Eax, pthreadContext->Ebx,
				pthreadContext->Ecx, pthreadContext->Edx,
				pthreadContext->Esi, pthreadContext->Edi);
		OnOutput("         CS:EIP:%04X:%08X  Flags:%08X\r\n",
				pthreadContext->SegCs, pthreadContext->Eip,
				pthreadContext->EFlags );
		OnOutput("         SS:ESP:%04X:%08X  EBP:%08X\r\n",
				pthreadContext->SegSs, pthreadContext->Esp,
				pthreadContext->Ebp );
		OnOutput("         DS:%04X  ES:%04X  FS:%04X  GS:%04X\r\n",
				pthreadContext->SegDs, pthreadContext->SegEs,
				pthreadContext->SegFs, pthreadContext->SegGs );
*/
	}
	OnOutput("===== end exception handling =====\r\n" );
	OnOutput("\r\n" );

	//if ( symbols_initialized )
	OnOutput("===== begin DumpStack output =====\r\n" );
	if ( symbols_err ) {
		OnOutput("DumpStack Error: IMAGEHLP.DLL wasn't found. GetLastError() returned 0x%8.8X\r\n", symbols_err );
	}
	if (!pthreadContext) {
		threadContext.ContextFlags = CONTEXT_FULL;
	if ( GetThreadContext( hThread, &threadContext ) )
		pthreadContext=&threadContext;
	}
	if ( pthreadContext ) {
		STACKFRAME stackFrame;
		DebugEnumSymbolDump desd(hProcess, &stackFrame);
		ZeroMemory( &stackFrame, sizeof( stackFrame ) );

		stackFrame.AddrPC.Offset    = pthreadContext->Eip;
		stackFrame.AddrPC.Mode      = AddrModeFlat;

		stackFrame.AddrStack.Offset = pthreadContext->Esp;
		stackFrame.AddrStack.Mode   = AddrModeFlat;

		stackFrame.AddrFrame.Offset = pthreadContext->Ebp;
		stackFrame.AddrFrame.Mode   = AddrModeFlat;

		for (int nFrame = 0; nFrame < 1024; nFrame++) {
			if ( StackWalk( IMAGE_FILE_MACHINE_I386, hProcess, hThread, &stackFrame, pthreadContext, NULL, SymFunctionTableAccess, GetModuleBase, NULL) == 0)
				break;
			if ( 0 == stackFrame.AddrFrame.Offset ) // Basic sanity check to make sure
				break;                          // the frame is OK.  Fail if not.

			if (!desd.FormatSymbolByAddr( stackFrame.AddrPC.Offset ))
				OnOutput("0x%8.8X: unknown", stackFrame.AddrPC.Offset );
			OnOutput("\r\n");

			IMAGEHLP_STACK_FRAME imagehlpStackFrame;
			imagehlpStackFrame.InstructionOffset = stackFrame.AddrPC.Offset;
			SymSetContext( hProcess, &imagehlpStackFrame, 0 );
			// Enumerate the locals/parameters
			desd.SymEnumSymbols();
		}
		//OnOutput("======== global variables output =====\r\n" );
		//if (pExcPtrs)
		//  desd.SymEnumSymbols(GetModuleBase(hProcess, (DWORD)pExcPtrs->ExceptionRecord->ExceptionAddress));
	}
	if ( symbols_err == 0)
		SymCleanup( hProcess );
	OnOutput("====== end DumpStack output ======\r\n" );
	DumpOutput(info.szModule, lpszDumpPath, pOnStackReporting);
}
