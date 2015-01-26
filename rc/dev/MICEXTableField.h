#pragma once

class CMICEXTableField 
{
public:
	CMICEXTableField();
	~CMICEXTableField();

public:
	CString m_csFieldName;
	CString m_csFieldDesc;

	int32	m_lFieldSize;
	int32	m_lFieldOffset;

	int32	m_lFieldType;

/*
			ftChar    = 0
			ftInteger = 1
			ftFixed   = 2
			ftFloat   = 3
*/	
	int32	m_lFieldAttr;
/*
			ffKey     = 1
			ffSecCode = 2
*/
	CString m_csCountType;
	CString m_csDefValue;
};