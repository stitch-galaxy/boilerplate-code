#pragma once

#include "RC_ConfigInt_Int.h"
#include "FOMessageHelper.h"
#include "com/rencap/common/protocol/connectivity/enumeration/MICEXSection.h"

#include <map>
#include <set>

class CFEEDConfigField
{
public:
	CFEEDConfigField()
	{
		m_csSource		= "";
		
		m_csRMDSFiled	= "";
		m_csTIBRVFiled	= "";
		m_csTIBRVDesc	= "";
		
		m_csIgnoreValues= "";

		m_bLog			= false;
	}

public:
	CString	m_csSource;
	
	CString	m_csRMDSFiled;
	CString	m_csTIBRVFiled;
	CString	m_csTIBRVDesc;
	CString	m_csIgnoreValues;

	BOOL	m_bLog;
};

typedef vector<CFEEDConfigField*> VEC_CONFIG_FIELDS;

class CFEEDConfigMessageCreator
{
public:
	CFEEDConfigMessageCreator()
	{
		m_bRMDS			= FALSE;
		m_bTIBRV		= FALSE;
		m_bTIBRVCert	= FALSE;

		m_csName		= "";
		m_csField		= "";
		
		m_csRMDSFormat	= "";
		m_csTIBRVFormat	= "";
		
		m_iClearFrame	= 1;
	}

	~CFEEDConfigMessageCreator()
	{
		for (VEC_CONFIG_FIELDS::iterator it = m_vecFields.begin(); it != m_vecFields.end(); it++)
		{
			delete (*it);
		}
		m_vecFields.clear();
	}

public:
	CString				m_csName;

	BOOL				m_bRMDS;
	BOOL				m_bTIBRV;
	BOOL				m_bTIBRVCert;

	CString				m_csField;

	CString				m_csRMDSFormat;
	CString				m_csTIBRVFormat;

	int					m_iClearFrame;

	VEC_CONFIG_FIELDS	m_vecFields;
};

class CFEEDConfigTable
{
public:
	CFEEDConfigTable()
	{
		m_csTableName			= "";

		m_bSaveToDB				= FALSE;

		m_bPublish				= FALSE;
		m_bTibrvFOPublish		= FALSE;
		m_pMessageCreator		= NULL;
	}

	~CFEEDConfigTable()
	{
		if (m_pMessageCreator != NULL)
			delete m_pMessageCreator;		
	}

	BOOL IsPublishToRMDS() const {
		return m_bPublish;
	}

	BOOL IsPublish() const {
		return m_bPublish || m_bTibrvFOPublish;
	}

	BOOL IsTibrvFOPublish() const {
		return m_bTibrvFOPublish;
	}

public:
	CString				m_csTableName;
	CString				m_csTableParams;
	BOOL				m_bTableComplete;

	BOOL				m_bSaveToDB;

	BOOL						m_bPublish;
	BOOL						m_bTibrvFOPublish;
	CFEEDConfigMessageCreator*	m_pMessageCreator;
};

typedef vector<CFEEDConfigTable*> VEC_CONFIG_TABLES;

class CFEEDConfigAlive
{
public:
	CFEEDConfigAlive()
	{
		m_bPublish			= FALSE;
		m_pMessageCreator	= NULL;
	}

	~CFEEDConfigAlive()
	{
		if (m_pMessageCreator != NULL)
			delete m_pMessageCreator;		
	}

public:
	BOOL						m_bPublish;
	CFEEDConfigMessageCreator*	m_pMessageCreator;
};


class CFEEDConfigVolume
{
public:
	CFEEDConfigVolume()
	{
		m_bPublish			= FALSE;
		m_bTibrvFOPublish = FALSE;
		m_pMessageCreator	= NULL;
	}

	~CFEEDConfigVolume()
	{
		if (m_pMessageCreator != NULL)
			delete m_pMessageCreator;		
	}
	BOOL IsPublishToRMDS() const {
		return m_bPublish;
	}

	BOOL IsPublish() const {
		return m_bPublish || m_bTibrvFOPublish;
	}

	BOOL IsTibrvFOPublish() const {
		return m_bTibrvFOPublish;
	}
public:
	BOOL						m_bPublish;
	BOOL						m_bTibrvFOPublish;
	CFEEDConfigMessageCreator*	m_pMessageCreator;
};

typedef vector<CString> VEC_CONFIG_CONNECTIONS;

class CFEEDConfigSection
{
public:

	CFEEDConfigSection()
	{
		m_bEnabled				= FALSE;

		m_csName				= "";

		m_bUseDB				= FALSE;
		m_iSaveDays				= 1;
		m_csSQLServer			= "";
		m_csSQLDatabase			= "";

		m_csLogonDays			= "1,2,3,4,5";
		m_csLogonTime			= "10:00";
		m_csLogoffTime			= "17:30";

		m_pAlive				= NULL;
		m_pVolume				= NULL;

		m_sectionType			= enumeration::MICEXSection::Unspecified;
	}

	~CFEEDConfigSection()
	{
		if (m_pAlive != NULL)
			delete m_pAlive;

		if (m_pVolume != NULL)
			delete m_pVolume;

		for (VEC_CONFIG_TABLES::iterator it = m_vecTables.begin(); it != m_vecTables.end(); it++)
		{
			delete (*it);
		}
		m_vecTables.clear();
	}

	CFEEDConfigTable* GetTableConfig(CString csTableName)
	{
		for (VEC_CONFIG_TABLES::iterator it = m_vecTables.begin(); it != m_vecTables.end(); it++)
		{
			if (csTableName.CompareNoCase((*it)->m_csTableName) == 0)
				return *it;
		}

		return NULL;
	}

	bool IsEnabled() const
	{
		return m_bEnabled == TRUE;
	}

public:
	BOOL				m_bEnabled;

	CString				m_csName;
	
	BOOL				m_bUseDB;
	BOOL				m_iSaveDays;
	CString				m_csSQLServer;
	CString				m_csSQLDatabase;

	CString				m_csLogonDays;
	CString				m_csLogonTime;
	CString				m_csLogoffTime;
	
	enumeration::MICEXSection::Values m_sectionType;
public:
	VEC_CONFIG_CONNECTIONS	m_vecConnections;

public:
	CFEEDConfigAlive*	m_pAlive;
	CFEEDConfigVolume*	m_pVolume;

public:
	VEC_CONFIG_TABLES		m_vecTables;
};

typedef vector<CFEEDConfigSection*> VEC_CONFIG_SECTIONS;

class CFEEDConfigRMDS
{
public:
	CFEEDConfigRMDS()
	{
		m_bEnabled		= FALSE;
		m_csFeedName	= "NONE";
	}
	bool IsEnabled() const
	{
		return m_bEnabled == TRUE;
	}
public:
	BOOL	m_bEnabled;
	CString	m_csFeedName;
};

// FIX-752
typedef std::set<CString> CmListeners;

class CFEEDConfigTIBRV
{
public:
	CFEEDConfigTIBRV(BOOL bCert)
	{
		m_bEnabled		= FALSE;

		m_bCert			= bCert;

		m_csServiceName	= "";
		m_csNetworkName	= "";
		m_csDaemonName	= "";

		m_csCmName		= "";
		m_csLedgerFile	= "";
		m_bRequestOld	= FALSE;
		m_bSyncLedger	= FALSE;
		m_sSubjectTrade	= "";
		m_sSubjectUSTrade = "";
		m_sSubjectSecurities = "";
		m_sSubjectVolume = "";
		m_cmListeners.clear();
	}
	~CFEEDConfigTIBRV()
	{
		m_cmListeners.clear();
	}
	CString ToString();
	bool IsEnabled() const
	{
		return m_bEnabled == TRUE;
	}
public:
	BOOL	m_bEnabled;
	
	BOOL	m_bCert;

	CString	m_csServiceName;
	CString	m_csNetworkName;
	CString	m_csDaemonName;

	CString	m_csCmName;
	CString	m_csLedgerFile;
	BOOL	m_bSyncLedger;

	BOOL	m_bRequestOld;
	// FIX-752
	// list of anticipated listeners from config
	CmListeners m_cmListeners;
	CString m_sSubjectTrade;
	CString m_sSubjectUSTrade;
	CString m_sSubjectSecurities;
	CString m_sSubjectVolume;
};

class CFEEDConfig
{
public:
	CFEEDConfig(void);
	~CFEEDConfig(void);
public:
	static std::string GetParamStringAtStart(const string& paramName);
	int GetTimeCorrectionValue();

public:
	BOOL Load(CString csProfile);
	
	BOOL LoadSettings();
	BOOL LoadRMDS(MSXML2::IXMLDOMNodePtr pRMDSNode, CFEEDConfigRMDS* pRMDSConfig);
	// FIX-752
	// if bIsSertified = TRUE - load anticipated listeners list from configuration
	BOOL LoadTIBRV(MSXML2::IXMLDOMNodePtr pTIBRVNode, CFEEDConfigTIBRV* pTIBRVConfig, BOOL bIsSertified = FALSE);
	BOOL LoadSection(MSXML2::IXMLDOMNodePtr pSectionNode, CFEEDConfigSection* pSection);
	BOOL LoadTable(MSXML2::IXMLDOMNodePtr pTableNode, CFEEDConfigTable* pTable);
	BOOL LoadMessageCreator(MSXML2::IXMLDOMNodePtr pMessageCreatorNode, CFEEDConfigMessageCreator* pMessageCreator);
	BOOL LoadField(MSXML2::IXMLDOMNodePtr pFieldNode, CFEEDConfigField* pField);

	BOOL LoadAlive(MSXML2::IXMLDOMNodePtr pAliveNode, CFEEDConfigAlive* pAlive);
	BOOL LoadVolume(MSXML2::IXMLDOMNodePtr pVolumeNode, CFEEDConfigVolume* pVolume);

	void Save();
// FIX-752 begin
private:
	bool LoadTibrvParameters();
	int LoadCmListeners(MSXML2::IXMLDOMNodePtr pNode, CFEEDConfigTIBRV* pTIBRVConfig);
// FIX-752 end
	enumeration::MICEXSection::Values MapSectionByName(const CString& csName);

protected:
	CString GetAttr(MSXML2::IXMLDOMNodePtr pNode, CString csName);
	BOOL GetParamBOOL(MSXML2::IXMLDOMNodePtr pParentNode, CString csName);
	int GetParamInt(MSXML2::IXMLDOMNodePtr pParentNode, CString csName);
	CString GetParamStr(MSXML2::IXMLDOMNodePtr pParentNode, CString csName);
	bool GetBoolean(MSXML2::IXMLDOMNodePtr pParent, bstr_t pChildName, bstr_t attrName);
	CString GetAttributeValue(MSXML2::IXMLDOMNodePtr pNode, bstr_t attrName);
	CString GetAttributeStringVal(MSXML2::IXMLDOMNodePtr pParent, bstr_t pChildName, bstr_t attrName);
	void CheckMandatoryParameter( const CString& paramName, const CString& sValue );
	bool CheckForEmpty( const CString& paramName, const CString& sValue, const CString& defValue );
public:
	static CFEEDConfig*		s_pConfigInstance;

protected:
	CString m_csProfile;

public:
	CString					m_csServiceName;
	CString					m_csExchange;
	CString					m_csExchangeCurrency;
	CFEEDConfigRMDS*		m_pRMDSConfig;
	CFEEDConfigTIBRV*		m_pTIBRVConfig;
	CFEEDConfigTIBRV*		m_pTIBRVCertConfig;
	CFEEDConfigTIBRV*		m_pCfgTibrvCm;

	VEC_CONFIG_SECTIONS		m_vecSections;
};
