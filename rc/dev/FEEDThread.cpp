#include "stdafx.h"
#include "FEEDThread.h"

#include "RCITService.h"

#include "FHLogger.h"

#include "DumpStack.h"

CFEEDThread::CFEEDThread(CString csThreadName)
{
	m_csThreadName		= csThreadName;

	m_hThreadHnd		= NULL;

	m_hStop				= ::CreateEvent(0, TRUE, FALSE, 0);
	m_hPause			= ::CreateEvent(0, TRUE, FALSE, 0);
	m_hContinue			= ::CreateEvent(0, TRUE, FALSE, 0);

	m_bIsRunning		= FALSE;
	m_hThreadReady		= ::CreateEvent(0, TRUE, FALSE, 0);
	m_hThreadStoped		= ::CreateEvent(0, TRUE, FALSE, 0);
}

CFEEDThread::~CFEEDThread(void)
{
	CloseHandle(m_hStop);
	CloseHandle(m_hPause);
	CloseHandle(m_hContinue);

	CloseHandle(m_hThreadReady);
	CloseHandle(m_hThreadStoped);
}

	
void CALLBACK OnStackReporting(WORD wEventType, LPCSTR lpszMessage)
{
	switch(wEventType)
	{
	case EVENTLOG_ERROR_TYPE:
		LOG_ERROR(SECTION(lSystem), lpszMessage);
		break;
	case EVENTLOG_WARNING_TYPE:
		LOG_WARNING(SECTION(lSystem), lpszMessage);
		break;
	case EVENTLOG_INFORMATION_TYPE:
	default:
		LOG_INFO(SECTION(lSystem), lpszMessage);
		break;
	}
}


DWORD WINAPI CFEEDThread::ThreadMainStatic(CFEEDThread* lpMain)
{
	return lpMain->ThreadMain();
}

DWORD CFEEDThread::ThreadMain()
{
	LOG_INFO(SECTION(lSystem), "%s thread starting...", m_csThreadName);

	BOOL IsSuccess = TRUE;
	
	RC_TRY
	{
		::CoInitialize(NULL);

		IsSuccess = RunBegin();
	}
	RC_EXCEPT_EXT("", OnStackReporting)
	{
		IsSuccess = FALSE;
	}

	if (!IsSuccess)
	{
		LOG_FATAL(SECTION(lSystem), "Unable to start %s thread", m_csThreadName);

		::SetEvent(m_hThreadStoped);

		return FALSE;
	}

	LOG_INFO(SECTION(lSystem), "%s thread started.", m_csThreadName);

	m_bIsRunning = TRUE;
	::SetEvent(m_hThreadReady);

	while (::WaitForSingleObject(m_hStop, 1) != WAIT_OBJECT_0) 
	{
		RC_TRY
		{
			RunStep();
		}
		RC_EXCEPT_THROW("", OnStackReporting)

		if(::WaitForSingleObject(m_hPause, 0) == WAIT_OBJECT_0)
		{
			while(::WaitForSingleObject(m_hContinue, 50) != WAIT_OBJECT_0)
			{
				if(::WaitForSingleObject(m_hStop, 50) == WAIT_OBJECT_0)
					break;
			}

			::ResetEvent(m_hPause);
			::ResetEvent(m_hContinue);
		}

		if(::WaitForSingleObject(m_hStop, 0) == WAIT_OBJECT_0)
			break;
	}

	m_bIsRunning = FALSE;::SetEvent(m_hThreadStoped);

	LOG_INFO(SECTION(lSystem), "%s thread stopping...", m_csThreadName);

	RC_TRY
	{
		RunEnd();

		::CoUninitialize();
	}
	RC_EXCEPT_THROW("", OnStackReporting)

	LOG_INFO(SECTION(lSystem), "%s thread stopped.", m_csThreadName);

	return TRUE;
}

void CFEEDThread::Start()
{
	m_hThreadHnd = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)CFEEDThread::ThreadMainStatic, (LPVOID)this, 0, &m_iThreadID);
}

void CFEEDThread::Stop()
{
	if (m_hThreadHnd)
	{
		if (m_hStop)
		{
			SetEvent(m_hStop);

			if (WaitForSingleObject(m_hThreadHnd, 30000) != WAIT_OBJECT_0) 
				TerminateThread(m_hThreadHnd, NULL); 
		}

		CloseHandle(m_hThreadHnd);
		m_hThreadHnd = NULL;
	}
}

void CFEEDThread::Pause()
{
	if(m_hPause)
		::SetEvent(m_hPause);
}

void CFEEDThread::Continue()
{
	if(m_hContinue)
		::SetEvent(m_hContinue);
}

void CFEEDThread::WaitFinalyState()
{
	HANDLE pHandler[2];
	pHandler[0] = m_hThreadReady;
	pHandler[1] = m_hThreadStoped;

	::WaitForMultipleObjects(2, pHandler, FALSE, INFINITE);
}

BOOL CFEEDThread::StartAndWait()
{
	Start();
	WaitFinalyState();
	return m_bIsRunning;
}
	
void CFEEDThread::StopProcess()
{
	SetEvent(CRCITService::s_pServiceInstance->m_hStop);
}