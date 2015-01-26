#pragma once

#include "MICEXTableKey.h"

class CMICEXTable;

class CMICEXVolumeCounter
{
public:
	CMICEXVolumeCounter();
	~CMICEXVolumeCounter(void);

public:

	BOOL Update(CMICEXTable* pTable, BYTE* pDataBuf);

	double GetVolume()
	{
		return m_dVolume;
	}

	BOOL IsUpdated()
	{
		return m_bUpdated;
	}

	BOOL UpdateComplete()
	{
		BOOL ret = m_bUpdated;
		m_bUpdated = FALSE;
		return ret;
	}
	
protected:
	BOOL				m_bUpdated;

	TABLEKEYMAPTOVOLUME m_mapVolumes;
	double				m_dVolume;
};
