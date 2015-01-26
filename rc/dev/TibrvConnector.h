#pragma once
#include "feedthread.h"

#include "FEEDSection.h"
#include "MICEXTable.h"

#include "RMDSMessageCreator.h"
#include "SafeQueue.h"

#include <tibrv/tibrvcpp.h>
#include <tibrv/cmcpp.h>



class CTibrvConnector :
	public CFEEDThread
{
public:
	CTibrvConnector(void);
	virtual ~CTibrvConnector(void);

public:
	BOOL RunBegin();
	void RunStep();
	void RunEnd();

	bool AddUpdate(CMICEXChangesInfo* pChangesInfo);

public:
	static CTibrvConnector* s_pTibrvInstance;

private:
	bool ProcessChangesInfo(CMICEXChangesInfo* pChangesInfo);
	bool Initialize();
	void Destroy();
	void PublishTable(CMICEXChangesInfo* pChangesInfo);
	void PublishVolume(CMICEXChangesInfo* pChangesInfo);
	void PublishAlive(CMICEXChangesInfo* pChangesInfo);
	bool SendMessageToTibrv(TibrvMsg& msg, bool bCertified = true);
	std::string MakeVolumeSubject(enumeration::MICEXSection::Values section);
	std::string MakeSecuritiesSubject(enumeration::MICEXSection::Values section, const std::string& secName);
	std::string MapSection(enumeration::MICEXSection::Values section);

	void RegisterAnticipatedListener( const CString& subject );

	bool IsInitialized() const {
		return m_bInitialized;
	}
	
	void PublishTrade(enumeration::MICEXSection::Values sectionType, CMICEXTable& tbl, LPBYTE pRowData);
	void PublishUSTrades(enumeration::MICEXSection::Values sectionType, CMICEXTable& tbl, LPBYTE pRowData);
	void PublishSecurities(enumeration::MICEXSection::Values sectionType, CMICEXTable& tbl, LPBYTE pRowData);
private:
	bool m_bInitialized;
	TibrvNetTransport transport;
	TibrvCmTransport cmTransport;

	CSafeQueue<CMICEXChangesInfo*> m_changesQueue;

private:
	typedef std::set<CString> SubjectsSet;
	SubjectsSet m_Subjects;

};
