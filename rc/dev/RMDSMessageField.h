#pragma once

#include <map>
#include <vector>

#include "MICEXSchema.h"
#include "MICEXTable.h"
#include "FEEDConfig.h"

static LPSTR _MONTH_MAP[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

typedef map<CString, CString>	MAP_CACHE_VALUES;

class CRMDSMessageField
{
public:
	CRMDSMessageField(CFEEDConfigField* pConfig, PUPDATERECORD pRMDSRecord, PTIBRVRECORD pTIBRVRecord)
	{
		m_pRMDSRecord	= pRMDSRecord;
		m_pTIBRVRecord	= pTIBRVRecord;

		m_bLog			= pConfig->m_bLog;

		m_csSource		= pConfig->m_csSource;
		
		m_csRMDSFiled	= pConfig->m_csRMDSFiled;
		m_csTIBRVFiled	= pConfig->m_csTIBRVFiled;

		m_csIgnoreValues= pConfig->m_csIgnoreValues;

		m_pRMDSField	= NULL;
		m_pTIBRVField	= NULL;

		if (m_pRMDSRecord != NULL && !m_csRMDSFiled.IsEmpty())
		{
			m_pRMDSField = (PCBFIELD) new BYTE[sizeof(CBFIELD)];
			memset(m_pRMDSField, 0, sizeof(CBFIELD));

			lstrcpy(m_pRMDSField->szFieldName, m_csRMDSFiled);

			m_pRMDSRecord->pFields.push_back(m_pRMDSField);
		}

		if (m_pTIBRVRecord != NULL && !m_csTIBRVFiled.IsEmpty())
		{
			m_pTIBRVField = (PTIBRVFIELD) new BYTE[sizeof(TIBRVFIELD)];
			memset(m_pTIBRVField, 0, sizeof(TIBRVFIELD));

			CString csName = m_csTIBRVFiled;
#ifdef DEBUG
			if (!pConfig->m_csTIBRVDesc.IsEmpty())
				csName += "/" + pConfig->m_csTIBRVDesc;
#endif
			strcpy(m_pTIBRVField->szFieldName, csName);

			m_pTIBRVRecord->vFields.push_back(m_pTIBRVField);
		}
	}

	virtual ~CRMDSMessageField()
	{
		if (m_pRMDSField != NULL)
			delete [] m_pRMDSField;
		if (m_pTIBRVField != NULL)
			delete [] m_pTIBRVField;
	}
	
	static CRMDSMessageField* Create(CFEEDConfigField* pConfig, PUPDATERECORD pRecord, PTIBRVRECORD pTIBRVRecord);
	static void Delete(CRMDSMessageField* pField);


public:
	static void InitCache(CMICEXChangesInfo* pChangesInfo)
	{
		m_pCacheChangesInfo = pChangesInfo;
		m_pCacheRowData		= pChangesInfo->m_pRowData;
		
		m_pCacheSecTable		= NULL;
		m_pCacheSecTypesTable	= NULL;
		m_pCacheUSTable			= NULL;
		m_pCacheSecRowData		= NULL;
		m_pCacheSecTypesRowData	= NULL;
		m_pCacheUSRowData		= NULL;

		m_mapCacheValues.clear();
	}

	static void ClearCache()
	{
		m_pCacheChangesInfo = NULL;
		m_pCacheRowData = NULL;

		m_pCacheSecTable		= NULL;
		m_pCacheSecTypesTable	= NULL;
		m_pCacheUSTable			= NULL;
		m_pCacheSecRowData		= NULL;
		m_pCacheSecTypesRowData	= NULL;
		m_pCacheUSRowData		= NULL;

		m_mapCacheValues.clear();
	}

	
	CString GetValue(CString csName)
	{
		if (m_pCacheRowData == NULL)
			return "";

		MAP_CACHE_VALUES::const_iterator itValue = m_mapCacheValues.find(csName);
		if (itValue != m_mapCacheValues.end())
			return itValue->second;

		CString csTableName = "";
		CString csFiledName = csName;

		int iPos = csName.Find("-");
		if (iPos > 0)
		{
			csTableName = csName.Left(iPos);
			csFiledName = csName.Mid(iPos + 1);
		}

		if (!csTableName.IsEmpty() && m_pCacheChangesInfo->m_pTable->m_csTableName.CompareNoCase(csTableName) != 0)
			return GetValue(csTableName, csFiledName);

		int iFiled = m_pCacheChangesInfo->m_pTable->GetFieldNumByName(csFiledName);

		if (iFiled < 0)
			return "";

		CString csValue;
		m_pCacheChangesInfo->m_pTable->FieldValToString(iFiled, (BYTE*)m_pCacheRowData, csValue);

		m_mapCacheValues[csName] = csValue;

        return csValue;
	}

	CString GetValue(CString csTableName, CString csFiledName)
	{
		if (m_pCacheRowData == NULL)
			return "";

		if (csTableName.CompareNoCase(m_pCacheChangesInfo->m_pTable->m_csTableName) == 0)
			return GetValue(csFiledName);

		CString csKey; csKey.Format("%s-%s", csTableName, csFiledName);
		MAP_CACHE_VALUES::const_iterator itValue = m_mapCacheValues.find(csKey);
		if (itValue != m_mapCacheValues.end())
			return itValue->second;

		CString csValue = "";

		if (csTableName.CompareNoCase(TABLE_FOSECTYPES) == 0)
		{
			CString csSecBoard	= GetValue(FIELD_SECBOARD);
			CString csSecCode	= GetValue(FIELD_SECCODE);

			if (m_pCacheSecRowData == NULL)
				m_pCacheSecRowData = m_pCacheChangesInfo->m_pSection->m_pSchema->GetSECURITIESData(csSecBoard, csSecCode);

			if (m_pCacheSecTable == NULL)
				m_pCacheSecTable = m_pCacheChangesInfo->m_pSection->m_pSchema->GetTable(TABLE_SECURITIES);

			CString csTypeID = m_pCacheSecTable->FieldValToString(m_pCacheSecTable->GetFieldNumByName(FIELD_SECURITIES_FOSECTYPEID), (BYTE*)m_pCacheSecRowData, csValue);

			if (m_pCacheSecTypesRowData == NULL)
				m_pCacheSecTypesRowData = m_pCacheChangesInfo->m_pSection->m_pSchema->GetFOSECTYPESData(csTypeID);

			if (m_pCacheSecTypesTable == NULL)
				m_pCacheSecTypesTable = m_pCacheChangesInfo->m_pSection->m_pSchema->GetTable(TABLE_FOSECTYPES);

			csValue = m_pCacheSecTypesTable->FieldValToString(m_pCacheSecTypesTable->GetFieldNumByName(csFiledName), (BYTE*)m_pCacheSecTypesRowData, csValue);
		}
		if (csTableName.CompareNoCase(TABLE_SECURITIES) == 0)
		{
			CString csSecBoard	= GetValue(FIELD_SECBOARD);
			CString csSecCode	= GetValue(FIELD_SECCODE);

			if (m_pCacheSecRowData == NULL)
				m_pCacheSecRowData = m_pCacheChangesInfo->m_pSection->m_pSchema->GetSECURITIESData(csSecBoard, csSecCode);

			if (m_pCacheSecTable == NULL)
				m_pCacheSecTable = m_pCacheChangesInfo->m_pSection->m_pSchema->GetTable(TABLE_SECURITIES);

			csValue = m_pCacheSecTable->FieldValToString(m_pCacheSecTable->GetFieldNumByName(csFiledName), (BYTE*)m_pCacheSecRowData, csValue);
		}
		else if (csTableName.CompareNoCase(TABLE_USTRADES) == 0)
		{
			CString csTradeNo	= GetValue(FIELD_TRADENO);
			CString csBuySell	= GetValue(FIELD_BUYSELL);

			if (m_pCacheUSRowData == NULL)
				m_pCacheUSRowData = m_pCacheChangesInfo->m_pSection->m_pSchema->GetUSTRADESData(csTradeNo, csBuySell);

			if (m_pCacheUSTable == NULL)
				m_pCacheUSTable = m_pCacheChangesInfo->m_pSection->m_pSchema->GetTable(TABLE_USTRADES);

			csValue = m_pCacheUSTable->FieldValToString(m_pCacheUSTable->GetFieldNumByName(csFiledName), (BYTE*)m_pCacheUSRowData, csValue);
		}

		m_mapCacheValues[csKey] = csValue;

		return csValue;
	}
	
public:
	virtual void Empty()
	{
		UpdateField("");
	}

	virtual void Update() = 0;
	
	BOOL IsIgnoreValue()
	{
		if (m_csIgnoreValues.IsEmpty())
			return FALSE;

		CString strVal = ";";
		strVal += m_csValue;
		strVal += ";";

		if (m_csIgnoreValues.Find(strVal) >= 0)
			return TRUE;

		return FALSE;
	}

protected:

	void UpdateField(CString csValue)
	{
		m_csValue = csValue;

		if (m_pRMDSField != NULL)
			lstrcpy(m_pRMDSField->szFieldValue, (LPCSTR)csValue);

		if (m_pTIBRVField != NULL)
			lstrcpy(m_pTIBRVField->szFieldValue, (LPCSTR)csValue);
	}
	
protected:
	static CMICEXChangesInfo*		m_pCacheChangesInfo;
	static LPVOID					m_pCacheRowData;
	
	static MAP_CACHE_VALUES			m_mapCacheValues;

	static CMICEXTable*				m_pCacheSecTable;
	static CMICEXTable*				m_pCacheSecTypesTable;
	static CMICEXTable*				m_pCacheUSTable;
	static LPVOID					m_pCacheSecRowData;
	static LPVOID					m_pCacheSecTypesRowData;
	static LPVOID					m_pCacheUSRowData;

public:
	PUPDATERECORD			m_pRMDSRecord;
	PTIBRVRECORD			m_pTIBRVRecord;

	PCBFIELD				m_pRMDSField;
	PTIBRVFIELD				m_pTIBRVField;

	CString					m_csValue;

public:
	BOOL		m_bLog;
	
	CString		m_csSource;

	CString		m_csRMDSFiled;
	CString		m_csTIBRVFiled;

	CString		m_csIgnoreValues;
};
	
typedef vector<CRMDSMessageField*> VEC_RMDS_MESSAGE_VIELDS;
	
class CRMDSSourceMessageField : public CRMDSMessageField
{
public:
	CRMDSSourceMessageField(CFEEDConfigField* pConfig, PUPDATERECORD pRecord, PTIBRVRECORD pTIBRVRecord, CString csSource) : 
	  CRMDSMessageField(pConfig, pRecord, pTIBRVRecord)
	{
		m_csSource = csSource;
	}

public:

	virtual void Update()
	{
		UpdateField(GetValue(m_csSource));
	}
	
protected:
	CString	m_csSource;
};
	
class CRMDSConstMessageField : public CRMDSMessageField
{
public:
	CRMDSConstMessageField(CFEEDConfigField* pConfig, PUPDATERECORD pRecord, PTIBRVRECORD pTIBRVRecord, CString csValue) :
	  CRMDSMessageField(pConfig, pRecord, pTIBRVRecord)
	{
		m_csValue = csValue;

		UpdateField(m_csValue);
	}

public:
	
	virtual void Update()
	{
		UpdateField(m_csValue);
	}
	
protected:
	CString	m_csValue;
};

	
enum ERMDSFuncTypes
{
	funcNone,
	funcAlive,
	funcVolume,
	funcCurTime,
	funcCurDate,
	funcStartTime,
	funcDoneTime
};

class CRMDSFuncMessageField : public CRMDSMessageField
{
public:
	CRMDSFuncMessageField(CFEEDConfigField* pConfig, PUPDATERECORD pRecord, PTIBRVRECORD pTIBRVRecord, CString csFunc) : 
	  CRMDSMessageField(pConfig, pRecord, pTIBRVRecord)
	{
		m_csFunc	= csFunc;
			
		if (m_csFunc.CompareNoCase("Alive") == 0)
			m_eFuncType = funcAlive;
		else if (m_csFunc.CompareNoCase("Volume") == 0)
			m_eFuncType = funcVolume;
		else if (m_csFunc.CompareNoCase("CurTime") == 0)
			m_eFuncType = funcCurTime;
		else if (m_csFunc.CompareNoCase("CurDate") == 0)
			m_eFuncType = funcCurDate;
		else if (m_csFunc.CompareNoCase("StartTime") == 0)
			m_eFuncType = funcStartTime;
		else if (m_csFunc.CompareNoCase("DoneTime") == 0)
			m_eFuncType = funcDoneTime;
		else
			m_eFuncType = funcNone;
	}
	
public:

	virtual void Empty()
	{
		if (m_eFuncType == funcStartTime || m_eFuncType == funcDoneTime)
		{
			Update();
			return;
		}

		UpdateField("");
	}

	virtual void Update()
	{
		UpdateField(Calculate());
	}
	
protected:

	CString Calculate()
	{
		switch (m_eFuncType)
		{
		case funcAlive:
			return Func_Alive();
		case funcVolume:
			return Func_Volume();
		case funcCurTime:
			return Func_CurTime();
		case funcCurDate:
			return Func_CurDate();
		case funcStartTime:
			return Func_StartTime();
		case funcDoneTime:
			return Func_DoneTime();
		default:
			return "";
		}
	}

	
	CString Func_Alive()
	{
		CString csTemp; csTemp.Format("%d", m_pCacheChangesInfo->m_iAliveState);
		return csTemp;
	}

	CString Func_Volume()
	{
		CString csOut;
		csOut.Format("%.2f", m_pCacheChangesInfo->m_dVolume);
		return csOut;
	}
	
	CString Func_CurTime()
	{
		struct tm tmCurr;
		time_t tCurr;
		time(&tCurr);
		tmCurr = *localtime(&tCurr);

		CString csValue;
		csValue.Format("%02d:%02d:%02d", tmCurr.tm_hour, tmCurr.tm_min, tmCurr.tm_sec);
		return csValue;
	}

	CString Func_CurDate()
	{
		struct tm tmCurr;
		time_t tCurr;
		time(&tCurr);
		tmCurr = *localtime(&tCurr);

		CString csValue;
		csValue.Format("%02d/%02d/%4d", tmCurr.tm_mon + 1, tmCurr.tm_mday, tmCurr.tm_year + 1900);
		return csValue;
	}

	CString Func_StartTime()
	{
		return m_pCacheChangesInfo->m_csStartTime;
	}

	CString Func_DoneTime()
	{
		return m_pCacheChangesInfo->m_csDoneTime;
	}
	
protected:
	CString			m_csFunc;
	ERMDSFuncTypes	m_eFuncType;
};
	
enum ERMDSRecalcTypes
{
	recalcNone,
	recalcSecCode,
	recalcPriceChange,
	recalcPrevPrice,
	recalcFromLots,
	recalcToLots,
	recalcRDate,
	recalcConfName,
	recalcFIXDate,
	recalcFIXTime,
	recalcFIXBuySide,
	recalcFIXSellSide,
	recalcFIXSide,
	recalcFIXSecType
};

class CRMDSRecalcMessageField : public CRMDSMessageField
{
public:
	CRMDSRecalcMessageField(CFEEDConfigField* pConfig, PUPDATERECORD pRecord, PTIBRVRECORD pTIBRVRecord, CString csSource, CString csRecalc) : 
	  CRMDSMessageField(pConfig, pRecord, pTIBRVRecord)
	{
		m_csSource = csSource;
		m_csRecalc = csRecalc;

		if (m_csRecalc.CompareNoCase("SecCode") == 0)
			m_eRecalcType = recalcSecCode;
		else if (m_csRecalc.CompareNoCase("PriceChange") == 0)
			m_eRecalcType = recalcPriceChange;
		else if (m_csRecalc.CompareNoCase("PrevPrice") == 0)
			m_eRecalcType = recalcPrevPrice;
		else if (m_csRecalc.CompareNoCase("FromLots") == 0)
			m_eRecalcType = recalcFromLots;
		else if (m_csRecalc.CompareNoCase("ToLots") == 0)
			m_eRecalcType = recalcToLots;
		else if (m_csRecalc.CompareNoCase("RDate") == 0)
			m_eRecalcType = recalcRDate;
		else if (m_csRecalc.CompareNoCase("ConfName") == 0)
			m_eRecalcType = recalcConfName;
		else if (m_csRecalc.CompareNoCase("FIXDate") == 0)
			m_eRecalcType = recalcFIXDate;
		else if (m_csRecalc.CompareNoCase("FIXTime") == 0)
			m_eRecalcType = recalcFIXTime;
		else if (m_csRecalc.CompareNoCase("FIXBuySide") == 0)
			m_eRecalcType = recalcFIXBuySide;
		else if (m_csRecalc.CompareNoCase("FIXSellSide") == 0)
			m_eRecalcType = recalcFIXSellSide;
		else if (m_csRecalc.CompareNoCase("FIXSide") == 0)
			m_eRecalcType = recalcFIXSide;
		else if (m_csRecalc.CompareNoCase("FIXSecType") == 0)
			m_eRecalcType = recalcFIXSecType;
		else
			m_eRecalcType = recalcNone;
	}
	
public:

	virtual void Update()
	{
		CString csValue = GetValue(m_csSource);

        csValue = Calculate(csValue);

		UpdateField(csValue);
	}
	
protected:

	CString Calculate(CString csValue)
	{
		switch (m_eRecalcType)
		{
		case recalcSecCode:
			return Recalc_SecCode(csValue);
		case recalcPriceChange:
			return Recalc_PriceChange(csValue);
		case recalcPrevPrice:
			return Recalc_PrevPrice(csValue);
		case recalcFromLots:
			return Recalc_FromLots(csValue);
		case recalcToLots:
			return Recalc_ToLots(csValue);
		case recalcRDate:
			return Recalc_RDate(csValue);
		case recalcConfName:
			return Recalc_ConfName(csValue);
		case recalcFIXDate:
			return Recalc_FIXDate(csValue);
		case recalcFIXTime:
			return Recalc_FIXTime(csValue);
		case recalcFIXBuySide:
			return Recalc_FIXBuySide(csValue);
		case recalcFIXSellSide:
			return Recalc_FIXSellSide(csValue);
		case recalcFIXSide:
			return Recalc_FIXSide(csValue);
		case recalcFIXSecType:
			return Recalc_FIXSecType(csValue);
		default:
			return csValue;
		}
	}

		
	CString Recalc_SecCode(CString csValue)
	{
		CString csMarketCode = GetValue(TABLE_SECURITIES, FIELD_SECURITIES_MARKETCODE);
		
		if(csMarketCode.CompareNoCase("RPS") == 0)
			return csValue + "PS";

		return csValue;
	}

	CString Recalc_PriceChange(CString csValue)
	{
		float fLast;
		float fChange;
		float fPrevPrice;

		sscanf((LPSTR)(LPCSTR)csValue, "%f", &fLast);

		CString csChange = GetValue(TABLE_SECURITIES, "CHANGE");

		sscanf((LPSTR)(LPCSTR)csChange, "%f", &fChange);

		fPrevPrice = fLast - fChange;

		double dPctChange = 0;
		if(fPrevPrice != 0) 
			dPctChange = (fLast - fPrevPrice) * 100 / fPrevPrice;

		CString csPctChange;
		csPctChange.Format("%.2f", dPctChange);
		return csPctChange;
	}

	CString Recalc_PrevPrice(CString csValue)
	{
		float fLast;
		float fChange;
		float fPrevPrice;

		sscanf((LPSTR)(LPCSTR)csValue, "%f", &fLast);

		CString csChange = GetValue(TABLE_SECURITIES, FIELD_SECURITIES_CHANGE);

		sscanf((LPSTR)(LPCSTR)csChange, "%f", &fChange);

		fPrevPrice = fLast - fChange;

		CString csDecimals = GetValue(TABLE_SECURITIES, FIELD_SECURITIES_DECIMALS);
		CString csFloatFormat; csFloatFormat.Format("%%.0%sf", (LPCSTR)csDecimals);

		CString csRet;
		csRet.Format(csFloatFormat, fPrevPrice);
		return csRet;
	}

	CString Recalc_FromLots(CString csValue)
	{
		int32 fLotSize = 1;
		CString csLotSize = GetValue(TABLE_SECURITIES, FIELD_SECURITIES_LOTSIZE);
		sscanf((LPSTR)(LPCSTR)csLotSize, "%ld", &fLotSize);

		int32 fQuantity;
		sscanf((LPSTR)(LPCSTR)csValue, "%ld", &fQuantity);

		fQuantity = fQuantity * fLotSize;

		csValue.Format("%ld", fQuantity);

		return csValue;
	}

	CString Recalc_ToLots(CString csValue)
	{
		int32 fLotSize = 1;
		CString csLotSize = GetValue(TABLE_SECURITIES, FIELD_SECURITIES_LOTSIZE);
		sscanf((LPSTR)(LPCSTR)csLotSize, "%ld", &fLotSize);

		int32 fQuantity;
		sscanf((LPSTR)(LPCSTR)csValue, "%ld", &fQuantity);

		if(fLotSize != 0)
			fQuantity = fQuantity / fLotSize;

		csValue.Format("%ld", fQuantity);

		return csValue;
	}

	CString Recalc_RDate(CString csValue)
	{
		int nYear;
		int nMonth;
		int nDate;
		
		int nScanned = sscanf((LPCSTR)csValue, "%4d%2d%2d", &nYear, &nMonth, &nDate);

		CString csDate;
		if((nScanned == 3) && (nYear != 0) && (nMonth != 0) && (nDate != 0)) 
			csDate.Format("%02d/%02d/%4d", nMonth, nDate, nYear);
		else
			csDate = csValue;
		
		return csDate;
	}

	CString Recalc_ConfName(CString csValue)
	{
		CString csRet = csValue;

		if(!csValue.IsEmpty()) 
		{
			int nSlashPos = csValue.Find("/");
			
			if(nSlashPos != -1) 
				csRet = csValue.Left(nSlashPos);

			csRet.Trim();

			if(csRet.GetLength() > 12) 
				csRet = csValue.Left(12);
		}
		return csRet;
	}
	
	CString Recalc_FIXDate(CString csValue)
	{
		struct tm tmCurr;
		time_t tCurr;
		time(&tCurr);
		tmCurr = *localtime(&tCurr);

		csValue.Format("%4d%02d%02d", tmCurr.tm_year + 1900, tmCurr.tm_mon + 1, tmCurr.tm_mday);

		return csValue;
	}

	CString Recalc_FIXTime(CString csValue)
	{
		struct tm tmCurr;
		time_t tCurr;
		time(&tCurr);
		tmCurr = *localtime(&tCurr);

		CString csDate;
		csDate.Format("%4d%02d%02d", tmCurr.tm_year + 1900, tmCurr.tm_mon + 1, tmCurr.tm_mday);

		return csDate + "-" + csValue;
	}

	CString Recalc_FIXBuySide(CString csValue)
	{
		if (csValue.CompareNoCase("B") == 0)
			return GetValue("FIRMID");
		else
			return GetValue("CPFIRMID");
	}

	CString Recalc_FIXSellSide(CString csValue)
	{
		if (!csValue.CompareNoCase("B") == 0)
			return GetValue("FIRMID");
		else
			return GetValue("CPFIRMID");
	}

	CString Recalc_FIXSide(CString csValue)
	{
		return (csValue.CompareNoCase("B") == 0) ? "1" : "2";
	}

	CString Recalc_FIXSecType(CString csValue)
	{
		CString csFIXSecType = "FF";

		if (csValue.Find("MICX") >= 0)
		{
			csFIXSecType += "I";
		}
		else
		{
			if (csValue.Find("EUR") >= 0 || csValue.Find("USD") >= 0)
				csFIXSecType += "C";
			else
				csFIXSecType += "M";
		}

		csFIXSecType += "CSX";

		return csFIXSecType;
	}
	
protected:
	CString				m_csSource;
	CString				m_csRecalc;
	ERMDSRecalcTypes	m_eRecalcType;
};