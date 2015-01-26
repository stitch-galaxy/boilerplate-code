#include "stdafx.h"
#include "MICEXConnector.h"

#include "FEEDConfig.h"
#include "FHLogger.h"

#include "MICEXSection.h"
#include "MICEXSchema.h"

CMICEXConnector::CMICEXConnector(CMICEXSection* pSection)
	: CFEEDThread(CFEEDConfig::s_pConfigInstance->m_csExchange)
{
	m_pSection			= pSection;
	m_pSchema			= pSection->m_pSchema;
	m_pConfig			= pSection->m_pConfig;

	m_bConnected		= FALSE;
	m_bWaitingForConnection = FALSE;

	m_iConnection		= 0;
}

CMICEXConnector::~CMICEXConnector()
{
}

	
BOOL CMICEXConnector::RunBegin()
{
	time(&m_timeLastCheck);
	m_timeLastCheck = m_timeLastCheck - 120;
	time(&m_timeLastStateUpdate);
	m_timeLastStateUpdate = m_timeLastStateUpdate - 120;
	m_iLastState = 0;

	UpdateState(1);

	return TRUE;
}

void CMICEXConnector::RunStep()
{
	CheckConnection();

	UpdateState(m_bConnected ? 2 : 1);

	if (!m_bConnected)
		return;

	ProcessTables();
}

void CMICEXConnector::RunEnd()
{
	UpdateState(0);

	Disconnect();
}

	
BOOL CMICEXConnector::IsLogonTime()
{
	time_t tCurr; time(&tCurr);
	struct tm tmCurr = *localtime(&tCurr);
	int iCurDay = tmCurr.tm_wday;
	int iCurM = tmCurr.tm_hour * 60 + tmCurr.tm_min;

	TCHAR szTemp[2];
	if (m_pConfig->m_csLogonDays.Find(itoa(iCurDay, szTemp, 10)) < 0)
		return FALSE;

	int iLogonH, iLogonM;
	if (sscanf((LPCSTR)m_pConfig->m_csLogonTime, "%2d:%2d", &iLogonH, &iLogonM) != 2)
	{
		LOG_ERROR(SECTION(lSystem), "Invalid logon time format");
		return FALSE;
	}
	iLogonM = iLogonH * 60 + iLogonM;

	int iLogoffH, iLogoffM;
	if (sscanf((LPCSTR)m_pConfig->m_csLogoffTime, "%2d:%2d", &iLogoffH, &iLogoffM) != 2)
	{
		LOG_ERROR(SECTION(lSystem), "Invalid logoff time format");
		return FALSE;
	}
	iLogoffM = iLogoffH * 60 + iLogoffM;

	if (iLogonM >= iCurM || iCurM >= iLogoffM)
		return FALSE;

	return TRUE;
}

void CMICEXConnector::CheckConnection()
{
	time_t timeCur; time(&timeCur);
	if (m_timeLastCheck > timeCur - 60)
		return;
	m_timeLastCheck = timeCur;

	if (!m_bConnected)
	{
		if (IsLogonTime()) {
			m_bWaitingForConnection = FALSE;
			Connect();
		}
		else {
			if(!m_bWaitingForConnection) {
				m_bWaitingForConnection = TRUE;
				LOG_INFO(SECTION(lSystem), "Waiting for %s logon time...", m_csThreadName);
			}
		}
	}
	else
	{
		if (!IsLogonTime())
		{
			Disconnect();
			LOG_INFO(SECTION(lSystem), "Waiting for %s logon time...", m_csThreadName);
			m_iConnection = 0;
			m_bWaitingForConnection = TRUE;
		}
	}
}

	
void CMICEXConnector::UpdateState(int iState)
{
	time_t timeCur; time(&timeCur);
	if (m_iLastState == iState && m_timeLastStateUpdate > timeCur - 5)
		return;
	m_timeLastStateUpdate = timeCur;
	m_iLastState = iState;

	m_pSection->UpdateSessionState(iState);
}

	
CString CMICEXConnector::GetConnectionString()
{
	CString csConnectionString = m_pConfig->m_vecConnections.at(m_iConnection);

	m_iConnection++;
	if ((unsigned int)m_iConnection >= m_pConfig->m_vecConnections.size())
		m_iConnection = 0;

	return csConnectionString;
}

	
BOOL CMICEXConnector::Connect()
{
	LOG_INFO(SECTION(lSystem), "Attempt to connect %s section...", (LPCSTR)m_pConfig->m_csName);

	char ErrorMsg[255];

	m_Idx = MTEConnect((LPSTR)(LPCSTR)GetConnectionString(), ErrorMsg);
	
	if( m_Idx < MTE_OK ) 
	{
		LOG_ERROR(SECTION(lSystem), "%s section connection error: %s", (LPCSTR)m_pConfig->m_csName, (LPCSTR)ErrorMsg);
		return FALSE;
	}

	if (!ParseSchema() || !OpenTables())
	{
		Disconnect();

		return FALSE;
	}

	m_bConnected = TRUE;

	return TRUE;
}

void CMICEXConnector::Disconnect()
{
	if (m_Idx < MTE_OK) 
		return;
	
	LOG_INFO(SECTION(lSystem), "Attempt to disconnect %s section...", (LPCSTR)m_pConfig->m_csName);

	CloseTables();

	LOG_INFO(SECTION(lSystem), "%s: Disconnecting...", m_pConfig->m_csName);

	int32 Err;
	if ((Err = MTEDisconnect(m_Idx)) == MTE_OK )
	{
		LOG_INFO(SECTION(lSystem), "%s  section disconnected.", (LPCSTR)m_pConfig->m_csName);
	}
	else
	{
		LOG_ERROR(SECTION(lSystem), "%s  section disconnect error: %s", (LPCSTR)m_pConfig->m_csName, MTEErrorMsg(Err));
	}

	m_bConnected = FALSE;
}

BOOL CMICEXConnector::ReConnect()
{
	Disconnect();
	return Connect();
}

	
BOOL CMICEXConnector::ParseSchema()
{
	UpdateState(m_bConnected ? 2 : 1);

	if(m_Idx >= MTE_OK) 
	{
		LOG_INFO(SECTION(lSystem), "%s  section connection established.", (LPCSTR)m_pConfig->m_csName);

		TMTEMsg* Msg;
		int32 Err;

		if((Err = MTEStructure(m_Idx, &Msg)) != MTE_OK) 
		{
			CString csErrorMsg;

			if( Err == MTE_TSMR )
			{
 				char* pData = (char *)(Msg + 1);
				csErrorMsg.Format("%s structure error: %s", m_csThreadName, pData);
			}
			else
			{
				csErrorMsg.Format("%s structure error: %s", m_csThreadName, MTEErrorMsg(Err));
			}

			LOG_ERROR(SECTION(lSystem), "%s  section structure error: %s", (LPCSTR)m_pConfig->m_csName, (LPCSTR)csErrorMsg);
			return FALSE;
		}
		else 
		{
			LOG_INFO(SECTION(lSystem), "%s information objects description received.", (LPCSTR)m_pConfig->m_csName);

			m_pSchema->ParseMICEXSchema((char *)(Msg + 1), Msg->DataLen);

			LOG_INFO(SECTION(lSystem), "%s information objects description parsed.", (LPCSTR)m_pConfig->m_csName);
		}
	}

	UpdateState(m_bConnected ? 2 : 1);

	return TRUE;;
}
	
BOOL CMICEXConnector::OpenTables()
{
	BOOL IsSuccess = TRUE;

	for (VEC_CONFIG_TABLES::iterator it = m_pConfig->m_vecTables.begin(); it != m_pConfig->m_vecTables.end(); it++)
	{
		UpdateState(m_bConnected ? 2 : 1);

		CFEEDConfigTable* pConfigMicexTable = *it;

		LOG_INFO(SECTION(lSystem), "%s: opening %s table...", (LPCSTR)m_pConfig->m_csName, (LPCSTR)pConfigMicexTable->m_csTableName);
		
		CMICEXTable* pTable = m_pSchema->GetTable(pConfigMicexTable->m_csTableName);

		TMTEMsg* Msg;
		char* pData;
		char* pTempPointer;

		try
		{
			pTable->m_hTableHandle = MTE_NOTCONNECTED;
			pTable->m_hTableHandle = MTEOpenTable(m_Idx, (LPSTR)(LPCSTR)pTable->m_csTableName, (LPSTR)(LPCSTR)pTable->m_pConfig->m_csTableParams, pTable->m_pConfig->m_bTableComplete, &Msg);
		}
		catch(...)
		{
		}

		if(pTable->m_hTableHandle >= MTE_OK)
		{
			pData = (char *)(Msg + 1);
			pTempPointer = pData;

			m_pSchema->MICEXParseTableData(&pTempPointer, pTable);
			
			LOG_INFO(SECTION(lSystem), "%s: opening %s completed.", (LPCSTR)m_pConfig->m_csName, (LPCSTR)pConfigMicexTable->m_csTableName);
		}
		else 
		{
			LOG_ERROR(SECTION(lSystem), "%s: unable to open %s table.", (LPCSTR)m_pConfig->m_csName, (LPCSTR)pConfigMicexTable->m_csTableName);
//			CFEEDLog::s_pLogInstance->FormatLogMessage(EVENTLOG_ERROR_TYPE, "%s: unable to open %s table.\n", (LPCSTR)m_pConfig->m_csName, (LPCSTR)pConfigMicexTable->m_csTableName);

			IsSuccess = FALSE;
		}
	}

	return IsSuccess;
}

void CMICEXConnector::CloseTables()
{
	for (VEC_CONFIG_TABLES::iterator it = m_pConfig->m_vecTables.begin(); it != m_pConfig->m_vecTables.end(); it++)
	{
		LOG_INFO(SECTION(lSystem), "%s: closing %s table...", (LPCSTR)m_pConfig->m_csName, (LPCSTR)(*it)->m_csTableName);

		CFEEDConfigTable* pConfigMicexTable = *it;
        
		CMICEXTable* pTable = m_pSchema->GetTable(pConfigMicexTable->m_csTableName);

		if(pTable->m_hTableHandle >= MTE_OK)
		{
			MTECloseTable(m_Idx, pTable->m_hTableHandle);
			pTable->m_hTableHandle = MTE_TEUNAVAIL;
		}
	}
}

	
BOOL CMICEXConnector::ProcessTables()
{
	TMTEMsg* Msg;
	int32 Err;

	if(m_Idx >= MTE_OK) 
	{
		for (VEC_CONFIG_TABLES::iterator it = m_pConfig->m_vecTables.begin(); it != m_pConfig->m_vecTables.end(); it++)
		{
			CFEEDConfigTable* pConfigMicexTable = *it;
            
			CMICEXTable* pTable = m_pSchema->GetTable(pConfigMicexTable->m_csTableName);

			if(pTable->m_hTableHandle >= MTE_OK)
				MTEAddTable(m_Idx, pTable->m_hTableHandle, pTable->m_nTableRef);
		}

		if((Err = MTERefresh(m_Idx, &Msg)) == MTE_OK) 
		{
			char* pData = (char *)(Msg + 1);

			if(pData != NULL) 
			{
				char* pTempPointer = pData;
				int32 nTables = *((int32*)pTempPointer);
				pTempPointer += sizeof(int32);

				for(int32 nTableCnt = 0; nTableCnt < nTables; nTableCnt++) 
				{
					int32 nRefTable = *((int32*)pTempPointer);
					CMICEXTable* pTable = m_pSchema->GetTable(nRefTable);

					m_pSchema->MICEXParseTableData(&pTempPointer, pTable);
				}
			}
		}
		else
		{
			LOG_ERROR(SECTION(lSystem), "%s: unable to refresh tables.", (LPCSTR)m_pConfig->m_csName);
			return ReConnect();
		}
	}

	return TRUE;
}

