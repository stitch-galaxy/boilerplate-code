#pragma once

#include ".\MICEXTableKey.h"
#include ".\MICEXVolumeCounter.h"

#include <windows.h>
#include "NTService.h"

#include "MICEXDB.h"
#include "RMDSConnector.h"

#include "MICEXSection.h"

BOOL StartRPCListen();
void StopRPCListen(void);

class CRCITService : public CNTService 
{
public:
	CRCITService(LPCTSTR lpServiceFileName, LPCTSTR lpServiceName, LPCTSTR lpDisplayName, LPCTSTR lpProfile);
	~CRCITService();

public:	

	CString m_csProfile;
	
	HANDLE	m_hStop;
	HANDLE	m_hPause;
	HANDLE	m_hContinue;
	
public:
	static CRCITService*	s_pServiceInstance;
	
public:
	VEC_MICEX_SECTIONS		m_vecSections;
	
public:	// overridables
	virtual void	Run(DWORD, LPTSTR *);
	virtual void	Pause();
	virtual void	Continue();
	virtual void	Shutdown();
	virtual void	Stop();
};