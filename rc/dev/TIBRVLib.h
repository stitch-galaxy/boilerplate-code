#pragma once

#include "RT_RDV_Link_Int.h"

class CTIBRVLib
{
public:
	CTIBRVLib();
	virtual ~CTIBRVLib(void);

protected:
	BOOL InitializeTIBRVLibrary();
	BOOL UninitializeTIBRVLibrary();

protected:
	HINSTANCE				m_hTIBRVLinkLIB;

public:
	_PINITTIBRV				TIBRV_Init;
	_PRELEASETIBRV			TIBRV_Release;
	_PINITTIBRVSESSION		TIBRV_InitSession;
	_PRELEASETIBRVSESSION	TIBRV_ReleaseSession;
	_MSGPUBLISHTIBRV		TIBRV_MsgPublish;
	_GETTIBRVSTATUSMESSAGE	TIBRV_GetStatusMessage;
	_PREGISTERTIBRVCMLISTENER TIBRV_RegisterCmListener; // FIX-752
};
