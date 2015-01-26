#pragma once

#include "MICEXTable.h"
#include "MICEXTables.h"
#include "MICEXEnum.h"
#include "MICEXEnums.h"
#include "FEEDConfig.h"

class CMICEXSection;

class CMICEXSchema
{
public:
	CMICEXSchema(CMICEXSection* pMICEXSection);
	~CMICEXSchema(void);

public:
	char* GetMICEXStringValue(char** pData);

public:
	CMICEXTable* GetTable(int nTableID);
	CMICEXTable* GetTable(CString csName);

public:
	void ParseMICEXTableFields(char** pData, BOOL fOutput, CMICEXTable* pTable);
	void ParseMICEXTables(char** pData);
	void ParseMICEXEnumTypes(char** pData);
	void ParseMICEXSchema(char* pData, long nLen);

public:
	CString GetField(CMICEXTable* pTable, CMICEXTableKey key, LPCSTR lpszFieldName);
	CString GetSECURITIESField(LPCSTR lpszBoard, LPCSTR lpszSecName, LPCSTR lpszFieldName);
	CString GetUSTRADESField(LPCSTR lpszTradeNo, LPCSTR lpszBuySell, LPCSTR lpszFieldName);
	CString GetTRADESField(LPCSTR lpszTradeNo, LPCSTR lpszBuySell, LPCSTR lpszFieldName);

	LPVOID GetFOSECTYPESData(LPCSTR lpszTypeID);
	LPVOID GetSECURITIESData(LPCSTR lpszBoard, LPCSTR lpszSecName);
	LPVOID GetUSTRADESData(LPCSTR lpszBoard, LPCSTR lpszSecName);

public:
	CMICEXChangesInfo* MICEXParseRowData(char** pData, CMICEXTable* pTable, CString& csTimeIn);
	void MICEXParseTableData(char** pData, CMICEXTable* pTable);

public:
	CMICEXTables*	m_pTables;
	CMICEXEnums*	m_pEnums;

public:
	CMICEXSection*				m_pSection;
	CFEEDConfigSection*	m_pConfig;
};
