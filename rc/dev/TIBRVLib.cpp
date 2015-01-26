#include "stdafx.h"
#include "TIBRVLib.h"

#include "FHLogger.h"
#include "DllUtils.h"

CTIBRVLib::CTIBRVLib()
{
	m_hTIBRVLinkLIB = NULL;

	TIBRV_Init = NULL;
	TIBRV_Release = NULL;
	TIBRV_InitSession = NULL;
	TIBRV_ReleaseSession = NULL;
	TIBRV_MsgPublish = NULL;
	TIBRV_GetStatusMessage = NULL;
	TIBRV_RegisterCmListener = NULL; // FIX-752
}

CTIBRVLib::~CTIBRVLib(void)
{
}


BOOL CTIBRVLib::InitializeTIBRVLibrary()
{
	const string sDllName("RT_RDV_Link.dll");
// FIX-906 begin
	try
	{
		m_hTIBRVLinkLIB = LoadLibrary(sDllName.c_str());
		if(m_hTIBRVLinkLIB == NULL) 
		{
			throw DllException("Unable to load '%s'", sDllName.c_str());
		}
		TIBRV_Init = GetProcAddressEx<_PINITTIBRV>(m_hTIBRVLinkLIB, sDllName, "InitTIBRV");
		TIBRV_Release = GetProcAddressEx<_PRELEASETIBRV>(m_hTIBRVLinkLIB, sDllName, "ReleaseTIBRV"); 
		TIBRV_InitSession = GetProcAddressEx<_PINITTIBRVSESSION>(m_hTIBRVLinkLIB, sDllName, "InitTIBRVSession"); 
		TIBRV_ReleaseSession = GetProcAddressEx<_PRELEASETIBRVSESSION>(m_hTIBRVLinkLIB, sDllName, "ReleaseTIBRVSession"); 
		TIBRV_RegisterCmListener = GetProcAddressEx<_PREGISTERTIBRVCMLISTENER>(m_hTIBRVLinkLIB, sDllName, "RegisterTibrvCmListener"); 
		TIBRV_MsgPublish = GetProcAddressEx<_MSGPUBLISHTIBRV>(m_hTIBRVLinkLIB, sDllName, "MsgPublishTIBRV"); 
		TIBRV_GetStatusMessage = GetProcAddressEx<_GETTIBRVSTATUSMESSAGE>(m_hTIBRVLinkLIB, sDllName, "GetTIBRVStatusMessage");
		
		if (!TIBRV_Init()) 
			throw DllException("Unable to Init TIBRV: %s", TIBRV_GetStatusMessage());
	}
	catch(DllException &ex)
	{
		LOG_FATAL(SECTION(lSystem), (char*)ex.what());
		return FALSE;
	}
// FIX-906 end

	return TRUE;
}

BOOL CTIBRVLib::UninitializeTIBRVLibrary()
{
	TIBRV_Release();

	if(m_hTIBRVLinkLIB != NULL) 
	{
		FreeLibrary(m_hTIBRVLinkLIB);
		m_hTIBRVLinkLIB = NULL;
		LOG_INFO(SECTION(lSystem), "TIBRV Session Stopped");
	}

	m_hTIBRVLinkLIB = NULL;

	TIBRV_Init = NULL;
	TIBRV_Release = NULL;
	TIBRV_InitSession = NULL;
	TIBRV_ReleaseSession = NULL;
	TIBRV_MsgPublish = NULL;
	TIBRV_GetStatusMessage = NULL;

	return TRUE;
}
