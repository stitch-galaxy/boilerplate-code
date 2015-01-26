#include "stdafx.h"
#include "MICEXDB.h"

#include "FEEDConfig.h"
#include "FHLogger.h"

#include "MICEXSection.h"
#include "MICEXTable.h"

inline void TESTHR(HRESULT x) {if FAILED(x) _com_issue_error(x);};

CMICEXDB::CMICEXDB(CMICEXSection* pSection) : CFEEDThread("DB")
{
	m_pSection	= pSection;
	m_pConfig	= pSection->m_pConfig;

	m_bInitialized		= FALSE;
	m_pSQLSrvConnection	= NULL;
	m_bConnected		= FALSE;

	InitializeCriticalSection(&m_critChangesInfos);
}

CMICEXDB::~CMICEXDB(void)
{
	DeleteCriticalSection(&m_critChangesInfos);
}

	
BOOL CMICEXDB::RunBegin()
{
	m_bInitialized = Connect();

	return m_bInitialized;
}

void CMICEXDB::RunStep()
{
	int iRetries = 0;

	CMICEXChangesInfo* pChangesInfo = NULL;
	while((pChangesInfo = GetUpdate()) != NULL)
	{
		if (pChangesInfo->m_eChangesType != changesTable)
		{
			delete pChangesInfo;

			continue;
		}
		
		if (!Update(pChangesInfo))
		{
			AddBegin(pChangesInfo);

			Sleep(1000);

			iRetries++;
			if (iRetries == 30)
			{
				StopProcess();

				return;
			}
		}

		if(::WaitForSingleObject(m_hStop, 0) == WAIT_OBJECT_0)
			break;
	}
}

void CMICEXDB::RunEnd()
{
	Disconnect();

	m_bInitialized = FALSE;

	ClearChangesQueue();
}


	
BOOL CMICEXDB::AddUpdate(CMICEXChangesInfo* pChangesInfo)
{
	if (!m_bInitialized)
		return FALSE;

	EnterCriticalSection(&m_critChangesInfos);

	m_vecChangesInfos.push_back(pChangesInfo);

	LeaveCriticalSection(&m_critChangesInfos);

	return TRUE;
}

void CMICEXDB::AddBegin(CMICEXChangesInfo* pChangesInfo)
{
	if (!m_bInitialized)
		return;

	EnterCriticalSection(&m_critChangesInfos);

	m_vecChangesInfos.insert(m_vecChangesInfos.begin(), pChangesInfo);

	LeaveCriticalSection(&m_critChangesInfos);
}

CMICEXChangesInfo* CMICEXDB::GetUpdate()
{
	CMICEXChangesInfo* pChangesInfo = NULL;

	if (!m_bInitialized)
		return pChangesInfo;

	EnterCriticalSection(&m_critChangesInfos);

	if (!m_vecChangesInfos.empty())
	{
		pChangesInfo = (*m_vecChangesInfos.begin());
		m_vecChangesInfos.erase(m_vecChangesInfos.begin());
	}

	LeaveCriticalSection(&m_critChangesInfos);

	return pChangesInfo;
}

	
void CMICEXDB::ClearChangesQueue()
{
	EnterCriticalSection(&m_critChangesInfos);

	for (VEC_CHANGES_INFO::iterator it = m_vecChangesInfos.begin(); it != m_vecChangesInfos.end(); it++)
		delete (CMICEXChangesInfo*)(*it);
	m_vecChangesInfos.clear();

	LeaveCriticalSection(&m_critChangesInfos);
}

	
BOOL CMICEXDB::Update(CMICEXChangesInfo* pChangesInfo)
{
	if (!m_pConfig->m_bUseDB)
		return TRUE;

	BOOL bRes = FALSE;

	if (pChangesInfo->m_bIsInit)
		bRes = CreateTable(pChangesInfo->m_pTable);
	else if (pChangesInfo->m_bIsClear)
		bRes = ClearTable(pChangesInfo->m_pTable, TRUE);
	else
		bRes = SaveTableRowInDB(pChangesInfo->m_pTable, pChangesInfo->m_pRowData, pChangesInfo->m_bIsExistRow);

	delete pChangesInfo;

	return bRes;
}

	
int PrintProviderError(_ConnectionPtr pConnection, LPSTR lpszProvider)
{
    // Print Provider Errors from Connection object.
    // pErr is a record object in the Connection's Error collection.
    ErrorPtr pErr  = NULL;
    long nCount  = 0;
    long i    = 0;
	if(pConnection == NULL)
		return 0;
    if( ((pConnection->GetErrors())->Count) > 0) {
        nCount = (pConnection->GetErrors())->Count;
        for(i = 0; i < nCount; i++) {
            pErr = (pConnection->GetErrors())->GetItem(i);
			LOG_ERROR(SECTION(lSystem), "%s - Error number: %x - %s", (LPSTR)lpszProvider, (LPCSTR) pErr->NativeError, (LPCSTR) pErr->Description);
			TRACE("Error number: %x - %s\n\r", (LPCSTR) pErr->Number, (LPCSTR) pErr->Description);
        }
		(pConnection->GetErrors())->Clear();
    }
	return nCount;
}

void PrintComError(_com_error &e, _ConnectionPtr pSQLSrvConnection)
{
	if(PrintProviderError(pSQLSrvConnection, "SQLServer") != 0)
		return;
    _bstr_t bstrSource(e.Source());
    _bstr_t bstrDescription(e.Description());
    TRACE("\nError\n");
    TRACE("Code = %08lx\n", e.Error());
    TRACE("Code meaning = %s\n", e.ErrorMessage());
    TRACE("Source = %s\n", (LPCSTR) bstrSource);
    TRACE("Description = %s\n", (LPCSTR) bstrDescription);  
	LOG_ERROR(SECTION(lSystem), "Error number: %08lx - %s", e.Error(), (LPCSTR)e.Description());
}

LPCSTR GetToday(CString& csDate)
{
	struct tm tmCurr;
	time_t tCurr;
	time(&tCurr);
	tmCurr = *localtime(&tCurr);
	csDate.Format("%02d/%02d/%04d", tmCurr.tm_mon + 1, tmCurr.tm_mday, tmCurr.tm_year + 1900);

	return (LPCSTR)csDate;
}


LPCSTR VarToString(_variant_t vValue, CString& csRet)
{
	csRet = "";
	if(vValue.vt != VT_NULL) {
		csRet = (LPSTR)_bstr_t(vValue);
	}
	return (LPCSTR)csRet;
}


LPCSTR GetSQLType(int nFieldType, int nFieldSize, int nFieldAttr, CString& csSQLType)
{
    csSQLType = "";
	CString csFormat;

	if(nFieldAttr & 1) // Key field
		csFormat = "nvarchar(%d) not null";
	else 
		csFormat = "nvarchar(%d) null";

    switch(nFieldType) {
		case 0: // char
			csSQLType.Format((LPCSTR)csFormat, nFieldSize);
			break;
		case 1: // Integer
			csSQLType.Format((LPCSTR)csFormat, nFieldSize);
			break;
		case 2:  // Fixed
			csSQLType.Format((LPCSTR)csFormat, nFieldSize + 2);
			break;
		case 3: // Float
			csSQLType.Format((LPCSTR)csFormat, nFieldSize + 2);
			break;
		case 4: // Date
			csSQLType.Format((LPCSTR)csFormat, nFieldSize);
			break;
		case 5: // Time
			csSQLType.Format((LPCSTR)csFormat, nFieldSize + 2);
			break;
        default:
			csSQLType = "";
            break;
	}

	return (LPCSTR)csSQLType;
}

	
BOOL CMICEXDB::Connect()
{
	if (!m_pConfig->m_bUseDB)
		return TRUE;

	BOOL fResult = TRUE;

	LOG_INFO(SECTION(lSystem), "Connecting to Database '%s' on '%s'", (LPSTR)(LPCSTR)m_pConfig->m_csSQLDatabase, (LPSTR)(LPCSTR)m_pConfig->m_csSQLServer);

	CString csConnect_String = "Provider=SQLOLEDB.1;Integrated Security=SSPI;Persist Security Info=False;Initial Catalog=";
	csConnect_String = csConnect_String + (LPCSTR)m_pConfig->m_csSQLDatabase;
	csConnect_String = csConnect_String + ";Data Source=";
	csConnect_String = csConnect_String + (LPCSTR)m_pConfig->m_csSQLServer;
	csConnect_String = csConnect_String + ";Locale Identifier=1049;Connect Timeout=15;Use Procedure for Prepare=1;Auto Translate=True;Packet Size=4096;";


	_bstr_t strSQLServerConn((LPSTR)(LPCSTR)csConnect_String);

	// open connection
	try 
	{
		// SQL Server
		TESTHR(m_pSQLSrvConnection.CreateInstance(__uuidof(Connection)));

		m_pSQLSrvConnection->PutConnectionTimeout(3600);
		m_pSQLSrvConnection->PutCommandTimeout(3600);

		m_pSQLSrvConnection->Open(strSQLServerConn,"","",NULL);
	}
	catch(_com_error &e) 
	{
		PrintComError(e, m_pSQLSrvConnection);
		fResult = FALSE;
	}

	m_bConnected = fResult;
	
	if(m_bConnected) 
	{
		LOG_INFO(SECTION(lSystem), "Database '%s' on '%s' connected", (LPSTR)(LPCSTR)m_pConfig->m_csSQLDatabase, (LPSTR)(LPCSTR)m_pConfig->m_csSQLServer);
	}
	else 
	{
		Disconnect();
	}

	return m_bConnected;
}

void CMICEXDB::Disconnect()
{
	if (!m_pConfig->m_bUseDB)
		return;

	if(m_pSQLSrvConnection != NULL) 
	{
		if(m_pSQLSrvConnection->GetState() == adStateOpen) 
			m_pSQLSrvConnection->Close();

		LOG_INFO(SECTION(lSystem), "Database '%s' on '%s' disconnected", (LPSTR)(LPCSTR)m_pConfig->m_csSQLDatabase, (LPSTR)(LPCSTR)m_pConfig->m_csSQLServer);
	}

	m_pSQLSrvConnection = NULL;
	m_bConnected = FALSE;
}



	
BOOL CMICEXDB::Exec(LPCSTR lpszSQLStatement)
{
	_variant_t vRows;
	vRows.vt = VT_I2;
	BOOL fResult = TRUE;

	int nAttempt = 0; 

	if(!m_bConnected) 
	{
		if(!Connect())
			return FALSE;
	}

	do 
	{

		nAttempt++;
		try 
		{
			m_pSQLSrvConnection->Execute(lpszSQLStatement, &vRows, NULL);
			fResult = TRUE;
		}
		catch(_com_error ex) 
		{
			LOG_ERROR(SECTION(lSystem), "%s ON %s", (LPCSTR)ex.Description(), lpszSQLStatement);
			fResult = FALSE;
		}
		catch(...) 
		{
			LOG_ERROR(SECTION(lSystem), "%s ON %s", (LPCSTR)"Unknown Error", lpszSQLStatement);
			fResult = FALSE;
		}
 
		if(!fResult) 
		{
			Disconnect();

			if(!Connect())
				return FALSE;
		}
	}
	while(!fResult && nAttempt < 3);

	m_pSQLSrvConnection->GetErrors()->Clear();

	return fResult;
}

	
BOOL CMICEXDB::CreateTable(CMICEXTable* pTable)
{
	CString csCommandString;

	CString csCols = "";
	CStringList cslColumns;
	
	if (!m_pConfig->m_bUseDB) 
		return TRUE;
	
	// Add UPDATE_DATE to all tables

	csCols = csCols + "[UPDATE_DATE] nvarchar(10) null";
	cslColumns.AddTail("UPDATE_DATE");

	for(int nCCount = 0; nCCount < pTable->m_nOutputFields; nCCount++) 
	{
		if(!csCols.IsEmpty())
			csCols = csCols + ", ";

		csCols = csCols + "[" + (LPCSTR)pTable->m_pOutputFields[nCCount].m_csFieldName + "]";
		csCols = csCols + " ";

		CString csTempFormat;
		GetSQLType(pTable->m_pOutputFields[nCCount].m_lFieldType, pTable->m_pOutputFields[nCCount].m_lFieldSize, pTable->m_pOutputFields[nCCount].m_lFieldAttr, csTempFormat);
		csCols = csCols + csTempFormat;

		cslColumns.AddTail((LPCSTR)pTable->m_pOutputFields[nCCount].m_csFieldName);
	}


	BOOL bExistTable = FALSE;

	if (CheckTableStructure(pTable, cslColumns, bExistTable)) 
	{
		LOG_INFO(SECTION(lSystem), "Table '%s' structure in Database is OK", (LPCSTR)pTable->m_csTableName);
		
		ClearTable(pTable, FALSE);

		return TRUE;
	}

	if (bExistTable)
	{
		LOG_INFO(SECTION(lSystem), "Recreating table '%s' in Database", (LPCSTR)pTable->m_csTableName);

		csCommandString.Format("DROP TABLE dbo.%s", (LPCSTR)pTable->m_csTableName);
		if(!Exec((LPCSTR)csCommandString)) 
		{
			LOG_ERROR(SECTION(lSystem), "ERROR Dropping TABLE '%s' in Database", (LPCSTR)pTable->m_csTableName);
			return FALSE;
		}
	}
	else
	{
		LOG_INFO(SECTION(lSystem), "Creating table '%s' in Database", (LPCSTR)pTable->m_csTableName);
	}

	// Create Table
	csCommandString.Format("CREATE TABLE dbo.%s (\n%s\n)\n", (LPCSTR)pTable->m_csTableName, (LPCSTR)csCols);

	if(Exec((LPCSTR)csCommandString)) 
	{
		LOG_INFO(SECTION(lSystem), "Table '%s' in Database is ready to use", (LPCSTR)pTable->m_csTableName);
	}
	else 
	{
		LOG_ERROR(SECTION(lSystem), "ERROR Creating TABLE '%s' in Database", (LPCSTR)pTable->m_csTableName);
		LOG_FATAL(SECTION(lSystem), "%s Feed Stopped due to DataBase Errors", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csServiceName);

		return FALSE;
	}

	return TRUE;
}

BOOL CMICEXDB::CheckTableStructure(CMICEXTable* pTable, CStringList& cslNewColumns, BOOL& bExistTable)
{
	CString csSQLStatement;

	_RecordsetPtr   pRs  = NULL;

	_variant_t vRows;
	vRows.vt = VT_I2;
	BOOL fResult = TRUE;

	if(!m_bConnected && !Connect())
		return FALSE;

	csSQLStatement.Format("SELECT name FROM syscolumns WHERE id = (SELECT id FROM sysobjects WHERE (xtype = 'U') AND (name = '%s')) ORDER BY colorder", pTable->m_csTableName);


	try 
	{
		pRs = m_pSQLSrvConnection->Execute((LPCSTR)csSQLStatement, &vRows, adCmdText);

		bExistTable = !pRs->GetEndOfFile();

	    if(((m_pSQLSrvConnection->GetErrors())->Count) == 0) {
			POSITION pos;
			CString csCol;
			CString csDBCol;
			BOOL fNotFound;
		   _variant_t Index;
			Index.vt = VT_I2;
			Index.iVal = 0;

			for( pos = cslNewColumns.GetHeadPosition(); pos != NULL; ) {
				csCol = cslNewColumns.GetNext(pos);
				csCol.TrimLeft(); csCol.TrimRight();
				fNotFound = TRUE;

				while(!pRs->GetEndOfFile()) {
					VarToString(pRs->Fields->Item[Index]->Value, csDBCol);
					csDBCol.TrimLeft(); csDBCol.TrimRight();
					if(csDBCol.CompareNoCase((LPCSTR)csCol) == 0) {
						fNotFound = FALSE;
						fResult = TRUE;
						break;
					}
					pRs->MoveNext();
				}

				if(fNotFound) {
					fResult = FALSE;
					break;
				}
			}

			if(fResult) {
				pRs->MoveFirst();

				while(!pRs->GetEndOfFile()) {
					VarToString(pRs->Fields->Item[Index]->Value, csDBCol);
					csDBCol.TrimLeft(); csDBCol.TrimRight();

					fNotFound = TRUE;
					for( pos = cslNewColumns.GetHeadPosition(); pos != NULL; ) {
						csCol = cslNewColumns.GetNext(pos);
						csCol.TrimLeft(); csCol.TrimRight();
						if(csDBCol.CompareNoCase((LPCSTR)csCol) == 0) {
							fNotFound = FALSE;
							fResult = TRUE;
							break;
						}
					}
					if(fNotFound) {
						fResult = FALSE;
						break;
					}

					pRs->MoveNext();
				}

			}
		}
		else 
		{
			PrintProviderError(m_pSQLSrvConnection, "SQL Server");
			fResult = FALSE;
		}
	}
	catch(_com_error ex) 
	{
		LOG_ERROR(SECTION(lSystem), "%s ON %s", (LPCSTR)ex.Description(), (LPCSTR)csSQLStatement);
		fResult = FALSE;
	}
	catch(...) 
	{
		LOG_ERROR(SECTION(lSystem), "%s ON %s", (LPCSTR)"Unknown Error", (LPCSTR)csSQLStatement);
		fResult = FALSE;
	}

	if(pRs != NULL) 
	{
		if(pRs->GetState() == 1)
			pRs->Close();	
		pRs = NULL;
	}

	m_pSQLSrvConnection->GetErrors()->Clear();

	return fResult;
}




	
BOOL CMICEXDB::ClearTable(CMICEXTable* pTable, BOOL IsFullClear)
{

	CString csCommandString;

	LOG_INFO(SECTION(lSystem), "Clearing %s %s in DataBase...", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csExchange, pTable->m_csTableName);

	if (IsFullClear)
		csCommandString.Format("TRUNCATE TABLE dbo.%s", (LPCSTR)pTable->m_csTableName);
	else
		csCommandString.Format("DELETE FROM dbo.%s "
		"WHERE ((CONVERT(DATETIME, UPDATE_DATE, 101) < DATEADD(day, -%d, GETDATE())) "
			"OR (UPDATE_DATE = CONVERT(VARCHAR(10), GETDATE(), 101)))", 
					(LPCSTR)pTable->m_csTableName, m_pConfig->m_iSaveDays);

	if(!Exec((LPCSTR)csCommandString))
	{
		LOG_ERROR(SECTION(lSystem), "Unable to clear %s %s in DataBase", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csExchange, (LPCSTR)pTable->m_csTableName);
		return FALSE;
	}
	else
	{
		LOG_INFO(SECTION(lSystem), "Clearing %s %s in DataBase completed", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csExchange, (LPCSTR)pTable->m_csTableName);
		return TRUE;
	}
}

	
BOOL CMICEXDB::SaveTableRowInDB(CMICEXTable* pTable, BYTE* pDataBuf, BOOL fUpdate)
{
	CString csColumns;
	CString csValues;
	CString csWHERE;
	CString csCommandString;

	if(!fUpdate) 
	{
		GetColumnsAndValuesLists(pTable, csColumns, csValues, pDataBuf);

		if(!csColumns.IsEmpty() && !csValues.IsEmpty()) 
		{
			csCommandString.Format("INSERT INTO %s (%s) VALUES(%s)", (LPCSTR)pTable->m_csTableName, (LPCSTR)csColumns, (LPCSTR)csValues);

			if(!Exec((LPCSTR)csCommandString)) 
			{
				LOG_ERROR(SECTION(lSystem), "Unable to insert row into TABLE '%s'", (LPCSTR)pTable->m_csTableName);
				LOG_FATAL(SECTION(lSystem), "%s Feed Stopped due to DataBase Errors", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csServiceName);
				return FALSE;
			}
		}
	} 
	else 
	{
		GetColumnsAndValues4Update(pTable, csColumns, pDataBuf);
		GetWHERE4Update(pTable, csWHERE, pDataBuf);

		if(!csColumns.IsEmpty() && !csWHERE.IsEmpty()) 
		{
			csCommandString.Format("UPDATE %s SET %s WHERE %s", (LPCSTR)pTable->m_csTableName, (LPCSTR)csColumns, (LPCSTR)csWHERE);
			
			if(!Exec((LPCSTR)csCommandString)) 
			{
				LOG_ERROR(SECTION(lSystem), "Unable to update row in TABLE '%s'", (LPCSTR)pTable->m_csTableName);
				LOG_FATAL(SECTION(lSystem), "%s Feed Stopped due to DataBase Errors", (LPCSTR)CFEEDConfig::s_pConfigInstance->m_csServiceName);

				return FALSE;
			}
		}
	}

	return TRUE;
}

	
LPCSTR CMICEXDB::MakeColumnsList(CMICEXTable* pTable, CString& csColumns)
{
	csColumns = "[UPDATE_DATE] ";
	// Make Columns list
	
	for(int nCCount = 0; nCCount < pTable->m_nOutputFields; nCCount++) 
	{
		if(!csColumns.IsEmpty())
			csColumns = csColumns + ", ";

		csColumns = csColumns + "[" + (LPCSTR)pTable->m_pOutputFields[nCCount].m_csFieldName + "]";
		csColumns = csColumns + " ";
	}

	return (LPCSTR)csColumns;
}


void CMICEXDB::GetColumnsAndValuesLists(CMICEXTable* pTable, CString& csColumns, CString& csValues, BYTE* pDataBuf)
{
	CString csFieldVal;
	
	csColumns = "[UPDATE_DATE] ";
	csValues.Format("'%s' ", GetToday(csFieldVal));

	for(int nCCount = 0; nCCount < pTable->m_nOutputFields; nCCount++) 
	{
		if(!csColumns.IsEmpty())
			csColumns = csColumns + ", ";

		csColumns = csColumns + "[" + (LPCSTR)pTable->m_pOutputFields[nCCount].m_csFieldName + "]";
		csColumns = csColumns + " ";

		if(!csValues.IsEmpty())
			csValues = csValues + ", ";

		pTable->FieldValToString(nCCount, pDataBuf, csFieldVal);

		csFieldVal.Replace("'", "''");

		csValues = csValues + "'" + (LPCSTR)csFieldVal + "'";
		csValues = csValues + " ";
	}
}


void CMICEXDB::GetColumnsAndValues4Update(CMICEXTable* pTable, CString& csColumnsAndVals, BYTE* pDataBuf)
{

	CString csFieldVal;
	csColumnsAndVals = "";
	
	for(int nCCount = 0; nCCount < pTable->m_nOutputFields; nCCount++) 
	{
		if(pTable->m_pOutputFields[nCCount].m_lFieldAttr & 1)  // Key field
			continue;
		
		if(!csColumnsAndVals.IsEmpty())
			csColumnsAndVals = csColumnsAndVals + ", ";
		csColumnsAndVals = csColumnsAndVals + "[" + (LPCSTR)pTable->m_pOutputFields[nCCount].m_csFieldName + "]";
		csColumnsAndVals = csColumnsAndVals + " = ";

		pTable->FieldValToString(nCCount, pDataBuf, csFieldVal);

		csFieldVal.Replace("'", "''");

		csColumnsAndVals = csColumnsAndVals + "'" + (LPCSTR)csFieldVal + "'";
		csColumnsAndVals = csColumnsAndVals + " ";
	}

}


void CMICEXDB::GetWHERE4Update(CMICEXTable* pTable, CString& csWhereClause, BYTE* pDataBuf)
{
	CString csFieldVal;
	csWhereClause = "";
	// Make Columns list
	for(int nCCount = 0; nCCount < pTable->m_nOutputFields; nCCount++) 
	{
		if(pTable->m_pOutputFields[nCCount].m_lFieldAttr & 1) // Key field
		{
			if(!csWhereClause.IsEmpty())
				csWhereClause = csWhereClause + " AND ";

			csWhereClause = csWhereClause + " ([" + (LPCSTR)pTable->m_pOutputFields[nCCount].m_csFieldName + "]";
			csWhereClause = csWhereClause + " = ";

			pTable->FieldValToString(nCCount, pDataBuf, csFieldVal);

			csFieldVal.Replace("'", "''");

			csWhereClause = csWhereClause + "'" + (LPCSTR)csFieldVal + "'";
			csWhereClause = csWhereClause + ") ";

			continue;
		}
	}
}





	