#pragma once

#include "MICEXTable.h"

class CMICEXTables 
{
public:
	CMICEXTables();
	~CMICEXTables();

public:
	void Clear();

	void AddTable(CMICEXTable* pMICEXTable);

	CMICEXTable* GetTable(int nTableID);
	CMICEXTable* GetTable(CString csName);

public:
	VEC_MICEX_TABLES	m_vecMICEXTables;
	MAP_MICEX_TABLES	m_mapMICEXTables;
};