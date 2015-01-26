#pragma once

#include <vector>

#include "MICEXTable.h"

#include "FEEDConfig.h"
#include "FEEDThread.h"

typedef vector<CString> MAP_DB_OPERATIONS;

class CMICEXDB : 
	public CFEEDThread
{
public:
	CMICEXDB(CMICEXSection* pSection);
	~CMICEXDB();

public:
	BOOL RunBegin();
	void RunStep();
	void RunEnd();

public:
	BOOL AddUpdate(CMICEXChangesInfo* pChangesInfo);
	CMICEXChangesInfo* GetUpdate();

	void AddBegin(CMICEXChangesInfo* pChangesInfo);

	void ClearChangesQueue();

protected:
	BOOL Update(CMICEXChangesInfo* pChangesInfo);

	BOOL Connect();
	void Disconnect();

	BOOL CreateTable(CMICEXTable* pTable);
	BOOL CheckTableStructure(CMICEXTable* pTable, CStringList& cslNewColumns, BOOL& bExistTable);
	
	BOOL ClearTable(CMICEXTable* pTable, BOOL IsFullClear);
	BOOL SaveTableRowInDB(CMICEXTable* pTable, BYTE* pDataBuf, BOOL fUpdate);

	BOOL Exec(LPCSTR lpszSQLStatement);

	LPCSTR MakeColumnsList(CMICEXTable* pTable, CString& csColumns);
	void GetColumnsAndValuesLists(CMICEXTable* pTable, CString& csColumns, CString& csValues, BYTE* pDataBuf);
	void GetColumnsAndValues4Update(CMICEXTable* pTable, CString& csColumnsAndVals, BYTE* pDataBuf);
	void GetWHERE4Update(CMICEXTable* pTable, CString& csWhereClause, BYTE* pDataBuf);

public:
	CMICEXSection*				m_pSection;
	CFEEDConfigSection*			m_pConfig;

protected:
	int							m_bInitialized;
	_ConnectionPtr				m_pSQLSrvConnection;
	BOOL						m_bConnected;

	MAP_DB_OPERATIONS			m_vecDBOper;

protected:
	CRITICAL_SECTION			m_critChangesInfos;
	VEC_CHANGES_INFO			m_vecChangesInfos;
};

typedef map<CString, CMICEXDB*> MAP_DB_CONNECTORS;