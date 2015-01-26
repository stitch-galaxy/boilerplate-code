#include "stdafx.h"
#include "MICEXSection.h"

#include "FEEDConfig.h"
#include "MICEXConnector.h"
#include "MICEXDB.h"
#include "RMDSConnector.h"
#include "TibrvConnector.h"

CMICEXSection::CMICEXSection(CFEEDConfigSection* pConfig)
{
	m_pConfig			= pConfig;

	if (m_pConfig->m_bUseDB)
		m_pDB			= new CMICEXDB(this);
	else
		m_pDB			= NULL;

	m_pSchema			= new CMICEXSchema(this);
	m_pConnector		= new CMICEXConnector(this);

	m_pVolumeCounter	= new CMICEXVolumeCounter();
}

CMICEXSection::~CMICEXSection()
{
	if (m_pDB != NULL)
		delete m_pDB;

	delete m_pSchema;
	delete m_pConnector;

	delete m_pVolumeCounter;
}

	
BOOL CMICEXSection::Start()
{
	if (m_pDB != NULL)
	{
		if (!m_pDB->StartAndWait())
			return FALSE;
	}

	if (!m_pConnector->StartAndWait())
		return FALSE;

	return TRUE;
}

void CMICEXSection::Stop()
{
	m_pConnector->Stop();

	if (m_pDB != NULL)
		m_pDB->Stop();
}

void CMICEXSection::Pause()
{
	if (m_pDB != NULL)
		m_pDB->Pause();

	m_pConnector->Pause();
}

void CMICEXSection::Continue()
{
	m_pConnector->Continue();

	if (m_pDB != NULL)
		m_pDB->Continue();
}

	
void CMICEXSection::UpdateSessionState(int iAliveState)
{
	CMICEXChangesInfo* pChangesInfo = new CMICEXChangesInfo(this, iAliveState);

	CRMDSConnector::s_pRMDSInstance->AddUpdate(pChangesInfo);
	CTibrvConnector::s_pTibrvInstance->AddUpdate(pChangesInfo);
}

	
void CMICEXSection::UpdateTableStructure(CMICEXTable* pTable)
{
	if (pTable->m_pConfig == NULL)
		return;

	CMICEXChangesInfo* pInitInfo = new CMICEXChangesInfo(MakeTimeStamp(), pTable, NULL, FALSE);
	pInitInfo->m_bIsInit = TRUE;
	pInitInfo->m_pSection = this;
	
	Update(pInitInfo);

	if ( m_pConfig->m_pVolume->IsPublish() && pTable->m_csTableName.Compare("SECURITIES") == 0)
	{
		CMICEXChangesInfo* pClearVolumeInfo = new CMICEXChangesInfo(MakeTimeStamp(), this, 0);
		if(m_pConfig->m_pVolume->IsPublishToRMDS()) {
			CRMDSConnector::s_pRMDSInstance->AddUpdate(pClearVolumeInfo);
		}
		if(m_pConfig->m_pVolume->IsTibrvFOPublish()) {
			CTibrvConnector::s_pTibrvInstance->AddUpdate(pClearVolumeInfo);
		}
		if(!m_pConfig->m_pVolume->IsPublishToRMDS()) {
			delete pClearVolumeInfo;
		}

	}
}

void CMICEXSection::UpdateTableBegin(CMICEXTable* pTable)
{
	if (pTable->m_lAttributes & 2)  // ClearOnUpdate
	{
		pTable->ClearTableData();

		CMICEXChangesInfo* pClearInfo = new CMICEXChangesInfo(MakeTimeStamp(), pTable, NULL, FALSE);
		pClearInfo->m_bIsClear = TRUE;
		pClearInfo->m_pSection = this;

		Update(pClearInfo);
	}
}

void CMICEXSection::UpdateRowComplete(CMICEXChangesInfo* pChangesInfo)
{
	if (pChangesInfo == NULL || 
		pChangesInfo->m_pTable == NULL || 
		pChangesInfo->m_pTable->m_pConfig == NULL)
		return;

	pChangesInfo->m_pSection = this;

	////////////////////////////////////////////////////////////////////////
	// Update volume counter

	if ( m_pConfig->m_pVolume->IsPublish() && pChangesInfo->m_pTable->m_csTableName.Compare("SECURITIES") == 0)
	{
		m_pVolumeCounter->Update(pChangesInfo->m_pTable, pChangesInfo->m_pRowData);
	}

	////////////////////////////////////////////////////////////////////////
	// Process changes

	Update(pChangesInfo);
}

void CMICEXSection::UpdateTableComplete(CMICEXTable* pTable)
{
	////////////////////////////////////////////////////////////////////////
	// Volume to RMDS

	if (m_pConfig->m_pVolume->IsPublish() && pTable->m_csTableName.Compare("SECURITIES") == 0 && m_pVolumeCounter->UpdateComplete())
	{
		CMICEXChangesInfo* pChangesInfo = new CMICEXChangesInfo(MakeTimeStamp(), this,
													m_pVolumeCounter->GetVolume());

		if(m_pConfig->m_pVolume->IsPublishToRMDS()) {
			CRMDSConnector::s_pRMDSInstance->AddUpdate(pChangesInfo);
		}
		if(m_pConfig->m_pVolume->IsTibrvFOPublish()) {
			CTibrvConnector::s_pTibrvInstance->AddUpdate(pChangesInfo);
		}
		if(!m_pConfig->m_pVolume->IsPublishToRMDS()) {
			delete pChangesInfo;
		}
	}
}

	
void CMICEXSection::Update(CMICEXChangesInfo* pChangesInfo)
{
	if(pChangesInfo->m_pTable->m_pConfig->IsTibrvFOPublish()) {
		CTibrvConnector::s_pTibrvInstance->AddUpdate(pChangesInfo);
	}
	if (pChangesInfo->m_pTable->m_pConfig->IsPublishToRMDS()) {
		CRMDSConnector::s_pRMDSInstance->AddUpdate(pChangesInfo);
	} else if (m_pDB != NULL && pChangesInfo->m_pTable->m_pConfig->m_bSaveToDB) {
		m_pDB->AddUpdate(pChangesInfo);
	} else {
		delete pChangesInfo;
	}
}
