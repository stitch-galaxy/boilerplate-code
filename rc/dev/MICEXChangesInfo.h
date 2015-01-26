#pragma once

class CMICEXSection;
class CMICEXSchema;
class CMICEXTable;

enum EnumChangesType
{
	changesTable,
	changesVolume,
	changesAlive
};

typedef map<CString, int>	MAPCHANGEDFIELDS;

class CMICEXChangesInfo 
{
public:	
	CMICEXChangesInfo(CString csStartTime, CMICEXTable* pTable, LPBYTE pRowData, BOOL bIsExistRow)
	{
		m_eChangesType	= changesTable;

		m_csStartTime	= csStartTime;

		m_pSection		= NULL;

		m_pTable		= pTable;
		m_pRowData		= pRowData;

		m_bIsExistRow	= bIsExistRow;

		m_bIsPriority	= FALSE;
		m_bIsInit		= FALSE;
		m_bIsClear		= FALSE;

		m_mapChangedFields.clear();
	}

	CMICEXChangesInfo(CString csStartTime, CMICEXSection* pSection, double dVolume)
	{
		m_eChangesType	= changesVolume;

		m_csStartTime	= csStartTime;
		m_pSection		= pSection;
		m_dVolume		= dVolume;

		m_pTable		= NULL;
		m_pRowData		= NULL;
		m_bIsExistRow	= FALSE;

		m_bIsPriority	= FALSE;
	}

	CMICEXChangesInfo(CMICEXSection* pSection, int iAliveState)
	{
		m_eChangesType	= changesAlive;

		m_iAliveState	= iAliveState;

		m_pSection		= pSection;

		m_pTable		= NULL;
		m_pRowData		= NULL;
		m_bIsExistRow	= FALSE;

		m_bIsPriority	= TRUE;
	}

	CMICEXChangesInfo(const CMICEXChangesInfo& mci)
		: m_mapChangedFields(mci.m_mapChangedFields)
	{
		m_eChangesType	= mci.m_eChangesType;
		m_csStartTime	= mci.m_csStartTime;
		m_csDoneTime	= mci.m_csDoneTime;
		m_pSection		= mci.m_pSection;
		m_pTable		= mci.m_pTable;
		m_pRowData		= mci.m_pRowData;
		m_bIsExistRow	= mci.m_bIsExistRow;
		m_bIsPriority	= mci.m_bIsPriority;
		m_bIsInit		= mci.m_bIsInit;
		m_bIsClear		= mci.m_bIsClear;
		m_dVolume		= mci.m_dVolume;
		m_iAliveState	= mci.m_iAliveState;
	}
public:
	EnumChangesType		m_eChangesType;

	CString				m_csStartTime;
	CString				m_csDoneTime;

	CMICEXSection*		m_pSection;

	CMICEXTable*		m_pTable;
	LPBYTE				m_pRowData;

	BOOL				m_bIsExistRow;

	BOOL				m_bIsPriority;
	BOOL				m_bIsInit;
	BOOL				m_bIsClear;

	MAPCHANGEDFIELDS	m_mapChangedFields;

	double				m_dVolume;

	int					m_iAliveState;
};

typedef vector<CMICEXChangesInfo*> VEC_CHANGES_INFO;