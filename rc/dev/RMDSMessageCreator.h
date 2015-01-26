#pragma once

#include <vector>
#include <map>

#include "RMDSLib.h"
#include "TIBRVLib.h"
#include "MICEXTable.h"
#include "MICEXSection.h"
#include "RMDSMessageField.h"

class CRMDSMessageCreator
{
protected:
	CRMDSMessageCreator(CFEEDConfigMessageCreator* pConfig);

public:
	virtual ~CRMDSMessageCreator();

public:
	static CRMDSMessageCreator* Create(CFEEDConfigAlive* pAlive);
	static CRMDSMessageCreator* Create(CFEEDConfigVolume* pVolume);
	static CRMDSMessageCreator* Create(CFEEDConfigTable* pTable);

	static CRMDSMessageCreator* Create(CFEEDConfigMessageCreator* pConfig);
	static void Delete(CRMDSMessageCreator* pMessageCreator);

public:

	BOOL ClearFileds(CMICEXChangesInfo* pChangesInfo);
	BOOL UpdateFileds(CMICEXChangesInfo* pChangesInfo, CString& csLog);

	PUPDATERECORD	GetRMDSClearMessage();
	PUPDATERECORD	GetRMDSMessage();
	PTIBRVRECORD	GetTIBRVMessage();

	virtual void	Clear() = 0;

protected:

	virtual BOOL	OnClearFileds()
	{
		return FALSE;
	}

	virtual void	OnUpdateFileds()
	{
	}

	virtual CString	GetRMDSEmptySubject() = 0;
	virtual CString	GetRMDSSubject() = 0;
	virtual CString	GetTIBRVSubject() = 0;

protected:
	PUPDATERECORD			m_pRMDSRecord;
	PTIBRVRECORD			m_pTIBRVRecord;

public:
	CString						m_csName;
	CFEEDConfigMessageCreator*	m_pConfig;

protected:
	VEC_RMDS_MESSAGE_VIELDS	m_vecFields;
};

typedef map<CString, CRMDSMessageCreator*> MAP_MESSAGE_CREATORS;

class CRMDSNumBaseMessageCreator : public CRMDSMessageCreator
{
public:
	CRMDSNumBaseMessageCreator(CFEEDConfigMessageCreator* pConfig) : 
		CRMDSMessageCreator(pConfig)
	{
		m_csRMDSFormat = pConfig->m_csRMDSFormat;
		m_csTIBRVFormat = pConfig->m_csTIBRVFormat;

		Clear();
	}

	virtual ~CRMDSNumBaseMessageCreator()
	{
	}

public:
	void Clear()
	{
		m_iIndex		= 0;
		

		m_bCleared		= FALSE;
		m_iClearIndex	= m_pConfig->m_iClearFrame + 1;
	}

protected:
	BOOL	OnClearFileds()
	{
		if (!m_bCleared)
		{
			m_iClearIndex--;

			return TRUE;
		}

		if (m_iClearIndex <= m_iIndex + m_pConfig->m_iClearFrame)
		{
			m_iClearIndex++;

			return TRUE;
		}

		return FALSE;
	}

	void	OnUpdateFileds()
	{
		m_iIndex++;
	}

	CString	GetRMDSSubject(int iIndex)
	{
		CString csSubject;
		csSubject.Format(m_csRMDSFormat, iIndex);
		return csSubject;
	}

	CString	GetRMDSEmptySubject()
	{
		int iIndex = m_iClearIndex;

		if (m_iClearIndex == 1)
		{
			m_iClearIndex	= m_pConfig->m_iClearFrame;
			m_bCleared		= TRUE;
		}

		return GetRMDSSubject(iIndex);
	}

	CString GetRMDSSubject()
	{
		return GetRMDSSubject(m_iIndex);
	}

	CString GetTIBRVSubject()
	{
		CString csSubject;
		csSubject.Format(m_csTIBRVFormat, m_iIndex);
		return csSubject;
	}

protected:
	CString	m_csRMDSFormat;
	CString	m_csTIBRVFormat;

	int		m_iIndex;
	int		m_iClearIndex;

	BOOL	m_bCleared;
	int		m_iClearItem;
};

class CRMDSFieldBaseMessageCreator : public CRMDSMessageCreator
{
public:
	CRMDSFieldBaseMessageCreator(CFEEDConfigMessageCreator* pConfig) : 
		CRMDSMessageCreator(pConfig)
	{
		m_csRMDSFormat = pConfig->m_csRMDSFormat;
		m_csTIBRVFormat = pConfig->m_csTIBRVFormat;

		m_csField = pConfig->m_csField;

		m_pSubjectField = NULL;

		for (VEC_RMDS_MESSAGE_VIELDS::iterator itField = m_vecFields.begin(); itField != m_vecFields.end(); itField++)
		{
			if ((*itField)->m_csRMDSFiled.CompareNoCase(m_csField) == 0 ||
				(*itField)->m_csTIBRVFiled.CompareNoCase(m_csField) == 0)
			{
				m_pSubjectField = (*itField);
			}
		}
	}

	virtual ~CRMDSFieldBaseMessageCreator()
	{
	}

public:
	void Clear()
	{
	}

protected:

	CString	GetRMDSEmptySubject()
	{
		return "";
	}

	CString GetRMDSSubject()
	{
		if (m_pSubjectField == NULL)
			return "NONE";

		CString csSubject;
		csSubject.Format(m_csRMDSFormat, m_pSubjectField->m_csValue);
		return csSubject;
	}

	CString GetTIBRVSubject()
	{
		if (m_pSubjectField == NULL)
			return "NONE";

		CString csSubject;
		csSubject.Format(m_csTIBRVFormat, m_pSubjectField->m_csValue);
		return csSubject;
	}

protected:
	CString				m_csRMDSFormat;
	CString				m_csTIBRVFormat;

	CString				m_csField;
	CRMDSMessageField*	m_pSubjectField;
};