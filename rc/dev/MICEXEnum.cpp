#include "stdafx.h"
#include ".\micexenum.h"

CMICEXEnum::CMICEXEnum()
{
	m_csEnumName = "";
	m_csEnumDescr = "";
	m_lEnumSize = NULL;
	m_lEnumKind = NULL;
	m_lConstants = NULL;
}


CMICEXEnum::~CMICEXEnum()
{
	if(!m_cslConstants.IsEmpty())
		m_cslConstants.RemoveAll();
}
