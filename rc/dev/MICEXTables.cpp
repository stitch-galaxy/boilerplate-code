#include "stdafx.h"
#include "MICEXTables.h"

CMICEXTables::CMICEXTables()
{
}

CMICEXTables::~CMICEXTables()
{
	Clear();
}

void CMICEXTables::Clear()
{
	for (VEC_MICEX_TABLES::iterator it = m_vecMICEXTables.begin(); it != m_vecMICEXTables.end(); it++)
		delete (*it);

	m_vecMICEXTables.clear();
	m_mapMICEXTables.clear();
}

void CMICEXTables::AddTable(CMICEXTable* pMICEXTable)
{
	m_vecMICEXTables.push_back(pMICEXTable);
	m_mapMICEXTables[pMICEXTable->m_csTableName] = pMICEXTable;
}

CMICEXTable* CMICEXTables::GetTable(int nTableID)
{
	return m_vecMICEXTables.at(nTableID);
}

CMICEXTable* CMICEXTables::GetTable(CString csName)
{
	MAP_MICEX_TABLES::const_iterator itTable = m_mapMICEXTables.find(csName);

	if (itTable != m_mapMICEXTables.end())
		return itTable->second;

	return NULL;
}
