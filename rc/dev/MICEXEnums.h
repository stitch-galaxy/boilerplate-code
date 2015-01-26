#pragma once

#include "MICEXEnum.h"

class CMICEXEnums 
{
public:
	CMICEXEnums();
	~CMICEXEnums();

public:
	void Clear();

	void AddEnum(CMICEXEnum* pMICEXEnum);

	CMICEXEnum* GetEnum(int nEnumID);
	CMICEXEnum* GetEnum(CString csName);

public:
	VEC_MICEX_ENUMS	m_vecMICEXEnums;
	MAP_MICEX_ENUMS	m_mapMICEXEnums;
};