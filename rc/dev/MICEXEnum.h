#pragma once

class CMICEXEnum 
{
public:
	CMICEXEnum();
	~CMICEXEnum();

public:
	CString m_csEnumName;
	CString m_csEnumDescr;
	int32	m_lEnumSize;
	int32	m_lEnumKind;
/*
	ekCheck = 0		
	ekGroup = 1		
	ekCombo = 2
*/
	int32	m_lConstants;
	CStringList m_cslConstants;
};

typedef vector<CMICEXEnum*>			VEC_MICEX_ENUMS;
typedef map<CString, CMICEXEnum*>	MAP_MICEX_ENUMS;