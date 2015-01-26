#include "stdafx.h"
#include "MICEXSchema.h"

#include "FHLogger.h"
#include "MICEXSection.h"

CMICEXSchema::CMICEXSchema(CMICEXSection* pSection)
{
	m_pSection			= pSection;
	m_pConfig			= pSection->m_pConfig;

	m_pTables			= new CMICEXTables();
	m_pEnums			= new CMICEXEnums();
}

CMICEXSchema::~CMICEXSchema(void)
{
	delete m_pTables;
	delete m_pEnums;
}

	
CMICEXTable* CMICEXSchema::GetTable(int nTableID)
{
	return m_pTables->GetTable(nTableID);
}

CMICEXTable* CMICEXSchema::GetTable(CString csName)
{
	return m_pTables->GetTable(csName);
}

	
char* CMICEXSchema::GetMICEXStringValue(char** pData)
{
	// Returned buffer MUST be free in calling procedure through delete [];
	long lDataLen = *((int32*)*pData);
	LPSTR lpszTmpBuff;
	if(lDataLen == NULL) 
	{
		lpszTmpBuff = new char[1];
		lpszTmpBuff[0] = 0;
		*pData += sizeof(int32);
		return lpszTmpBuff;
	}

	lpszTmpBuff = new char[lDataLen + 1];
	memset(lpszTmpBuff, 0, lDataLen + 1);
	*pData += sizeof(int32);
	memcpy(lpszTmpBuff, *pData, lDataLen);
	*pData += lDataLen;
	return lpszTmpBuff;
}

	
void CMICEXSchema::ParseMICEXTableFields(char** pData, BOOL fOutput, CMICEXTable* pTable)
{
/*
	КолвоПолей		Integer
	Поле1			TField
*/
	long lFields = *((int32*)*pData);
	*pData += sizeof(int32);

	CMICEXTableField* pFields;

	if(fOutput) 
	{
		pTable->m_nOutputFields = lFields;
		pTable->m_pOutputFields = new CMICEXTableField[lFields];
		pFields = pTable->m_pOutputFields;
	}
	else 
	{
		pTable->m_nInputFields = lFields;
		pTable->m_pInputFields = new CMICEXTableField[lFields];
		pFields = pTable->m_pInputFields;
	}

	int32 lFieldOffset = 0;

	for(long nCnt = 0; nCnt < lFields; nCnt++) 
	{
		char* pszString;
		pszString = GetMICEXStringValue(pData);
		pFields[nCnt].m_csFieldName = pszString;
		delete [] pszString;

		pszString = GetMICEXStringValue(pData);
		pFields[nCnt].m_csFieldDesc = pszString;
		delete [] pszString;

		pFields[nCnt].m_lFieldSize = *((int32*)*pData);
		*pData += sizeof(int32);

		// Offset for get/set values.
		pFields[nCnt].m_lFieldOffset = lFieldOffset;
		lFieldOffset += pFields[nCnt].m_lFieldSize;

		if(fOutput)  // Count total bytes in row
			pTable->m_lRowDataSize += pFields[nCnt].m_lFieldSize;

		pFields[nCnt].m_lFieldType = *((int32*)*pData);
		*pData += sizeof(int32);

		pFields[nCnt].m_lFieldAttr = *((int32*)*pData);
		*pData += sizeof(int32);

		if(fOutput) 
		{
			if(pFields[nCnt].m_lFieldAttr & 1)
			{
				pTable->m_vKeyFields.push_back(nCnt);
				pTable->m_nKeySize += pFields[nCnt].m_lFieldSize;
			}
		}

		pszString = GetMICEXStringValue(pData);
		pFields[nCnt].m_csCountType = pszString;
		delete [] pszString;

		if(!fOutput) 
		{
			pszString = GetMICEXStringValue(pData);
			pFields[nCnt].m_csDefValue = pszString;
			delete [] pszString;
		}
	}
	return;

}


void CMICEXSchema::ParseMICEXTables(char** pData)
{
/*
	КолвоТаблиц		Integer
	Таблица1		TTable
*/
	char* pszString;
	long lTables = *((int32*)*pData);
	*pData += sizeof(int32);

	m_pTables->Clear();

	for(long nCnt = 0; nCnt < lTables; nCnt++) 
	{
		/*
			Имя				String
			Описание		String
			Атрибуты		TTableFlags
			ВходныеПоля		TFields
			ВыходныеПоля	TFields
		*/

		pszString = GetMICEXStringValue(pData);
		CString csTableName = pszString;
		delete [] pszString;

		CMICEXTable* pTable = new CMICEXTable(this, csTableName, m_pSection->m_pConfig->GetTableConfig(csTableName));

		pszString = GetMICEXStringValue(pData);
		pTable->m_csTableDescr = pszString;
		delete [] pszString;

		pTable->m_lAttributes = *((int32*)*pData);
		*pData += sizeof(int32);

		ParseMICEXTableFields(pData, FALSE, pTable);
		ParseMICEXTableFields(pData, TRUE, pTable);

		m_pTables->AddTable(pTable);
		pTable->m_nTableRef = m_pTables->m_vecMICEXTables.size() - 1;

		m_pSection->UpdateTableStructure(pTable);
	}
}


void CMICEXSchema::ParseMICEXEnumTypes(char** pData)
{
/*
	КолвоТипов			Integer
	Тип1				TEnumType
*/
	char* pszString;
	long lTypes = *((int32*)*pData);
	*pData += sizeof(int32);

	m_pEnums->Clear();

	for(long nCnt = 0; nCnt < lTypes; nCnt++) 
	{
/*
		Имя					String
		Описание			String
		Размер				Integer
		Тип					TEnumKind
		КолвоКонстант		Integer
		Константа1			String
*/
		CMICEXEnum* pEnum = new CMICEXEnum();

		pszString = GetMICEXStringValue(pData);
		pEnum->m_csEnumName = pszString;
		delete [] pszString;

		pszString = GetMICEXStringValue(pData);
		pEnum->m_csEnumDescr = pszString;
		delete [] pszString;

		pEnum->m_lEnumSize = *((int32*)*pData);
		*pData += sizeof(int32);

		pEnum->m_lEnumKind = *((int32*)*pData);
		*pData += sizeof(int32);

		long nConsts = *((int32*)*pData);
		pEnum->m_lConstants = nConsts;
		*pData += sizeof(int32);

		for(long nCntConsts = 0; nCntConsts < nConsts; nCntConsts++) 
		{
			pszString = GetMICEXStringValue(pData);
			pEnum->m_cslConstants.AddTail(pszString);
			delete [] pszString;
		}

		m_pEnums->AddEnum(pEnum);
	}
	return;
}


void CMICEXSchema::ParseMICEXSchema(char* pData, long nLen)
{
/*
	ИмяИнтерфейса		String
	ОписаниеИнтерфейса	String
	ПеречислимыеТипы	TEnumTypes
	Таблицы				TTables
	Транзакции			TTransactions
*/
	char* pDataBuffPtr = pData;
	char* pszString;
	LOG_INFO(SECTION(lSystem), "Parsing of DB Schema...");
	pszString = GetMICEXStringValue(&pDataBuffPtr);
	LOG_INFO(SECTION(lSystem), "Interface Name: %s", (LPCTSTR)pszString);
	delete [] pszString;
	if(pDataBuffPtr < (pData + nLen)) 
	{
		pszString = GetMICEXStringValue(&pDataBuffPtr);
		delete [] pszString;
	}

	if(pDataBuffPtr < (pData + nLen)) 
	{
		ParseMICEXEnumTypes(&pDataBuffPtr);
	}

	if(pDataBuffPtr < (pData + nLen)) 
	{
		ParseMICEXTables(&pDataBuffPtr);
	}

	LOG_INFO(SECTION(lSystem), "Parsing of DB Schema completed");
}

	
CMICEXChangesInfo*  CMICEXSchema::MICEXParseRowData(char** pData, CMICEXTable* pTable, CString& csTimeIn)
{
	/*
		КолвоПолей		Byte
		ДлинаДанных		Integer
		НомераПолей		Byte[КолвоПолей]
		ДанныеПолей		Byte[ДлинаДанных]
	*/

	BYTE nFieldsNum;
	int32 nDataLength;

	nFieldsNum = ((TMTERowStruct*)*pData)->NumFields;
	*pData += sizeof(BYTE); // skip Number Of Fields
	nDataLength = *((int32*)*pData);
	*pData += sizeof(int32); // skip Data Length

	BYTE* pFieldsArray = NULL;
	if(nFieldsNum != 0) 
	{
		pFieldsArray = new BYTE[nFieldsNum];
		memset(pFieldsArray, 0, nFieldsNum);
		memcpy(pFieldsArray, *pData, nFieldsNum);
	}
	*pData += nFieldsNum; // skip Fields numbers

	BYTE* pD = new BYTE[nDataLength];
	memset(pD, 0, nDataLength);
	memcpy(pD, *pData, nDataLength);
	*pData += nDataLength;

	CMICEXChangesInfo* pChangesInfo = NULL;
	if(pTable != NULL) 
		pChangesInfo = pTable->UpdateTableRow(nFieldsNum, pFieldsArray, nDataLength, pD, csTimeIn);
	
	delete [] pD;
	if(pFieldsArray != NULL)
		delete [] pFieldsArray;

	return pChangesInfo;
}

void CMICEXSchema::MICEXParseTableData(char** pData, CMICEXTable* pTable)
{
	CString csTimeIn = MakeTimeStamp();

	/*
		Ref			Integer
		КолвоСтрок		Integer
		Строка1		TMTERow
	*/

	m_pSection->UpdateTableBegin(pTable);

	*pData += sizeof(int32); // skip Ref
	int32 nRows = *((int32*)*pData);
	*pData += sizeof(int32); // skip RecordNumber
	
	for(int32 nRowsCnt = 0; nRowsCnt < nRows; nRowsCnt++) 
	{
		CMICEXChangesInfo* pChangesInfo = MICEXParseRowData(pData, pTable, csTimeIn);

		if (pTable != NULL && pChangesInfo != NULL) // has changes
			m_pSection->UpdateRowComplete(pChangesInfo);
	}

	if(pTable != NULL)
		m_pSection->UpdateTableComplete(pTable);
}

	
CString CMICEXSchema::GetField(CMICEXTable* pTable, CMICEXTableKey key, LPCSTR lpszFieldName)
{
	CString csFieldValue;

	TABLEKEYMAPTOPOSITION::const_iterator keyIterator = pTable->m_mTableKeys.find(key);

	if (keyIterator == pTable->m_mTableKeys.end())
		return csFieldValue;
	
	LPBYTE pRowData = pTable->m_vTableData.at(keyIterator->second);

	pTable->FieldValToString(pTable->GetFieldNumByName(lpszFieldName), pRowData, csFieldValue);
	
	return csFieldValue;
}

CString CMICEXSchema::GetSECURITIESField(LPCSTR lpszBoard, LPCSTR lpszSecName, LPCSTR lpszFieldName)
{
	CMICEXTable* pTable = m_pTables->GetTable("SECURITIES");
	if (pTable == NULL)
		return "";

	CMICEXTableKey key = CMICEXTableKey::GetSecuritiesKey(pTable, lpszBoard, lpszSecName);

	return GetField(pTable, key, lpszFieldName);
}

LPVOID CMICEXSchema::GetFOSECTYPESData(LPCSTR lpszTypeID)
{
	CMICEXTable* pTable = m_pTables->GetTable(TABLE_FOSECTYPES);
	if (pTable == NULL)
		return NULL;

	CMICEXTableKey key = CMICEXTableKey::GetSecTypesKey(pTable, lpszTypeID);

	TABLEKEYMAPTOPOSITION::const_iterator keyIterator = pTable->m_mTableKeys.find(key);

	if (keyIterator == pTable->m_mTableKeys.end())
		return NULL;
	
	return pTable->m_vTableData.at(keyIterator->second);
}

LPVOID CMICEXSchema::GetSECURITIESData(LPCSTR lpszBoard, LPCSTR lpszSecName)
{
	CMICEXTable* pTable = m_pTables->GetTable(TABLE_SECURITIES);
	if (pTable == NULL)
		return NULL;

	CMICEXTableKey key = CMICEXTableKey::GetSecuritiesKey(pTable, lpszBoard, lpszSecName);

	TABLEKEYMAPTOPOSITION::const_iterator keyIterator = pTable->m_mTableKeys.find(key);

	if (keyIterator == pTable->m_mTableKeys.end())
		return NULL;
	
	return pTable->m_vTableData.at(keyIterator->second);
}

CString CMICEXSchema::GetUSTRADESField(LPCSTR lpszTradeNo, LPCSTR lpszBuySell, LPCSTR lpszFieldName)
{
	CMICEXTable* pUSTradeTable = m_pTables->GetTable("USTRADES");
	if (pUSTradeTable == NULL)
		return "";

	CMICEXTableKey keyUSTrades = CMICEXTableKey::GetUSTradesKey(pUSTradeTable, lpszTradeNo, lpszBuySell);

	return GetField(pUSTradeTable, keyUSTrades, lpszFieldName);
}

LPVOID CMICEXSchema::GetUSTRADESData(LPCSTR lpszTradeNo, LPCSTR lpszBuySell)
{
	CMICEXTable* pUSTradeTable = m_pTables->GetTable("USTRADES");
	if (pUSTradeTable == NULL)
		return "";

	CMICEXTableKey keyUSTrades = CMICEXTableKey::GetUSTradesKey(pUSTradeTable, lpszTradeNo, lpszBuySell);

	TABLEKEYMAPTOPOSITION::const_iterator keyIterator = pUSTradeTable->m_mTableKeys.find(keyUSTrades);

	if (keyIterator == pUSTradeTable->m_mTableKeys.end())
		return NULL;
	
	return pUSTradeTable->m_vTableData.at(keyIterator->second);
}


CString CMICEXSchema::GetTRADESField(LPCSTR lpszTradeNo, LPCSTR lpszBuySell, LPCSTR lpszFieldName)
{
	CMICEXTable* pTradeTable = m_pTables->GetTable("TRADES");
	if (pTradeTable == NULL)
		return "";

	CMICEXTableKey keyTrades = CMICEXTableKey::GetTradesKey(pTradeTable, lpszTradeNo, lpszBuySell);

	return GetField(pTradeTable, keyTrades, lpszFieldName);
}



	
	