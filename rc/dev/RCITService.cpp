#include "stdafx.h"
#include "MCXControlStructures.h"
#include "RCITService.h"
#include "RC_MICEX_Feed.h"

#include "FEEDConfig.h"
#include "FHLogger.h"
#include "DllUtils.h"

#include "MICEXConnector.h"
#include "MICEXDB.h"
#include "MICEXSection.h"
#include "RMDSConnector.h"
#include "TibrvConnector.h"

#include <ole2.h>
#include <stdio.h>
#include <conio.h>

CRCITService* CRCITService::s_pServiceInstance = NULL;

CString MakeTimeStamp()
{
	SYSTEMTIME strSysTime;
	GetLocalTime(&strSysTime);

	CString csTimeStamp;
	csTimeStamp.Format("%02d:%02d:%02d.%03d", strSysTime.wHour, strSysTime.wMinute, strSysTime.wSecond, strSysTime.wMilliseconds);
	
	return csTimeStamp;
}

CRCITService::CRCITService(LPCTSTR lpServiceFileName, LPCTSTR lpServiceName, LPCTSTR lpDisplayName, LPCTSTR lpProfile)
	: CNTService(lpServiceFileName, lpServiceName, lpDisplayName)
	, m_hStop(0)
	, m_hPause(0)
	, m_hContinue(0),
	m_csProfile(lpProfile)
{
	m_dwControlsAccepted = 0;
	m_dwControlsAccepted |= SERVICE_ACCEPT_STOP;
	m_dwControlsAccepted |= SERVICE_ACCEPT_PAUSE_CONTINUE;
	m_dwControlsAccepted |= SERVICE_ACCEPT_SHUTDOWN;
	m_dwServiceType |= SERVICE_INTERACTIVE_PROCESS;

	CRCITService::s_pServiceInstance	= this;

	CFEEDConfig::s_pConfigInstance	= NULL;
	CRMDSConnector::s_pRMDSInstance	= NULL;

	m_hStop = ::CreateEvent(0, TRUE, FALSE, 0);
	m_hPause = ::CreateEvent(0, TRUE, FALSE, 0);
	m_hContinue = ::CreateEvent(0, TRUE, FALSE, 0);
}

CRCITService::~CRCITService()
{
	CloseHandle(m_hStop);
	CloseHandle(m_hPause);
	CloseHandle(m_hContinue);
}

void CRCITService::Run(DWORD dwArgc, LPTSTR * ppszArgv) 
{
	ReportStatus(SERVICE_START_PENDING);

    ::CoInitialize(NULL);

	////////////////////////////////////////////////////////////////////////////
	// Initializing
	
	try
	{
		CFEEDConfig::s_pConfigInstance	= new CFEEDConfig();
		CRMDSConnector::s_pRMDSInstance	= new CRMDSConnector();
		
		CTibrvConnector::s_pTibrvInstance = new CTibrvConnector();

		///////////////////////////////////////////////////////////////////////////////////
		// Logging
		ServiceUtils::SetServiceDefaultPath();
		bool bResult = false;
		LOG_INITIALIZE(bResult)
			if (!bResult)
				throw 0;
		CFHLogger::s_pLogInstance->SetFeedName(m_lpServiceName);

		LOG_PRINT_OUT_HEADER(m_lpServiceName, (LPCSTR)m_csProfile); 		

		//////////////////////////////////////////////////////////////////////////////////
		// Configuration
		if (!CFEEDConfig::s_pConfigInstance->Load(m_csProfile))
			throw 0;

		///////////////////////////////////////////////////////////////////////////////////
		// RPC Listener
		StartRPCListen();

		///////////////////////////////////////////////////////////////////////////////////
		// RMDS
		if (!CRMDSConnector::s_pRMDSInstance->StartAndWait())
			throw 0;

		///////////////////////////////////////////////////////////////////////////////////
		// Tibrv
		if (!CTibrvConnector::s_pTibrvInstance->StartAndWait())
			throw 0;

		///////////////////////////////////////////////////////////////////////////////////
		// MICEX sections

		for (VEC_CONFIG_SECTIONS::iterator itSection = CFEEDConfig::s_pConfigInstance->m_vecSections.begin(); itSection != CFEEDConfig::s_pConfigInstance->m_vecSections.end(); itSection++)
		{
			CFEEDConfigSection* pConfigSection = (*itSection);
			if (!pConfigSection->m_bEnabled)
				continue;

			//////////////////////////////////////////////////////////////////////////////
			// MICEX connection

			CMICEXSection* pMICEXSection = new CMICEXSection(pConfigSection);
			
			if (!pMICEXSection->Start())
			{
				delete pMICEXSection;
				throw 0;
			}

			m_vecSections.push_back(pMICEXSection);
		}
	}
	catch(...)
	{
		SetEvent(m_hStop);
	}

	///////////////////////////////////////////////////////////////////////////////////
	// Service status RUN

	ReportStatus(SERVICE_RUNNING);

	///////////////////////////////////////////////////////////////////////////////////
	// Backgroun thread

	if (::WaitForSingleObject(m_hStop, 0) != WAIT_OBJECT_0)
	{
		ReportStatus(SERVICE_RUNNING);

		while (::WaitForSingleObject(m_hStop, 50) != WAIT_OBJECT_0) 
		{
			MSG msg;

			while(PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) 
			{
				if (msg.message == WM_QUIT)
				{
					SetEvent(m_hStop);
					break;
				}

				TranslateMessage(&msg);
				DispatchMessage(&msg);
			}

			if(::WaitForSingleObject(m_hPause, 5) == WAIT_OBJECT_0) 
			{
				///////////////////////////////////////////////////////////////////////////////////
				// Go to PAUSE state
				CRMDSConnector::s_pRMDSInstance->Pause();
				CTibrvConnector::s_pTibrvInstance->Pause();

				for (VEC_MICEX_SECTIONS::iterator itMICEXSection = m_vecSections.begin(); itMICEXSection != m_vecSections.end(); itMICEXSection++)
				{
					(*itMICEXSection)->Pause();
				}

				///////////////////////////////////////////////////////////////////////////////////
				// Service status PAUSE

				ReportStatus(SERVICE_PAUSED);

				while(::WaitForSingleObject(m_hContinue, 50) != WAIT_OBJECT_0) 
				{
					if(::WaitForSingleObject(m_hStop, 50) == WAIT_OBJECT_0)
						break;
				}

				///////////////////////////////////////////////////////////////////////////////////
				// Go to RUN state

				for (VEC_MICEX_SECTIONS::iterator itMICEXSection = m_vecSections.begin(); itMICEXSection != m_vecSections.end(); itMICEXSection++)
				{
					(*itMICEXSection)->Continue();
				}

				CRMDSConnector::s_pRMDSInstance->Continue();
				CTibrvConnector::s_pTibrvInstance->Continue();

				///////////////////////////////////////////////////////////////////////////////////
				// Service status RUN

				ReportStatus(SERVICE_RUNNING);

				::ResetEvent(m_hPause);
				::ResetEvent(m_hContinue);
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////
	// Stoping application

	ReportStatus(SERVICE_STOP_PENDING);

	for (VEC_MICEX_SECTIONS::iterator itMICEXSection = m_vecSections.begin(); itMICEXSection != m_vecSections.end(); itMICEXSection++)
	{
		(*itMICEXSection)->Stop();
	}
	CTibrvConnector::s_pTibrvInstance->Stop();
	CRMDSConnector::s_pRMDSInstance->Stop();

	CFEEDConfig::s_pConfigInstance->Save();

	Sleep(0);

	for (VEC_MICEX_SECTIONS::iterator itMICEXSection = m_vecSections.begin(); itMICEXSection != m_vecSections.end(); itMICEXSection++)
	{
		delete (CMICEXSection*)(*itMICEXSection);
	}
	m_vecSections.clear();
	if (CTibrvConnector::s_pTibrvInstance != NULL){
		delete CTibrvConnector::s_pTibrvInstance;
		CTibrvConnector::s_pTibrvInstance = NULL;
	}
	if (CRMDSConnector::s_pRMDSInstance != NULL)
		delete CRMDSConnector::s_pRMDSInstance;
	if (CFEEDConfig::s_pConfigInstance != NULL)
		delete CFEEDConfig::s_pConfigInstance;

	StopRPCListen();

	::CoUninitialize();

	Sleep(1000);

	///////////////////////////////////////////////////////////////////////////////////
	// Service status STOP

	ReportStatus(SERVICE_STOPPED);
}

	
void CRCITService::Stop() 
{
	ReportStatus(SERVICE_STOP_PENDING, 5000);

	::SetEvent(m_hStop);
}

void CRCITService::Pause() 
{
	ReportStatus(SERVICE_PAUSE_PENDING);

	::SetEvent(m_hPause);

	ReportStatus(SERVICE_PAUSED);
}

void CRCITService::Continue() 
{
	ReportStatus(SERVICE_CONTINUE_PENDING);
	
	::SetEvent(m_hContinue);

	ReportStatus(SERVICE_RUNNING);
}

void CRCITService::Shutdown() 
{
	::SetEvent(m_hStop);
}

const char* CFHLogger::GetLogSection(int section)
{
	const char* chDefLoggerName = "FH";
	switch(section)
	{
	case lNone:
		return chDefLoggerName;
	case lSystem:
		return "FH.SYS";
	case lConfig:
		return "FH.CFG";
	case lSecurity:
		return "FH.MD.SECURITIES";
	case lAlive:
		return "FH.ALIVE";
	case lTrade:
		return "FH.MD.TRADES";
	case lVolume:
		return "FH.MD.VOLUME";
	case lUstrade:
		return "FH.MD.USTRADES";
	case lTibrv:
		return "TIBRV";
	default:
		break;
	}
	return chDefLoggerName;
}
