#include "stdafx.h"
#include "RMDSMessageCreator.h"

#include "FHLogger.h"

CMICEXChangesInfo*		CRMDSMessageField::m_pCacheChangesInfo	= NULL;
LPVOID					CRMDSMessageField::m_pCacheRowData		= NULL;
MAP_CACHE_VALUES		CRMDSMessageField::m_mapCacheValues;

CMICEXTable*			CRMDSMessageField::m_pCacheSecTable			= NULL;
CMICEXTable*			CRMDSMessageField::m_pCacheSecTypesTable	= NULL;
CMICEXTable*			CRMDSMessageField::m_pCacheUSTable			= NULL;
LPVOID					CRMDSMessageField::m_pCacheSecRowData		= NULL;
LPVOID					CRMDSMessageField::m_pCacheSecTypesRowData	= NULL;
LPVOID					CRMDSMessageField::m_pCacheUSRowData		= NULL;

CRMDSMessageField* CRMDSMessageField::Create(CFEEDConfigField* pConfig, PUPDATERECORD pRecord, PTIBRVRECORD pTIBRVRecord)
{
	CRMDSMessageField* pMessageField = NULL;

	if (pConfig->m_csSource.Find('!') < 0)
	{
		pMessageField = new CRMDSSourceMessageField(pConfig, pRecord, pTIBRVRecord, pConfig->m_csSource);
	}
	else if (pConfig->m_csSource.Left(2).CompareNoCase("C!") == 0)
	{
		CString csValue = pConfig->m_csSource.Mid(2);

		pMessageField = new CRMDSConstMessageField(pConfig, pRecord, pTIBRVRecord, csValue);
	}
	else if (pConfig->m_csSource.Left(2).CompareNoCase("F!") == 0)
	{
		CString csFunc = pConfig->m_csSource.Mid(2);

		pMessageField = new CRMDSFuncMessageField(pConfig, pRecord, pTIBRVRecord, csFunc);
	}
	else if (pConfig->m_csSource.Left(2).CompareNoCase("R!") == 0)
	{
		CString csSettings = pConfig->m_csSource.Mid(2);
	
		int iDelimeterPos = csSettings.Find('!');

		CString csSource	= csSettings.Left(iDelimeterPos);
		CString csRecalc	= csSettings.Mid(iDelimeterPos + 1);

		pMessageField = new CRMDSRecalcMessageField(pConfig, pRecord, pTIBRVRecord, csSource, csRecalc);
	}

	return pMessageField;
}

void CRMDSMessageField::Delete(CRMDSMessageField* pField)
{
	delete pField;

	/*if (pField->m_csSource.Find('!') < 0)
	{
		delete (CRMDSSourceMessageField*)pField;
	}
	else if (pField->m_csSource.Left(2).CompareNoCase("C!") == 0)
	{
		delete (CRMDSConstMessageField*)pField;
	}
	else if (pField->m_csSource.Left(2).CompareNoCase("F!") == 0)
	{
		delete (CRMDSFuncMessageField*)pField;
	}
	else if (pField->m_csSource.Left(2).CompareNoCase("R!") == 0)
	{
		delete (CRMDSRecalcMessageField*)pField;
	}*/
}

	
CRMDSMessageCreator* CRMDSMessageCreator::Create(CFEEDConfigAlive* pAlive)
{
	return Create(pAlive->m_pMessageCreator);
}

CRMDSMessageCreator* CRMDSMessageCreator::Create(CFEEDConfigVolume* pVolume)
{
	return Create(pVolume->m_pMessageCreator);
}

CRMDSMessageCreator* CRMDSMessageCreator::Create(CFEEDConfigTable* pTable)
{
	return Create(pTable->m_pMessageCreator);
}

	
CRMDSMessageCreator* CRMDSMessageCreator::Create(CFEEDConfigMessageCreator* pConfig)
{
	CRMDSMessageCreator* pMessageCreator = NULL;
	
	CString m_csName = pConfig->m_csName; 

    if (m_csName.CompareNoCase("NumBase") == 0)
		pMessageCreator = new CRMDSNumBaseMessageCreator(pConfig);
	else if (m_csName.CompareNoCase("FieldBase") == 0)
		pMessageCreator = new CRMDSFieldBaseMessageCreator(pConfig);

	return pMessageCreator;
}

void CRMDSMessageCreator::Delete(CRMDSMessageCreator* pMessageCreator)
{
	delete pMessageCreator;

	/*if (pMessageCreator->m_csName.CompareNoCase("NumBase") == 0)
		delete (CRMDSNumBaseMessageCreator*)pMessageCreator;
	else if (pMessageCreator->m_csName.CompareNoCase("FieldBase") == 0)
		delete (CRMDSFieldBaseMessageCreator*)pMessageCreator;*/
}

	
CRMDSMessageCreator::CRMDSMessageCreator(CFEEDConfigMessageCreator* pConfig)
{
	m_pConfig	= pConfig;
	m_csName	= pConfig->m_csName;

	VEC_CONFIG_FIELDS& vecFields = pConfig->m_vecFields;

	m_pRMDSRecord = NULL;
	m_pTIBRVRecord = NULL;

	if (pConfig->m_bRMDS)
	{
		m_pRMDSRecord = new UPDATERECORD();
		memset(m_pRMDSRecord, 0, sizeof(UPDATERECORD));
		
		lstrcpy(m_pRMDSRecord->szItemSource, (LPSTR)(LPCSTR)CFEEDConfig::s_pConfigInstance->m_pRMDSConfig->m_csFeedName);
		m_pRMDSRecord->nRecordState = SSL_RECORD_OK;
	}

	if (pConfig->m_bTIBRV || pConfig->m_bTIBRVCert)
	{
		m_pTIBRVRecord = new TIBRVRECORD();
		memset(m_pTIBRVRecord, 0, sizeof(TIBRVRECORD));
	}

	for (VEC_CONFIG_FIELDS::iterator itField = vecFields.begin(); itField != vecFields.end(); itField++)
	{
		CRMDSMessageField* pField = CRMDSMessageField::Create(*itField, m_pRMDSRecord, m_pTIBRVRecord);

		if (pField == NULL)
		{
			LOG_ERROR(SECTION(lSystem), "Invalid field source: %s", (*itField)->m_csSource);
//			CFEEDLog::s_pLogInstance->FormatLogMessage(EVENTLOG_ERROR_TYPE, "Invalid field source: %s", (*itField)->m_csSource);
			continue;
		}

		m_vecFields.push_back(pField);
	}

}

CRMDSMessageCreator::~CRMDSMessageCreator()
{
	for (VEC_RMDS_MESSAGE_VIELDS::iterator itField = m_vecFields.begin(); itField != m_vecFields.end(); itField++)
	{
		CRMDSMessageField::Delete((*itField));
	}
	m_vecFields.clear();

	if (m_pRMDSRecord != NULL)
		delete m_pRMDSRecord;
	if (m_pTIBRVRecord != NULL)
		delete m_pTIBRVRecord;
}

	
BOOL CRMDSMessageCreator::ClearFileds(CMICEXChangesInfo* pChangesInfo)
{
	CRMDSMessageField::InitCache(pChangesInfo);

	if (!OnClearFileds())
		return FALSE;

	for (VEC_RMDS_MESSAGE_VIELDS::iterator itField = m_vecFields.begin(); itField != m_vecFields.end(); itField++)
	{
		CRMDSMessageField* pField = *itField;

		pField->Empty();
	}

	CRMDSMessageField::ClearCache();

	return TRUE;
}

BOOL CRMDSMessageCreator::UpdateFileds(CMICEXChangesInfo* pChangesInfo, CString& csLog)
{
	pChangesInfo->m_csDoneTime = MakeTimeStamp();

	CRMDSMessageField::InitCache(pChangesInfo);

	OnUpdateFileds();

	BOOL bIsIgnoreValue = FALSE;

	BOOL bIsFirstField = TRUE;

	for (VEC_RMDS_MESSAGE_VIELDS::iterator itField = m_vecFields.begin(); itField != m_vecFields.end(); itField++)
	{
		CRMDSMessageField* pField = *itField;

		pField->Update();

		bIsIgnoreValue |= pField->IsIgnoreValue();

		if (pField->m_bLog)
		{
			if (!bIsFirstField)
				csLog += ", ";
			bIsFirstField = FALSE;

			csLog += (!pField->m_csRMDSFiled.IsEmpty()) ? pField->m_csRMDSFiled : pField->m_csTIBRVFiled;
			csLog += "=";
			csLog += pField->m_csValue;
		}
	}

	CRMDSMessageField::ClearCache();

	return bIsIgnoreValue;
}

	
PUPDATERECORD CRMDSMessageCreator::GetRMDSClearMessage()
{
	if (m_pRMDSRecord == NULL)
		return NULL;

	CString csSubject = GetRMDSEmptySubject();
	if (csSubject.IsEmpty())
		return NULL;

	lstrcpy(m_pRMDSRecord->szItemName, (LPCSTR)csSubject);

	return m_pRMDSRecord;
}

PUPDATERECORD CRMDSMessageCreator::GetRMDSMessage()
{
	if (m_pRMDSRecord == NULL)
		return NULL;

	CString csSubject = GetRMDSSubject();
	if (csSubject.IsEmpty())
		return NULL;

	lstrcpy(m_pRMDSRecord->szItemName, (LPCSTR)csSubject);

	return m_pRMDSRecord;
}

PTIBRVRECORD CRMDSMessageCreator::GetTIBRVMessage()
{
	if (m_pTIBRVRecord == NULL)
		return NULL;

	CString csSubject = GetTIBRVSubject();
	if (csSubject.IsEmpty())
		return NULL;

	lstrcpy(m_pTIBRVRecord->pSubjectName, (LPCSTR)csSubject);

	return m_pTIBRVRecord;
}