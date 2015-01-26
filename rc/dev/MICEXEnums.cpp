#include "stdafx.h"
#include ".\micexenums.h"

CMICEXEnums::CMICEXEnums()
{
}

CMICEXEnums::~CMICEXEnums()
{
	Clear();
}

void CMICEXEnums::Clear()
{
	for (VEC_MICEX_ENUMS::iterator it = m_vecMICEXEnums.begin(); it != m_vecMICEXEnums.end(); it++)
		delete (*it);

	m_vecMICEXEnums.clear();
	m_mapMICEXEnums.clear();
}

void CMICEXEnums::AddEnum(CMICEXEnum* pMICEXEnum)
{
	m_vecMICEXEnums.push_back(pMICEXEnum);
	m_mapMICEXEnums[pMICEXEnum->m_csEnumName] = pMICEXEnum;
}

CMICEXEnum* CMICEXEnums::GetEnum(int nEnumID)
{
	return m_vecMICEXEnums.at(nEnumID);
}

CMICEXEnum* CMICEXEnums::GetEnum(CString csName)
{
	MAP_MICEX_ENUMS::const_iterator itEnum = m_mapMICEXEnums.find(csName);

	if (itEnum != m_mapMICEXEnums.end())
		return itEnum->second;

	return NULL;
}
