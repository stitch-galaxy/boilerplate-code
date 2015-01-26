#include "stdafx.h"
#include "RMDSLib.h"

#include "FHLogger.h"
#include "DllUtils.h"

CRMDSLib::CRMDSLib()
{
	m_hMarketLinkLIB = NULL;

	SSL_InitSSL = NULL;
	SSL_InitSSL_Source = NULL;
	SSL_FreeSSL = NULL;
	SSL_GetSSLStatusMessage = NULL;
	SSL_PublishSSL = NULL;
	SSL_SetSRCReady = NULL;
	SSL_MapNumFromShortByFName = NULL;
	SSL_MapSHORTFromNUMByNAME = NULL;
	SSL_ItemSubscribeSSL = NULL;
	SSL_ItemUnsubscribeSSL = NULL;
	SSL_ItemUnsubscribeALLSSL = NULL;
}

CRMDSLib::~CRMDSLib(void)
{
}

BOOL CRMDSLib::InitializeRMDSLibrary()
{
	const string sDllName("RT_SSL_Link.dll");
	try
	{
		m_hMarketLinkLIB = LoadLibrary(sDllName.c_str());
		if(m_hMarketLinkLIB == NULL) 
			throw DllException("Unable to load '%s'", sDllName.c_str());

		SSL_InitSSL = GetProcAddressEx<_PINITSSL>(m_hMarketLinkLIB, sDllName, "InitSSL"); 
		SSL_InitSSL_Source = GetProcAddressEx<_PINITSSL_SOURCE>(m_hMarketLinkLIB, sDllName, "InitSSL_Source"); 
		SSL_FreeSSL = GetProcAddressEx<_PFREESSL>(m_hMarketLinkLIB, sDllName, "FreeSSL"); 
		SSL_GetSSLStatusMessage = GetProcAddressEx<_PGETSSLSTATUSMESSAGE>(m_hMarketLinkLIB, sDllName, "GetSSLStatusMessage"); 
		SSL_PublishSSL = GetProcAddressEx<_PITEMPUBLISHSSL>(m_hMarketLinkLIB, sDllName, "ItemPublishSSL"); 
		SSL_SetSRCReady = GetProcAddressEx<_PSETSSL_SOURCE_READY>(m_hMarketLinkLIB, sDllName, "SetSSL_Source_Ready");
		SSL_MapNumFromShortByFName = GetProcAddressEx<_PMAPNUMFROMSHORTBYNAME>(m_hMarketLinkLIB, sDllName, "MapNUMFromSHORTByNAME"); 
		SSL_MapSHORTFromNUMByNAME = GetProcAddressEx<_PMAPSHORTFROMNUMBYNAME>(m_hMarketLinkLIB, sDllName, "MapSHORTFromNUMByNAME"); 
		SSL_ItemSubscribeSSL = GetProcAddressEx<_PITEMSUBSCRIBESSL>(m_hMarketLinkLIB, sDllName, "ItemSubscribeSSL"); 
		SSL_ItemUnsubscribeSSL = GetProcAddressEx<_PITEMUNSUBSCRIBESSL>(m_hMarketLinkLIB, sDllName, "ItemUnsubscribeSSL"); 
		SSL_ItemUnsubscribeALLSSL = GetProcAddressEx<_PITEMUNSUBSCRIBEALLSSL>(m_hMarketLinkLIB, sDllName, "ItemUnsubscribeALLSSL"); 
	}
	catch(DllException &ex)
	{
		LOG_FATAL(SECTION(lSystem), (char*)ex.what());
		return FALSE;
	}
	return TRUE;
}

BOOL CRMDSLib::UninitializeRMDSLibrary()
{
	if(m_hMarketLinkLIB != NULL) 
	{
		FreeLibrary(m_hMarketLinkLIB);
		m_hMarketLinkLIB = NULL;
		LOG_INFO(SECTION(lSystem), "Sink SSL Session Stopped");
	}

	m_hMarketLinkLIB = NULL;

	SSL_InitSSL = NULL;
	SSL_InitSSL_Source = NULL;
	SSL_FreeSSL = NULL;
	SSL_GetSSLStatusMessage = NULL;
	SSL_PublishSSL = NULL;
	SSL_SetSRCReady = NULL;
	SSL_MapNumFromShortByFName = NULL;
	SSL_MapSHORTFromNUMByNAME = NULL;
	SSL_ItemSubscribeSSL = NULL;
	SSL_ItemUnsubscribeSSL = NULL;
	SSL_ItemUnsubscribeALLSSL = NULL;

	return TRUE;
}
