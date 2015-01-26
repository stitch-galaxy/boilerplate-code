#include "stdafx.h"
#include ".\micextablefield.h"

CMICEXTableField::CMICEXTableField()
{
	m_csFieldName = "";
	m_csFieldDesc = "";
	
	m_lFieldSize = NULL;
	m_lFieldOffset = NULL;

	m_lFieldType = NULL;
	
	m_lFieldAttr = NULL;

	m_csCountType = "";
	m_csDefValue = "";
}


CMICEXTableField::~CMICEXTableField()
{

}

