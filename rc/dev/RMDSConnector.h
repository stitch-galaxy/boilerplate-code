#pragma once

#include <map>

#include "RMDSLib.h"
#include "TIBRVLib.h"
#include "FEEDThread.h"
#include "FEEDConfig.h"

#include "FEEDSection.h"
#include "MICEXTable.h"

#include "RMDSMessageCreator.h"

class CFEEDConfigSection;

#define _ZERO_FRAME_SIZE 25

class CRMDSConnector : 
	public CRMDSLib,
	public CTIBRVLib,
	public CFEEDThread
{
public:
	CRMDSConnector();
	~CRMDSConnector(void);

public:
	BOOL RunBegin();
	void RunStep();
	void RunEnd();

protected:
	BOOL Update(CMICEXChangesInfo* pChangesInfo);

public:
	BOOL AddUpdate(CMICEXChangesInfo* pChangesInfo);
	CMICEXChangesInfo* GetUpdate();

	void ClearChangesQueue();

protected:
	void ReleaseMessageCreators();

	CRMDSMessageCreator* GetAliveMessageCreator(CMICEXSection* pSection);
	CRMDSMessageCreator* GetVolumeMessageCreator(CMICEXSection* pSection);
	CRMDSMessageCreator* GetTableMessageCreator(CMICEXSection* pSection, CMICEXTable* pTable);

protected:
	void PublishAlive(CMICEXChangesInfo* pChangesInfo);
	void PublishVolume(CMICEXChangesInfo* pChangesInfo);
	void PublishTable(CMICEXChangesInfo* pChangesInfo);

protected:
	BOOL PublishClear(CRMDSMessageCreator* pMessageCreator, CMICEXChangesInfo* pChangesInfo, CString lpstrTable, BOOL IsLogRecord = TRUE);
	BOOL Publish(CRMDSMessageCreator* pMessageCreator, CMICEXChangesInfo* pChangesInfo, CString lpstrTable, BOOL IsLogRecord = TRUE);

	BOOL PublishRMDSRecord(PUPDATERECORD pRecord, CString lpstrTable, CString& csLog, BOOL IsLogRecord);
	BOOL PublishTIBRVRecord(int iSession, PTIBRVRECORD pRecord, CString lpstrTable, CString& csLog, BOOL IsLogRecord);

public:
	static CRMDSConnector*		s_pRMDSInstance;

protected:
	int							m_bInitialized;

	int							m_iSSLSessionID;
	int							m_iTIBRVSessionID;
	int							m_iTIBRVCertSessionID;

	MAP_MESSAGE_CREATORS		m_mapMessageCreators;

protected:
	CRITICAL_SECTION			m_critChangesInfos;
	VEC_CHANGES_INFO			m_vecChangesInfos;
// FIX-752 begin
private:
	typedef std::set<CString> SubjectsSet;
	// List of messages subjects for registration anticipeted listeners
	SubjectsSet m_Subjects;
// FIX-752 end
	static string MakeSectionsName(const char* lpTableName);
};