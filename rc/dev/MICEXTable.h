#pragma once

#include "MICEXChangesInfo.h"
#include "MICEXTableField.h"
#include "MICEXTableKey.h"

#define GetFieldValue(table, nFieldNum, pRowBuf, nFieldLen) \
	(pRowBuf + table->m_pOutputFields[nFieldNum].m_lFieldOffset); \
	*nFieldLen = table->m_pOutputFields[nFieldNum].m_lFieldSize;

#define SetFieldValue(table, nFieldNum, pRowBuf, pFieldBuf) \
	memcpy(pRowBuf + table->m_pOutputFields[nFieldNum].m_lFieldOffset, pFieldBuf, table->m_pOutputFields[nFieldNum].m_lFieldSize);

#define TABLE_FOSECTYPES	"FOSECTYPES"
#define TABLE_SECURITIES	"SECURITIES"
#define TABLE_TRADES		"TRADES"
#define TABLE_USTRADES		"USTRADES"

#define FIELD_SECBOARD	"SECBOARD"
#define FIELD_SECCODE	"SECCODE"

#define FIELD_TRADENO	"TRADENO"
#define FIELD_BUYSELL	"BUYSELL"

#define FIELD_SECURITIES_FOSECTYPEID	"FOSECTYPEID"
#define FIELD_SECURITIES_DECIMALS		"DECIMALS"
#define FIELD_SECURITIES_LOTSIZE		"LOTSIZE"
#define FIELD_SECURITIES_MARKETCODE		"MARKETCODE"
#define FIELD_SECURITIES_CHANGE			"CHANGE"

#define FIELD_SECTYPE "SECTYPE"

#define FIELD_USTRADES_STATUS		"STATUS"


typedef vector<LPBYTE>		TABLEDATAVECTOR;
typedef vector<int32>		KEYFIELDSVECTOR;

class CFEEDConfig;
class CFEEDConfigTable;

class CMICEXSection;
class CMICEXSchema;
class CMICEXTable;

CString MakeTimeStamp();

class CMICEXTable 
{
public:	
	CMICEXTable(CMICEXSchema* pSchema, CString csTableName, CFEEDConfigTable* pConfig);
	~CMICEXTable();

public:	
	void ClearTableData();

public:	
	CMICEXChangesInfo* UpdateTableRow(int nFields, BYTE* pFieldBuf, int32 nDataLen, BYTE* pDataBuf, CString& csTimeIn);

public:	
	LPCSTR GetEmptyFieldValToString(int nFieldNum, CString& csOut);
	LPCSTR MICEXTime2String(LPCSTR lpszMCXTime, CString& csOut);
	LPCSTR FieldValToString(int nFieldNum, BYTE* pDataBuf, CString& csOut);
	int GetFieldNumByName(LPCSTR lpszFieldName);

public:
	CString					m_csTableName;
	CString					m_csTableDescr;
	int32					m_lAttributes;

	int32					m_hTableHandle;
	int32					m_nTableRef;
	CRITICAL_SECTION		m_hTableAccess;

	int						m_nInputFields;
	int						m_nOutputFields;

	CMICEXTableField*		m_pInputFields;
	CMICEXTableField*		m_pOutputFields;

	KEYFIELDSVECTOR			m_vKeyFields;
	int32					m_nKeySize;

	long					m_lRowDataSize;

	TABLEDATAVECTOR			m_vTableData;
	TABLEKEYMAPTOPOSITION	m_mTableKeys;

public:
	CMICEXSchema*			m_pSchema;
	CFEEDConfigTable*	m_pConfig;
};

typedef vector<CMICEXTable*>		VEC_MICEX_TABLES;
typedef map<CString, CMICEXTable*>	MAP_MICEX_TABLES;