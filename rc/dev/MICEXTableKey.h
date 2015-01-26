#pragma once

class CMICEXTable;

class CMICEXTableKey
{
public:
	CMICEXTableKey();
	CMICEXTableKey(const CMICEXTableKey& compKey);
	CMICEXTableKey(CMICEXTable* pTable, int nFields, BYTE* pFieldBuf, int32 nDataLen, BYTE* pDataBuf);
	~CMICEXTableKey();

public:

	bool operator < (const CMICEXTableKey& compKey) const
	{
		return (memcmp(m_pData, compKey.m_pData, m_iSize) < 0);
	}

	void operator = (const CMICEXTableKey& compKey)
	{
		m_pTable	= compKey.m_pTable;
		m_iSize		= compKey.m_iSize;
		
		if (m_pData != NULL)
			delete [] m_pData;
		m_pData = new BYTE[m_iSize];

		memcpy(m_pData, compKey.m_pData, m_iSize);
	}

	static CMICEXTableKey GetSecTypesKey(CMICEXTable* pTable, LPCSTR lpszTypeID);
	static CMICEXTableKey GetSecuritiesKey(CMICEXTable* pTable, LPCSTR lpszBoard, LPCSTR lpszName);
	static CMICEXTableKey GetTradesKey(CMICEXTable* pTable, LPCSTR lpszTradeNo, LPCSTR lpszBuySell);
	static CMICEXTableKey GetUSTradesKey(CMICEXTable* pTable, LPCSTR lpszTradeNo, LPCSTR lpszBuySell);

public:
	CMICEXTable*	m_pTable;
	
	int32			m_iSize;
	BYTE*			m_pData;
};

typedef map<CMICEXTableKey, int>	TABLEKEYMAPTOPOSITION;
typedef map<CMICEXTableKey, double>	TABLEKEYMAPTOVOLUME;