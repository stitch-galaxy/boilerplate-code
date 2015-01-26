#include "stdafx.h"
#include "RMDSConnector.h"

#include "FEEDConfig.h"
#include "FHLogger.h"

#include "MICEXDB.h"

CRMDSConnector*	CRMDSConnector::s_pRMDSInstance = NULL;

// FIX-906 begin
string CRMDSConnector::MakeSectionsName(const char* lpTableName)
{
	string sRet("FH.MD.");
	if(lpTableName != NULL)
		sRet += lpTableName;
	else
		sRet += "UNKNOWN";
	return sRet;
}

#define SECTION_BY_NAME(param) CRMDSConnector::MakeSectionsName(param).c_str()
// FIX-906 end

CRMDSConnector::CRMDSConnector() : CRMDSLib(), CTIBRVLib(), CFEEDThread("RMDS")
{
	m_bInitialized	= FALSE;
 
	m_iSSLSessionID			= -1;
	m_iTIBRVSessionID		= -1;
	m_iTIBRVCertSessionID	= -1;

	InitializeCriticalSection(&m_critChangesInfos);
}

CRMDSConnector::~CRMDSConnector(void)
{
	m_Subjects.clear(); // FIX-752
	DeleteCriticalSection(&m_critChangesInfos);
}

	
BOOL CRMDSConnector::RunBegin()
{

	////////////////////////////////////////////////
	// Open SSL Session

	CFEEDConfigRMDS* pRMDSConfig = CFEEDConfig::s_pConfigInstance->m_pRMDSConfig;
	if (pRMDSConfig->m_bEnabled)
	{

		if (!InitializeRMDSLibrary())
		{
			LOG_FATAL(SECTION(lSystem), "Unable to initialize SSL library.");
			return FALSE;
		}

		LOG_INFO(SECTION(lSystem), "Opening SSL Session...");

		m_iSSLSessionID = SSL_InitSSL_Source(pRMDSConfig->m_csFeedName, SSL_SRC_SOURCE_DRIVEN, NULL);

		if(m_iSSLSessionID < 0) 
		{
			LOG_FATAL(SECTION(lSystem), "Unable to Start SSL Session - %s", SSL_GetSSLStatusMessage());
			return FALSE;
		}

		LOG_INFO(SECTION(lSystem), "SSL Session Started");
		SSL_SetSRCReady(m_iSSLSessionID);
	}

	////////////////////////////////////////////////
	// Open TIBRV Session

	CFEEDConfigTIBRV* pTIBRVConfig = CFEEDConfig::s_pConfigInstance->m_pTIBRVConfig;
	CFEEDConfigTIBRV* pTIBRVCertConfig = CFEEDConfig::s_pConfigInstance->m_pTIBRVCertConfig;

	if(pTIBRVConfig->m_bEnabled || pTIBRVCertConfig->m_bEnabled)
	{
		if (!InitializeTIBRVLibrary())
		{
			LOG_FATAL(SECTION(lSystem), "Unable to initialize TIBRV library.");
			return FALSE;
		}

	if (pTIBRVConfig->m_bEnabled)
	{
		LOG_INFO(SECTION(lSystem), "Opening TIBRV Session...");

		m_iTIBRVSessionID = TIBRV_InitSession(
						RDV_LINK_PUBLISHER,
						NULL, 0, NULL,
						pTIBRVConfig->m_csServiceName, pTIBRVConfig->m_csNetworkName, pTIBRVConfig->m_csDaemonName,
						NULL, FALSE, NULL, FALSE);

		if(m_iTIBRVSessionID < 0) 
		{
			LOG_FATAL(SECTION(lSystem), "Unable to Start TIBRV Session - %s", SSL_GetSSLStatusMessage());
			return FALSE;
		}

		LOG_INFO(SECTION(lSystem), "TIBRV Session Started");
	}

	////////////////////////////////////////////////
	// Open TIBRV Certified Session
	if (pTIBRVCertConfig->m_bEnabled)
	{
		LOG_INFO(SECTION(lSystem), "Opening TIBRV Certified Session...");

		m_iTIBRVCertSessionID = TIBRV_InitSession(
						RDV_LINK_CM_PUBLISHER,
						NULL, 0, NULL,
						(pTIBRVCertConfig->m_csServiceName.IsEmpty() == TRUE) ? (char*)NULL : pTIBRVCertConfig->m_csServiceName, 
						(pTIBRVCertConfig->m_csNetworkName.IsEmpty() == TRUE) ? (char*)NULL : pTIBRVCertConfig->m_csNetworkName, 
						(pTIBRVCertConfig->m_csDaemonName.IsEmpty() == TRUE) ? (char*)NULL : pTIBRVCertConfig->m_csDaemonName, 
						pTIBRVCertConfig->m_csCmName, pTIBRVCertConfig->m_bRequestOld, pTIBRVCertConfig->m_csLedgerFile, pTIBRVCertConfig->m_bSyncLedger);

		if(m_iTIBRVCertSessionID < 0) 
		{
			LOG_FATAL(SECTION(lSystem), "Unable to Start TIBRV Certified Session - %s", SSL_GetSSLStatusMessage());
			return FALSE;
		}

		LOG_INFO(SECTION(lSystem), "TIBRV Certified Session Started");
	}
	}
	////////////////////////////////////////////////

	m_bInitialized	= TRUE;

	return TRUE;
}

void CRMDSConnector::RunStep()
{
	MSG msg;
	while(PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) 
	{
		if (msg.message == WM_QUIT)
			return;

		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	CMICEXChangesInfo* pChangesInfo = NULL;
	while((pChangesInfo = GetUpdate()) != NULL)
	{
		Update(pChangesInfo);

		if(::WaitForSingleObject(m_hStop, 0) == WAIT_OBJECT_0)
			break;
	}
}

void CRMDSConnector::RunEnd()
{
	RunStep();

	time_t timeStart, curTime;
	time(&timeStart); time(&curTime);
	while (timeStart > curTime - 5)
	{
		time(&curTime);

		MSG msg;
		while(PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) 
		{
			if (msg.message == WM_QUIT)
				return;

			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	if(m_iSSLSessionID != -1) 
	{
		SSL_FreeSSL(m_iSSLSessionID);
		m_iSSLSessionID = -1;
	}

	if(m_iTIBRVSessionID != -1) 
	{
		TIBRV_ReleaseSession(m_iTIBRVSessionID);
		m_iTIBRVSessionID = -1;
	}

	if(m_iTIBRVCertSessionID != -1) 
	{
		TIBRV_ReleaseSession(m_iTIBRVCertSessionID);
		m_iTIBRVCertSessionID = -1;
	}

	UninitializeRMDSLibrary();
	UninitializeTIBRVLibrary();

	m_bInitialized	= FALSE;

	ClearChangesQueue();
	ReleaseMessageCreators();
}

	
BOOL CRMDSConnector::Update(CMICEXChangesInfo* pChangesInfo)
{
	if (pChangesInfo->m_eChangesType == changesTable)
	{
		PublishTable(pChangesInfo);
	}
	else if (pChangesInfo->m_eChangesType == changesVolume)
	{
		PublishVolume(pChangesInfo);
	}
	else if (pChangesInfo->m_eChangesType == changesAlive)
	{
		PublishAlive(pChangesInfo);
	}

	if (pChangesInfo->m_pTable != NULL)
	{
		CMICEXSection*	pSection	= pChangesInfo->m_pSection;
		CMICEXTable*	pTable		= pChangesInfo->m_pTable;

		if (pTable->m_pConfig->m_bSaveToDB && pSection->m_pDB != NULL)
			return pSection->m_pDB->AddUpdate(pChangesInfo);
	}

	delete pChangesInfo;

	return TRUE;
}

	
BOOL CRMDSConnector::AddUpdate(CMICEXChangesInfo* pChangesInfo)
{
	if (!m_bInitialized)
		return FALSE;

	EnterCriticalSection(&m_critChangesInfos);

	if (pChangesInfo->m_bIsPriority)
		m_vecChangesInfos.insert(m_vecChangesInfos.begin(), pChangesInfo);
	else
		m_vecChangesInfos.push_back(pChangesInfo);

	LeaveCriticalSection(&m_critChangesInfos);

	return TRUE;
}

CMICEXChangesInfo* CRMDSConnector::GetUpdate()
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

	
void CRMDSConnector::ClearChangesQueue()
{
	EnterCriticalSection(&m_critChangesInfos);

	for (VEC_CHANGES_INFO::iterator it = m_vecChangesInfos.begin(); it != m_vecChangesInfos.end(); it++)
		delete (CMICEXChangesInfo*)(*it);
	m_vecChangesInfos.clear();

	LeaveCriticalSection(&m_critChangesInfos);
}

	
void CRMDSConnector::ReleaseMessageCreators()
{
    for (MAP_MESSAGE_CREATORS::iterator itMessageCreator = m_mapMessageCreators.begin(); itMessageCreator != m_mapMessageCreators.end(); itMessageCreator++)
	{
		CRMDSMessageCreator::Delete(itMessageCreator->second);
	}
	m_mapMessageCreators.clear();
}

CRMDSMessageCreator* CRMDSConnector::GetAliveMessageCreator(CMICEXSection* pSection)
{
	CString csKey; 
	csKey.Format("%s-%s", pSection->m_pConfig->m_csName, "Alive");

	MAP_MESSAGE_CREATORS::const_iterator itMC = m_mapMessageCreators.find(csKey);
	if (itMC != m_mapMessageCreators.end())
		return itMC->second;

	CRMDSMessageCreator* pMessageCreator = CRMDSMessageCreator::Create(
							pSection->m_pConfig->m_pAlive);

	m_mapMessageCreators[csKey] = pMessageCreator;

	return pMessageCreator;
}

CRMDSMessageCreator* CRMDSConnector::GetVolumeMessageCreator(CMICEXSection* pSection)
{
	CString csKey; 
	csKey.Format("%s-%s", pSection->m_pConfig->m_csName, "Volume");

	MAP_MESSAGE_CREATORS::const_iterator itMC = m_mapMessageCreators.find(csKey);
	if (itMC != m_mapMessageCreators.end())
		return itMC->second;

	CRMDSMessageCreator* pMessageCreator = CRMDSMessageCreator::Create(
							pSection->m_pConfig->m_pVolume);

	m_mapMessageCreators[csKey] = pMessageCreator;

	return pMessageCreator;
}

CRMDSMessageCreator* CRMDSConnector::GetTableMessageCreator(CMICEXSection* pSection, CMICEXTable* pTable)
{
	CString csKey; 
	csKey.Format("%s-%s", pSection->m_pConfig->m_csName, pTable->m_csTableName);

	MAP_MESSAGE_CREATORS::const_iterator itMC = m_mapMessageCreators.find(csKey);
	if (itMC != m_mapMessageCreators.end())
		return itMC->second;

	CRMDSMessageCreator* pMessageCreator = CRMDSMessageCreator::Create(pTable->m_pConfig);

	m_mapMessageCreators[csKey] = pMessageCreator;

	return pMessageCreator;
}

	
void CRMDSConnector::PublishAlive(CMICEXChangesInfo* pChangesInfo)
{
	CRMDSMessageCreator* pMessageCreator = GetAliveMessageCreator(pChangesInfo->m_pSection);

	if (pMessageCreator == NULL)
		return;

	Publish(pMessageCreator, pChangesInfo, "ALIVE", FALSE);
}

void CRMDSConnector::PublishVolume(CMICEXChangesInfo* pChangesInfo)
{
	CRMDSMessageCreator* pMessageCreator = GetVolumeMessageCreator(pChangesInfo->m_pSection);

	if (pMessageCreator == NULL)
		return;

	Publish(pMessageCreator, pChangesInfo, "VOLUME");
}

void CRMDSConnector::PublishTable(CMICEXChangesInfo* pChangesInfo)
{
	CMICEXSection*	pSection	= pChangesInfo->m_pSection;
	CMICEXTable*	pTable		= pChangesInfo->m_pTable;

	if (pTable->m_pConfig->IsPublishToRMDS())
	{
		CRMDSMessageCreator* pMessageCreator = GetTableMessageCreator(pSection, pTable);

		if (pMessageCreator == NULL)
			return;

		if (pChangesInfo->m_bIsClear || pChangesInfo->m_bIsInit)
			pMessageCreator->Clear();

		PublishClear(pMessageCreator, pChangesInfo, pTable->m_csTableName);

		if (!pChangesInfo->m_bIsInit && !pChangesInfo->m_bIsClear)
		{
			Publish(pMessageCreator, pChangesInfo, pTable->m_csTableName);
		}
	}
}

	

BOOL CRMDSConnector::PublishClear(CRMDSMessageCreator* pMessageCreator, CMICEXChangesInfo* pChangesInfo, CString lpstrTable, BOOL IsLogRecord)
{
	if (m_iSSLSessionID >= 0		&& pMessageCreator->m_pConfig->m_bRMDS)
	{
		while(pMessageCreator->ClearFileds(pChangesInfo))
		{
			PUPDATERECORD pClearRecord = pMessageCreator->GetRMDSClearMessage();

			CString csLog = "cleared";
			PublishRMDSRecord(pClearRecord, lpstrTable, csLog, IsLogRecord);
		}
	}

	return TRUE;
}

BOOL CRMDSConnector::Publish(CRMDSMessageCreator* pMessageCreator, CMICEXChangesInfo* pChangesInfo, CString lpstrTable, BOOL IsLogRecord)
{
	CString csLog = "";

	BOOL IsIgnore = pMessageCreator->UpdateFileds(pChangesInfo, csLog);
	
	if (m_iSSLSessionID >= 0		&& pMessageCreator->m_pConfig->m_bRMDS)
	{
		PUPDATERECORD pRMDSMessage = pMessageCreator->GetRMDSMessage();

		if (!IsIgnore)
		{
			PublishRMDSRecord(pRMDSMessage, lpstrTable, csLog, IsLogRecord);
		}
		else
		{
			LOG_INFO(SECTION_BY_NAME((LPCSTR)lpstrTable),
				"[%s.%s] IGNORED %s", (LPCSTR)pRMDSMessage->szItemSource, (LPCSTR)pRMDSMessage->szItemName, (LPCSTR)csLog);	
		}
	}

	if ((m_iTIBRVSessionID >= 0		&& pMessageCreator->m_pConfig->m_bTIBRV) || 
		(m_iTIBRVCertSessionID >= 0	&& pMessageCreator->m_pConfig->m_bTIBRVCert))
	{
		PTIBRVRECORD pTIBRVMessage = pMessageCreator->GetTIBRVMessage();

		if (!IsIgnore)
		{
			if (m_iTIBRVSessionID >= 0		&& pMessageCreator->m_pConfig->m_bTIBRV)
				PublishTIBRVRecord(m_iTIBRVSessionID, pTIBRVMessage, lpstrTable, csLog, IsLogRecord);

			if (m_iTIBRVCertSessionID >= 0	&& pMessageCreator->m_pConfig->m_bTIBRVCert)
			{
				// FIX-752 begin
				// find anticipated listener for this message subject
				// If pair not present in storage,  register pair anticipated listener - message subject
				if(TIBRV_RegisterCmListener != NULL)
				{
					const CmListeners &cmListeners = CFEEDConfig::s_pConfigInstance->m_pTIBRVCertConfig->m_cmListeners;
					if(cmListeners.size() > 0)
					{
						const CString strSubject(pTIBRVMessage->pSubjectName);
						if(m_Subjects.find(strSubject) == m_Subjects.end())
						{
							// register subject
							m_Subjects.insert(strSubject);

							CmListeners::const_iterator cit = NULL;
							for(cit = cmListeners.begin(); cit != cmListeners.end(); ++cit)
							{
								const CString strCmName(*cit);
								if(TIBRV_RegisterCmListener(m_iTIBRVCertSessionID, (LPCSTR)strCmName, (LPCSTR)strSubject) == 1)
									LOG_INFO(SECTION(lSystem), "Anticipated listener '%s' for message with subject '%s' was registered",
										(LPCSTR)strCmName, (LPCSTR)strSubject);
								else
									LOG_ERROR(SECTION(lSystem), "Anticipated listener '%s' for message with subject '%s' was not registered",
										(LPCSTR)strCmName, (LPCSTR)strSubject);
							}
						}
					}
				}
				// FIX-752 end
				PublishTIBRVRecord(m_iTIBRVCertSessionID, pTIBRVMessage, lpstrTable, csLog, IsLogRecord);
			}
		}
		else
		{
			LOG_INFO(SECTION_BY_NAME((LPCSTR)lpstrTable),
				"[%s] IGNORED %s", 
				(LPCSTR)pTIBRVMessage->pSubjectName, (LPCSTR)csLog);
		}
	}

	return TRUE;
}

BOOL CRMDSConnector::PublishRMDSRecord(PUPDATERECORD pRecord, CString lpstrTable, CString& csLog, BOOL IsLogRecord)
{
	if (SSL_PublishSSL(m_iSSLSessionID, pRecord) >= 0)
	{
		if (!IsLogRecord)
			return TRUE;
		LOG_INFO(SECTION_BY_NAME((LPCSTR)lpstrTable),
			"[%s.%s] %s", (LPCSTR)pRecord->szItemSource, (LPCSTR)pRecord->szItemName, (LPCSTR)csLog);	

		return TRUE;
	}
	else
	{
		LOG_ERROR(SECTION(lSystem), "Unable to publish [%s.%s] %s.",
			(LPCSTR)pRecord->szItemSource, (LPCSTR)pRecord->szItemName, (LPCSTR)csLog);

		return FALSE;
	}
}

BOOL CRMDSConnector::PublishTIBRVRecord(int iSession, PTIBRVRECORD pRecord, CString lpstrTable, CString& csLog, BOOL IsLogRecord)
{
	if (TIBRV_MsgPublish(iSession, pRecord, FALSE, 0))
	{
		if (!IsLogRecord)
			return TRUE;
		LOG_INFO(SECTION_BY_NAME((LPCSTR)lpstrTable),
			"[%s] %s", 
			(LPCSTR)pRecord->pSubjectName, (LPCSTR)csLog);	

		return TRUE;
	}
	else
	{
		LOG_ERROR(SECTION(lSystem), "Unable to publish [%s] %s.",
			(LPCSTR)pRecord->pSubjectName, (LPCSTR)csLog);

		return FALSE;
	}
}

	
