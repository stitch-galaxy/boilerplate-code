#pragma once

#include "RT_SSL_Link_Int.h"

class CRMDSLib
{
public:
	CRMDSLib();
	virtual ~CRMDSLib(void);

protected:
	BOOL InitializeRMDSLibrary();
	BOOL UninitializeRMDSLibrary();

protected:
	HINSTANCE				m_hMarketLinkLIB;

public:
	_PINITSSL				SSL_InitSSL;
	_PINITSSL_SOURCE		SSL_InitSSL_Source;
	_PFREESSL				SSL_FreeSSL;
	_PGETSSLSTATUSMESSAGE	SSL_GetSSLStatusMessage;
	_PITEMPUBLISHSSL		SSL_PublishSSL;
	_PSETSSL_SOURCE_READY	SSL_SetSRCReady;
	_PMAPNUMFROMSHORTBYNAME	SSL_MapNumFromShortByFName;
	_PMAPSHORTFROMNUMBYNAME	SSL_MapSHORTFromNUMByNAME;
	_PITEMSUBSCRIBESSL		SSL_ItemSubscribeSSL;
	_PITEMUNSUBSCRIBESSL	SSL_ItemUnsubscribeSSL;
	_PITEMUNSUBSCRIBEALLSSL	SSL_ItemUnsubscribeALLSSL;
};
