#include "stdafx.h"
#include "MICEXTableKey.h"

#include "MICEXTable.h"

CMICEXTableKey::CMICEXTableKey()
{
    m_pData = NULL;
}

CMICEXTableKey::CMICEXTableKey(const CMICEXTableKey& compKey)
{
	m_pData = NULL;

	operator=(compKey);
}

CMICEXTableKey::CMICEXTableKey(CMICEXTable* pTable, int nFields, BYTE* pFieldBuf, int32 nDataLen, BYTE* pDataBuf)
{
	m_pTable	= pTable;
	m_iSize		= pTable->m_nKeySize;
	m_pData		= new BYTE[m_iSize];

	memset(m_pData, 0, m_iSize);
	
	map<int32, int32> mapFieldsNum;
	map<int32, int32> mapFieldsOffset;

	int32 offset = 0;
	int32 nRealFields = (nFields != 0) ? nFields : pTable->m_nOutputFields;
	
	for (int nField = 0; nField < nRealFields; nField++)
	{
		int32 nFieldNum = (pFieldBuf != NULL) ? pFieldBuf[nField] : nField;

		mapFieldsNum[nFieldNum]		= nField;
		mapFieldsOffset[nFieldNum]	= offset;

		offset += m_pTable->m_pOutputFields[nFieldNum].m_lFieldSize;
	}
	
	offset = 0;
	for(UINT nKeyField = 0; nKeyField < m_pTable->m_vKeyFields.size(); nKeyField++)
	{
		int32 size = m_pTable->m_pOutputFields[m_pTable->m_vKeyFields[nKeyField]].m_lFieldSize;
		BYTE* pVal = (pDataBuf + mapFieldsOffset[m_pTable->m_vKeyFields[nKeyField]]);

		BYTE* pValZeroTerm = new BYTE[size + 1];
		memset(pValZeroTerm, 0, size + 1);
		memcpy(pValZeroTerm, pVal, size);

		BYTE* pStrVal = new BYTE[size + 1];
		memset(pStrVal, 0, size + 1);
		strcpy((char*)pStrVal, (char*)pValZeroTerm);
		
		CString cs = (char*)pStrVal;
		cs.Trim();
		
		memset(pStrVal, 0, size + 1);
		strcpy((char*)pStrVal, cs);

		memcpy((m_pData + offset), pStrVal, size);

		offset += m_pTable->m_pOutputFields[m_pTable->m_vKeyFields[nKeyField]].m_lFieldSize;

		delete [] pValZeroTerm;
		delete [] pStrVal;
	}
}

CMICEXTableKey::~CMICEXTableKey()
{
	delete [] m_pData;
}

CMICEXTableKey CMICEXTableKey::GetSecTypesKey(CMICEXTable* pTable, LPCSTR lpszTypeID)
{
	int32 sizeTypeID= 6;
	int32 keySize	= sizeTypeID;

	BYTE* pTypeIDZeroTerm = new BYTE[sizeTypeID + 1];
	memset(pTypeIDZeroTerm, 0, sizeTypeID + 1);
	strcpy((char*)pTypeIDZeroTerm, lpszTypeID);

	CMICEXTableKey keySec;

	keySec.m_pTable		= pTable;
	keySec.m_iSize		= keySize;
	keySec.m_pData		= new BYTE[keySize];

	memset(keySec.m_pData, 0, keySize);

	memcpy(keySec.m_pData, pTypeIDZeroTerm, sizeTypeID);

	delete [] pTypeIDZeroTerm;

	return keySec;
}
	
CMICEXTableKey CMICEXTableKey::GetSecuritiesKey(CMICEXTable* pTable, LPCSTR lpszBoard, LPCSTR lpszName)
{
	int32 sizeBoard	= 4;
	int32 sizeName	= 12;
	int32 keySize	= sizeBoard + sizeName;

	BYTE* pBoardZeroTerm = new BYTE[sizeBoard + 1];
	memset(pBoardZeroTerm, 0, sizeBoard + 1);
	strcpy((char*)pBoardZeroTerm, lpszBoard);

	BYTE* pNameZeroTerm = new BYTE[sizeName + 1];
	memset(pNameZeroTerm, 0, sizeName + 1);
	strcpy((char*)pNameZeroTerm, lpszName);

	CMICEXTableKey keySec;

	keySec.m_pTable		= pTable;
	keySec.m_iSize		= keySize;
	keySec.m_pData		= new BYTE[keySize];

	memset(keySec.m_pData, 0, keySize);

	memcpy(keySec.m_pData,				pBoardZeroTerm,	sizeBoard);
	memcpy(keySec.m_pData + sizeBoard,	pNameZeroTerm,	sizeName);

	delete [] pBoardZeroTerm;
	delete [] pNameZeroTerm;

	return keySec;
}

CMICEXTableKey CMICEXTableKey::GetTradesKey(CMICEXTable* pTable, LPCSTR lpszTradeNo, LPCSTR lpszBuySell)
{
	int32 sizeTradeNo	= 12;
	int32 sizeBuySell	= 1;
	int32 keySize	= sizeTradeNo + sizeBuySell;

	BYTE* pTradeNoZeroTerm = new BYTE[sizeTradeNo + 1];
	memset(pTradeNoZeroTerm, 0, sizeTradeNo + 1);
	strcpy((char*)pTradeNoZeroTerm, lpszTradeNo);

	BYTE* pBuySellZeroTerm = new BYTE[sizeBuySell + 1];
	memset(pBuySellZeroTerm, 0, sizeBuySell + 1);
	strcpy((char*)pBuySellZeroTerm, lpszBuySell);

	CMICEXTableKey keySec;

	keySec.m_pTable		= pTable;
	keySec.m_iSize		= keySize;
	keySec.m_pData		= new BYTE[keySize];

	memset(keySec.m_pData, 0, keySize);

	memcpy(keySec.m_pData,					pTradeNoZeroTerm, sizeTradeNo);
	memcpy(keySec.m_pData + sizeTradeNo,	pBuySellZeroTerm, sizeBuySell);

	delete [] pTradeNoZeroTerm;
	delete [] pBuySellZeroTerm;

	return keySec;
}

CMICEXTableKey CMICEXTableKey::GetUSTradesKey(CMICEXTable* pTable, LPCSTR lpszTradeNo, LPCSTR lpszBuySell)
{
	int32 sizeTradeNo	= 12;
	int32 sizeBuySell	= 1;
	int32 keySize	= sizeTradeNo + sizeBuySell;

	BYTE* pTradeNoZeroTerm = new BYTE[sizeTradeNo + 1];
	memset(pTradeNoZeroTerm, 0, sizeTradeNo + 1);
	strcpy((char*)pTradeNoZeroTerm, lpszTradeNo);

	BYTE* pBuySellZeroTerm = new BYTE[sizeBuySell + 1];
	memset(pBuySellZeroTerm, 0, sizeBuySell + 1);
	strcpy((char*)pBuySellZeroTerm, lpszBuySell);

	CMICEXTableKey keySec;

	keySec.m_pTable		= pTable;
	keySec.m_iSize		= keySize;
	keySec.m_pData		= new BYTE[keySize];

	memset(keySec.m_pData, 0, keySize);

	memcpy(keySec.m_pData,					pTradeNoZeroTerm, sizeTradeNo);
	memcpy(keySec.m_pData + sizeTradeNo,	pBuySellZeroTerm, sizeBuySell);

	delete [] pTradeNoZeroTerm;
	delete [] pBuySellZeroTerm;

	return keySec;
}