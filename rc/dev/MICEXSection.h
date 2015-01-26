#pragma once

#include <map>

#include "MICEXVolumeCounter.h"

class CMICEXConnector;
class CMICEXSchema;
class CMICEXTable;
class CMICEXChangesInfo;
class CFEEDConfigSection;

class CMICEXDB;
class CRMDSConnector;

class CMICEXSection
{
public:
	CMICEXSection(CFEEDConfigSection* pConfig);
	~CMICEXSection();

public:
	BOOL Start();
	void Stop();
	void Pause();
	void Continue();

public:
	void UpdateSessionState(int iAliveState);

	void UpdateTableStructure(CMICEXTable* pTable);
	void UpdateTableBegin(CMICEXTable* pTable);
	void UpdateRowComplete(CMICEXChangesInfo* pChangesInfo);
	void UpdateTableComplete(CMICEXTable* pTable);

protected:
	void Update(CMICEXChangesInfo* pChangesInfo);

public:
	CFEEDConfigSection*	m_pConfig;

public:
	CMICEXSchema*				m_pSchema;
	CMICEXConnector*			m_pConnector;
	CMICEXDB*					m_pDB;

protected:
	CMICEXVolumeCounter*		m_pVolumeCounter;
};

typedef vector<CMICEXSection*> VEC_MICEX_SECTIONS;

