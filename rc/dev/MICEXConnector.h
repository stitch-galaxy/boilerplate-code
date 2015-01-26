#pragma once

#include "FEEDConfig.h"
#include "FEEDThread.h"
#include "MICEXSchema.h"

class CMICEXSection;

class CMICEXConnector : 
	public CFEEDThread
{
public:
	CMICEXConnector(CMICEXSection* pSection);
	~CMICEXConnector();

public:
	BOOL RunBegin();
	void RunStep();
	void RunEnd();

protected:
	BOOL IsLogonTime();

	void CheckConnection();
	void UpdateState(int iState);

	CString GetConnectionString();

	BOOL Connect();
	void Disconnect();
	BOOL ReConnect();

	BOOL ParseSchema();

	BOOL OpenTables();
	void CloseTables();
	BOOL ProcessTables();

private:
	int32 m_Idx;

protected:
	int							m_iConnection;

private:
	CFEEDConfigSection*			m_pConfig;

	CMICEXSection*				m_pSection;
	CMICEXSchema*				m_pSchema;
	
protected:
	BOOL						m_bConnected;
	BOOL						m_bWaitingForConnection;
	
	time_t						m_timeLastCheck;
	time_t						m_timeLastStateUpdate;
	int							m_iLastState;
};
