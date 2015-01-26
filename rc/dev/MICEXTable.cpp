#include "stdafx.h"
#include "MICEXTable.h"

#include "MICEXSchema.h"

CMICEXTable::CMICEXTable(CMICEXSchema* pSchema, CString csTableName, CFEEDConfigTable* pConfig)
{
	m_pSchema		= pSchema;

	m_csTableName	= csTableName;
	m_csTableDescr	= "";
	m_lAttributes	= NULL;

	m_nInputFields	= 0;
	m_nOutputFields	= 0;

	m_pInputFields	= NULL;
	m_pOutputFields	= NULL;

	m_lRowDataSize	= 0;
	m_nKeySize		= 0;

	m_pConfig		= pConfig;
}

CMICEXTable::~CMICEXTable()
{
	if(m_pInputFields != NULL)
		delete [] m_pInputFields;

	if(m_pOutputFields != NULL) 
		delete [] m_pOutputFields;

	if(!m_mTableKeys.empty())
		m_mTableKeys.clear();

	if(!m_vKeyFields.empty())
		m_vKeyFields.clear();

	if(!m_vTableData.empty()) 
	{
		for(TABLEDATAVECTOR::iterator dataIterator = m_vTableData.begin(); dataIterator != m_vTableData.end(); dataIterator++) 
		{
			if(*dataIterator != NULL) 
				delete [] *dataIterator;
		}

		m_vTableData.clear();
	}
}

	
void CMICEXTable::ClearTableData()
{
	if(!m_mTableKeys.empty()) 
		m_mTableKeys.clear();

	for(TABLEDATAVECTOR::iterator dataIterator = m_vTableData.begin(); dataIterator != m_vTableData.end(); dataIterator++) 
	{
		if(*dataIterator != NULL)
			delete [] *dataIterator;
	}
	m_vTableData.clear();
}

	
CMICEXChangesInfo* CMICEXTable::UpdateTableRow(int nFields, BYTE* pFieldBuf, int32 nDataLen, BYTE* pDataBuf, CString& csTimeIn)
{
	CMICEXTableKey key(this, nFields, pFieldBuf, nDataLen, pDataBuf);

	TABLEKEYMAPTOPOSITION::const_iterator keyIterator = m_mTableKeys.find(key);

	LPBYTE pRowData;
	BOOL fFound, fChanged;

	if (keyIterator != m_mTableKeys.end())
	{
		pRowData = m_vTableData.at(keyIterator->second);

		fFound		= TRUE;
		fChanged	= FALSE;
	}
	else
	{
		BYTE* pEmptyRow = new BYTE[m_lRowDataSize];
		memset(pEmptyRow, ' ', m_lRowDataSize);
		
		m_vTableData.push_back(pEmptyRow);
		m_mTableKeys[key] = m_vTableData.size() - 1;
		
		pRowData = *(m_vTableData.end() - 1);
		
		fFound		= FALSE;
		fChanged	= TRUE;
	}

	CMICEXChangesInfo* pChangesInfo = new CMICEXChangesInfo(csTimeIn, this, pRowData, fFound);

	BYTE* pDataBufPtr = pDataBuf;
	int32 nRealFields = (nFields != 0) ? nFields : m_nOutputFields;
	
	for(int32 nFieldsCnt = 0; nFieldsCnt < nRealFields; nFieldsCnt++) 
	{
		int32 nFieldNum;
		if(pFieldBuf != NULL)
			nFieldNum = pFieldBuf[nFieldsCnt];
		else
			nFieldNum = nFieldsCnt;

		if(nFieldNum < m_nOutputFields) 
		{
			BYTE* pFieldVal = pDataBufPtr;

			int32 nSize;
			BYTE* pOldVal = GetFieldValue(this, nFieldNum, pRowData, &nSize);
			
			BOOL IsDiferent = (memcmp(pOldVal, pFieldVal, nSize) != 0);

			if ((IsDiferent) && 
				(m_csTableName.Compare("SECURITIES") == 0) &&
				((m_pOutputFields[nFieldNum].m_csFieldName.Compare("BID")	== 0) ||
				 (m_pOutputFields[nFieldNum].m_csFieldName.Compare("OFFER")	== 0)))
			{
				BYTE* pOldValZeroTerm = new BYTE[m_pOutputFields[nFieldNum].m_lFieldSize + 1];
				BYTE* pNewValZeroTerm = new BYTE[m_pOutputFields[nFieldNum].m_lFieldSize + 1];

				memset(pOldValZeroTerm, 0, m_pOutputFields[nFieldNum].m_lFieldSize + 1);
				memcpy(pOldValZeroTerm, pOldVal, m_pOutputFields[nFieldNum].m_lFieldSize);

				memset(pNewValZeroTerm, 0, m_pOutputFields[nFieldNum].m_lFieldSize + 1);
				memcpy(pNewValZeroTerm, pFieldVal, m_pOutputFields[nFieldNum].m_lFieldSize);

				float fOldVal = 0;
				float fNewVal = 0;

				sscanf((LPSTR)pOldValZeroTerm, "%f", &fOldVal);
				sscanf((LPSTR)pNewValZeroTerm, "%f", &fNewVal);
				
				if(fNewVal != 0) 
					IsDiferent = (fNewVal != fOldVal);
				else if(fOldVal == 0)
					IsDiferent = (fNewVal != fOldVal);
				else
					IsDiferent = FALSE;

				if (fNewVal == fOldVal) 
					IsDiferent = FALSE;

				delete [] pOldValZeroTerm;
				delete [] pNewValZeroTerm;
			}
	
			if (IsDiferent) 
			{
				SetFieldValue(this, nFieldNum, pRowData, pFieldVal);

				pChangesInfo->m_mapChangedFields[m_pOutputFields[nFieldNum].m_csFieldName] = 0;

				fChanged = TRUE;
			}
	
			pDataBufPtr += m_pOutputFields[nFieldNum].m_lFieldSize;
		}
	}

	
	if (fChanged) 
	{ 
		return pChangesInfo;
	}
	else
	{
		delete pChangesInfo;

		return NULL;
	}
}

	
LPCSTR CMICEXTable::GetEmptyFieldValToString(int nFieldNum, CString& csOut)
{
	switch(m_pOutputFields[nFieldNum].m_lFieldType) 
	{
	case 1: //ftInteger:
		csOut.Format("%ld", 0);
		break;
	case 2: //ftFixed:
		csOut.Format("%.2f", 0);
		break;
	case 3: //ftFloat: 
		csOut.Format("%.2f", 0);
		break;
	default:
		//case ftChar:
		if((m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("TIME") == 0) ||
			(m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("TRADETIME") == 0)) 
		{
			csOut = "00:00:00";
		}
		else 
		{
			csOut = "";
		}
		break;
	}

	return (LPCSTR)csOut;
}

LPCSTR CMICEXTable::MICEXTime2String(LPCSTR lpszMCXTime, CString& csOut)
{
	if(lstrlen((LPCSTR)lpszMCXTime) == 6) 
	{
		csOut = (char)*lpszMCXTime;
		csOut = csOut + (char)*(lpszMCXTime + 1);
		csOut = csOut + ":";
		csOut = csOut + (char)*(lpszMCXTime + 2);
		csOut = csOut + (char)*(lpszMCXTime + 3);
		csOut = csOut + ":";
		csOut = csOut + (char)*(lpszMCXTime + 4);
		csOut = csOut + (char)*(lpszMCXTime + 5);
	}

	return (LPCSTR)csOut;
}

LPCSTR CMICEXTable::FieldValToString(int nFieldNum, BYTE* pDataBuf, CString& csOut)
{
	if (nFieldNum < 0)
	{
		csOut = "";
		return (LPCSTR)csOut;
	}

	if (pDataBuf == NULL)
		return GetEmptyFieldValToString(nFieldNum, csOut);

	BYTE* pDataBufPtr = pDataBuf;
	BYTE* pFieldVal = NULL;
	CString csConv;
	int32 nSize;
	int nDecs;
	CString csDecimals = "";
	CString csSecCode = "";
	CString csBoardID = "";
	CString csFloatFormat = "%.";

	BYTE* pTestFieldVal = GetFieldValue(this, nFieldNum, pDataBuf, &nSize);

	pFieldVal = new BYTE[nSize + 1];
	memset(pFieldVal, 0, nSize + 1);
	memcpy(pFieldVal, pTestFieldVal, nSize);

	switch(m_pOutputFields[nFieldNum].m_lFieldType) 
	{
	case 1: //ftInteger:
		{
			csOut.Format("%lu", atol((LPSTR)pFieldVal));
			break;
		}
	case 2: //ftFixed:
		{
			csConv = (LPSTR)pFieldVal;
			csConv = csConv.Left(csConv.GetLength() - 2);
			csConv += ".";
			csConv += (LPSTR)&(pFieldVal[lstrlen((LPSTR)pFieldVal) - 2]);
			csOut.Format("%.2f", atof((LPCSTR)csConv));
			break;
		}
	case 3: 
		{
			//ftFloat: 
			if(m_csTableName.CompareNoCase("SECURITIES") == 0) 
			{
				FieldValToString(GetFieldNumByName(FIELD_SECURITIES_DECIMALS), pDataBuf, csDecimals);
			}
			else 
			{
				int nSECCode = GetFieldNumByName(FIELD_SECCODE);
				int nBoard = GetFieldNumByName(FIELD_SECBOARD);

				if(nSECCode != -1 && nBoard != -1)
				{
					FieldValToString(nSECCode, pDataBuf, csSecCode);
					FieldValToString(nBoard, pDataBuf, csBoardID);

					if((csSecCode != "") && (csBoardID != ""))
						csDecimals = m_pSchema->GetSECURITIESField(csBoardID, csSecCode, FIELD_SECURITIES_DECIMALS);
				}
			}
			csDecimals.TrimLeft();
			csDecimals.TrimRight();
			nDecs = atoi((LPCSTR)csDecimals);

			if(nDecs > 6)
				TRACE("!!!\n");

			csFloatFormat += (LPCSTR)csDecimals;
			csFloatFormat += "f";
		
			csConv = (LPSTR)pFieldVal;
			csConv = csConv.Left(csConv.GetLength() - nDecs);
			csConv += ".";
			csConv += (LPSTR)&(pFieldVal[lstrlen((LPSTR)pFieldVal) - nDecs]);
			csOut.Format((LPCSTR)csFloatFormat, atof((LPCSTR)csConv));
		}
		break;
	default:
		{
			//case ftChar:
			if((m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("TIME") == 0) ||
				(m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("TRADETIME") == 0) ||
				(m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("ORDERTIME") == 0) ||
				(m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("QUOTETIME") == 0) ||
				(m_pOutputFields[nFieldNum].m_csFieldName.CompareNoCase("DEALTIME") == 0)) 
			{
				MICEXTime2String((LPCSTR)pFieldVal, csOut);
			}
			else 
			{
				csOut.Format("%s", (LPSTR)pFieldVal);
			}
			break;
		}
	}

	delete [] pFieldVal;

	csOut.TrimLeft();
	csOut.TrimRight();

	if(csOut.GetLength() > 40)
		TRACE("!!!\n");		

	return (LPCSTR)csOut;
}

int CMICEXTable::GetFieldNumByName(LPCSTR lpszFieldName)
{
	for(int nCnt = 0; nCnt < m_nOutputFields; nCnt++) {
		if(lstrcmp(m_pOutputFields[nCnt].m_csFieldName, lpszFieldName) == 0) {
			return nCnt;
		}
	}
	return -1;
}


