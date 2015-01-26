#include "stdafx.h"
#include "FEEDConfig.h"
#include "MICEXTable.h"
#include "FHLogger.h"

#include "RC_ConfigInt_Int.h"
#include "DllUtils.h"

extern string LastErrorMessageText();

const char* pcszTrue = "true";
const char* pcszFalse = "false";

CFEEDConfig*	CFEEDConfig::s_pConfigInstance = NULL;

CFEEDConfig::CFEEDConfig(void)
{
	m_pRMDSConfig	= NULL;
	m_pTIBRVConfig	= NULL;
	m_pCfgTibrvCm = NULL;
}

CFEEDConfig::~CFEEDConfig(void)
{
	for (VEC_CONFIG_SECTIONS::iterator itSection = m_vecSections.begin(); itSection != m_vecSections.end(); itSection++)
		delete (*itSection);
	m_vecSections.clear();

	if (m_pRMDSConfig != NULL)
		delete m_pRMDSConfig;
	if (m_pTIBRVConfig != NULL)
		delete m_pTIBRVConfig;
	m_pRMDSConfig	= NULL;
	m_pTIBRVConfig	= NULL;

	m_pCfgTibrvCm = NULL;
}

BOOL CFEEDConfig::Load(CString csProfile)
{
	m_csProfile = csProfile;

	if (!LoadConfigFile((LPCSTR)m_csProfile))
	{
		LOG_ERROR(SECTION(lConfig), "Unable to open config file '%s'. %s",
			(LPCSTR)m_csProfile, LastErrorMessageText().c_str());
		return FALSE;
	}
	
	if (!LoadSettings())
		return FALSE;

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	SaveAndCloseConfigFile();

	return TRUE;
}
	
CString CFEEDConfig::GetAttr(MSXML2::IXMLDOMNodePtr pNode, CString csName)
{
	MSXML2::IXMLDOMNamedNodeMapPtr	pAttrs = pNode->attributes;
	MSXML2::IXMLDOMNodePtr			pAttr = pAttrs->getNamedItem((bstr_t)csName);

	if (pAttr == NULL)
		return "";

	variant_t val = pAttr->nodeValue;

	return (LPSTR)(bstr_t)val;
}

BOOL CFEEDConfig::GetParamBOOL(MSXML2::IXMLDOMNodePtr pParentNode, CString csName)
{
	CString csVal = GetParamStr(pParentNode, csName);
	if (csVal.IsEmpty())
		return FALSE;

	return (csVal.CompareNoCase(pcszTrue) == 0);
}

int CFEEDConfig::GetParamInt(MSXML2::IXMLDOMNodePtr pParentNode, CString csName)
{
	CString csVal = GetParamStr(pParentNode, csName);
	if (csVal.IsEmpty())
		return 0;

	return atoi(csVal);
}

CString CFEEDConfig::GetParamStr(MSXML2::IXMLDOMNodePtr pParentNode, CString csName)
{
	if(pParentNode == NULL)
		return "";

	CString csSearch;
	//csSearch.Format("descendant::param->Item[@name='%s']", csName);
	csSearch.Format("param[@name='%s']", csName);

	MSXML2::IXMLDOMNodePtr pNode = pParentNode->selectSingleNode((bstr_t)csSearch);

	if (pNode == NULL)
		return "";

	return GetAttr(pNode, "value");
}

CString CFEEDConfig::GetAttributeValue(MSXML2::IXMLDOMNodePtr pNode, bstr_t attrName)
{
	if(pNode == NULL)
		return "";

	variant_t varValue;
	((IXMLDOMElementPtr)pNode)->getAttribute(attrName, &varValue);
	if(varValue.vt == VT_NULL)
		return "";

	const CString sTemp(( const char*)(bstr_t)varValue);
	return sTemp;
}

CString CFEEDConfig::GetAttributeStringVal(MSXML2::IXMLDOMNodePtr pParent, bstr_t pChildName, bstr_t attrName)
{
	if(pParent == NULL)
		return "";

	MSXML2::IXMLDOMNodePtr pNode = pParent->selectSingleNode(pChildName);
	if(pNode == NULL)
		return "";

	return GetAttributeValue(pNode, attrName);
}

bool CFEEDConfig::CheckForEmpty( const CString& paramName, const CString& sValue, const CString& defValue )
{
	if(sValue.IsEmpty())
	{
		LOG_WARNING(SECTION(lConfig), "Parameter '%s' is not configured, the default value is used: %s",
			(LPCSTR)paramName, (LPCSTR)defValue);
		return true;
	}
	return false;
}

void CFEEDConfig::CheckMandatoryParameter( const CString& paramName, const CString& sValue )
{
	if(sValue.IsEmpty())
		throw DllException("Mandatory parameter '%s' is not configured", (LPCSTR)paramName);
	return;
}

bool CFEEDConfig::GetBoolean(MSXML2::IXMLDOMNodePtr pParent, bstr_t pChildName, bstr_t attrName)
{
	CString sTemp = GetAttributeStringVal(pParent, pChildName, attrName);
	sTemp.MakeLower();

	if(sTemp.Compare(pcszTrue) != 0 && sTemp.Compare(pcszFalse) != 0)
	{
		LOG_WARNING(SECTION(lConfig),
			"Incorrect parameter value <%s %s=\"%s\">. Will be used defaut value 'false'",
			(const char*)pChildName, (const char*)attrName, (LPCSTR)sTemp);
		return false;
	}
	else
	{
		LOG_TRACE(SECTION(lConfig), "%s %s='%s'", (const char*)pChildName, (const char*)attrName, (LPCSTR)sTemp);
	}
	return (sTemp.Compare(pcszTrue) == 0);
}

	
BOOL CFEEDConfig::LoadSettings()
{
	LPVOID pDoc = GetConfigDomDocument();

	MSXML2::IXMLDOMDocumentPtr	pDOMDocument	= (MSXML2::IXMLDOMDocument*)pDoc;
	if (pDOMDocument == NULL)
	{
		LOG_FATAL(SECTION(lConfig), "Unable to read configuration - retriving document.");
		return FALSE;
	}

	MSXML2::IXMLDOMElementPtr	pSettingsElement	= pDOMDocument->documentElement;
	if (pSettingsElement == NULL)
	{
		LOG_FATAL(SECTION(lConfig), "Unable to read configuration - retriving root element.");
		return FALSE;
	}

	MSXML2::IXMLDOMNodePtr		pSettingsNode	= pSettingsElement;

	/////////////////////////////////////////////////////////////////////////////////////////
	// Service settings

	this->m_csServiceName = GetParamStr(pSettingsNode, "serviceName");
	this->m_csExchange = GetParamStr(pSettingsNode, "exchange");
	this->m_csExchangeCurrency = GetParamStr(pSettingsNode, "exchangeCurrency");
	
	try {
		CheckMandatoryParameter("serviceName", this->m_csServiceName);
		CheckMandatoryParameter("exchange", this->m_csExchange);
		CheckMandatoryParameter("exchangeCurrency", this->m_csExchangeCurrency);
	}
	catch (DllException& ex) {
		LOG_FATAL(SECTION(lConfig), ex.what());
		return FALSE;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	// RMDS settings

	this->m_pRMDSConfig = new CFEEDConfigRMDS();

	MSXML2::IXMLDOMNodePtr		pRMDSNode	= pSettingsElement->selectSingleNode(L"rmdsConnection");

	if (!LoadRMDS(pRMDSNode, this->m_pRMDSConfig))
		return FALSE;

	/////////////////////////////////////////////////////////////////////////////////////////
	// TIBRV settings

	this->m_pTIBRVConfig = new CFEEDConfigTIBRV(FALSE);

	MSXML2::IXMLDOMNodePtr		pTIBRVNode	= pSettingsElement->selectSingleNode(L"tibrvConnection");

	if (!LoadTIBRV(pTIBRVNode, this->m_pTIBRVConfig))
		return FALSE;

	/////////////////////////////////////////////////////////////////////////////////////////
	// TIBRV Certified settings

	this->m_pTIBRVCertConfig = new CFEEDConfigTIBRV(TRUE);

	MSXML2::IXMLDOMNodePtr		pTIBRVCertNode	= pSettingsElement->selectSingleNode(L"tibrvCertConnection");
	// FIX-752
	// load settings of tibrv certified message delivery
	if (!LoadTIBRV(pTIBRVCertNode, this->m_pTIBRVCertConfig, TRUE))
		return FALSE;

	//////////////////////////////////////////////////////////////////////////
	// Tibrv FO settings
	this->m_pCfgTibrvCm = new CFEEDConfigTIBRV(TRUE);
	if(!LoadTibrvParameters())
		return FALSE;


	/////////////////////////////////////////////////////////////////////////////////////////
	// Sections settings

	MSXML2::IXMLDOMNodeListPtr pSectionsNodeList = pSettingsElement->selectNodes(L"section");

	for (long lSection = 0; lSection < pSectionsNodeList->length; lSection++)
	{
		MSXML2::IXMLDOMNodePtr pSectionNode = pSectionsNodeList->Getitem(lSection);

		CFEEDConfigSection* pSection = new CFEEDConfigSection();

		m_vecSections.push_back(pSection);

		if (!LoadSection(pSectionNode, pSection))
			return FALSE;
	}

	/////////////////////////////////////////////////////////////////////////////////////////

	return TRUE;
}
	
BOOL CFEEDConfig::LoadRMDS(MSXML2::IXMLDOMNodePtr pRMDSNode, CFEEDConfigRMDS* pRMDSConfig)
{
	if(pRMDSConfig == NULL)
		return FALSE;

	pRMDSConfig->m_bEnabled		= GetParamBOOL(pRMDSNode, "enabled");
	pRMDSConfig->m_csFeedName	= GetParamStr(pRMDSNode, "feedName");
	
	try {
		if(pRMDSConfig->IsEnabled())
			CheckMandatoryParameter("feedName", pRMDSConfig->m_csFeedName);		
	}
	catch (DllException& ex) {
		LOG_FATAL(SECTION(lConfig), ex.what());
		return FALSE;
	}

	return TRUE;
}

BOOL CFEEDConfig::LoadTIBRV(MSXML2::IXMLDOMNodePtr pTIBRVNode, CFEEDConfigTIBRV* pTIBRVConfig, BOOL bIsSertified)
{
	if(pTIBRVConfig == NULL)
		return FALSE;

	pTIBRVConfig->m_bEnabled		= GetParamBOOL(pTIBRVNode, "enabled");

	pTIBRVConfig->m_csServiceName	= GetParamStr(pTIBRVNode, "serviceName");
	pTIBRVConfig->m_csNetworkName	= GetParamStr(pTIBRVNode, "networkName");
	pTIBRVConfig->m_csDaemonName	= GetParamStr(pTIBRVNode, "daemonName");

	pTIBRVConfig->m_csCmName		= GetParamStr(pTIBRVNode, "cmName");
	pTIBRVConfig->m_csLedgerFile	= GetParamStr(pTIBRVNode, "ledgerFile");
	pTIBRVConfig->m_bSyncLedger		= GetParamBOOL(pTIBRVNode, "syncLedger");

	pTIBRVConfig->m_bRequestOld		= GetParamBOOL(pTIBRVNode, "requestOld");
	// FIX-752 begin
	// Load anticipated listeners from config file
	if(bIsSertified == TRUE) {
		MSXML2::IXMLDOMNodePtr pCmListenersNode = pTIBRVNode->selectSingleNode(L"AnticipatedListeners");
		int nCmLoaded = LoadCmListeners(pCmListenersNode, pTIBRVConfig);
		if(nCmLoaded > 0)
			LOG_DEBUG(SECTION(lConfig), "Configuration: '%d' anticipated listeners were loaded from config", nCmLoaded);
		else
			LOG_DEBUG(SECTION(lConfig), "Configuration: not one anticipated listener was not loaded from config");
	}
	// FIX-752 end
	return TRUE;
}

// FIX-752 begin
// Load anticipated listeners from config file
// Function returns quantity loaded listeners
int CFEEDConfig::LoadCmListeners(MSXML2::IXMLDOMNodePtr pNode, CFEEDConfigTIBRV* pTIBRVConfig)
{
	if(pNode == NULL || pTIBRVConfig == NULL)
		return 0;
	MSXML2::IXMLDOMNodeListPtr pNodes = pNode->selectNodes(L"Listener");
	for(long i = 0; i < pNodes->length; i++)
	{
		MSXML2::IXMLDOMNodePtr pItem = pNodes->Getitem(i);
		const CString strCmName = GetAttr(pItem, "cmname");
		if( !strCmName.IsEmpty() && pTIBRVConfig->m_cmListeners.insert(strCmName).second == true)
			LOG_DEBUG(SECTION(lConfig), "Configuration: Loaded anticipated listener '%s'", (LPCSTR)strCmName);
	}
	return (int)(pTIBRVConfig->m_cmListeners.size());
}
// FIX-752 end
enumeration::MICEXSection::Values CFEEDConfig::MapSectionByName(const CString& csName)
{
	enumeration::MICEXSection::Values sectionType = enumeration::MICEXSection::Unspecified;
	if(csName.CompareNoCase("Equty") == 0)
		sectionType = enumeration::MICEXSection::EQ;
	else
		if(csName.CompareNoCase("GKO") == 0)
			sectionType = enumeration::MICEXSection::GKO;
		else
			if(csName.CompareNoCase("FO") == 0)
				sectionType = enumeration::MICEXSection::FO;

	return sectionType;
}

BOOL CFEEDConfig::LoadSection(MSXML2::IXMLDOMNodePtr pSectionNode, CFEEDConfigSection* pSection)
{
	if(pSection == NULL)
		return FALSE;

	pSection->m_csName			= GetAttr(pSectionNode, "name");

	pSection->m_bEnabled		= GetParamBOOL(pSectionNode, "enabled");

	pSection->m_bUseDB			= GetParamBOOL(pSectionNode, "useDB");
	pSection->m_iSaveDays		= GetParamInt(pSectionNode, "saveDays");
	pSection->m_csSQLServer		= GetParamStr(pSectionNode, "sqlServer");
	pSection->m_csSQLDatabase	= GetParamStr(pSectionNode, "sqlDatabase");

	pSection->m_csLogonDays		= GetParamStr(pSectionNode, "logonDays");
	pSection->m_csLogonTime		= GetParamStr(pSectionNode, "logonTime");
	pSection->m_csLogoffTime	= GetParamStr(pSectionNode, "logoffTime");

	// FIX-1374
	pSection->m_sectionType = MapSectionByName(pSection->m_csName);

	////////////////////////////////////////////////////////
	// Connection

	MSXML2::IXMLDOMNodeListPtr pConnectionsNodeList = pSectionNode->selectNodes(L"connection");

	for (long lConnection = 0; lConnection < pConnectionsNodeList->length; lConnection++)
	{
		MSXML2::IXMLDOMNodePtr pConnectionNode = pConnectionsNodeList->Getitem(lConnection);

		CString csValue1			= GetParamStr(pConnectionNode, "1");
		CString csValue2			= GetParamStr(pConnectionNode, "2");
		CString csValue3			= GetParamStr(pConnectionNode, "3");
		CString csValue4			= GetParamStr(pConnectionNode, "4");
		CString csValue5			= GetParamStr(pConnectionNode, "5");

		CString csConnectionString = "";
		
		csConnectionString	+= csValue1 + '\n';
		csConnectionString	+= csValue2 + '\n';
		csConnectionString	+= csValue3 + '\n';
		csConnectionString	+= csValue4 + '\n';
		csConnectionString	+= csValue5 + '\n';

		pSection->m_vecConnections.push_back(csConnectionString);
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	// Alive settings

	pSection->m_pAlive = new CFEEDConfigAlive();

	MSXML2::IXMLDOMNodePtr  pAliveNode = pSectionNode->selectSingleNode(L"alive");

	if (!LoadAlive(pAliveNode, pSection->m_pAlive))
		return FALSE;

	////////////////////////////////////////////////////////
	// Volume

	MSXML2::IXMLDOMNodePtr pVolumeNode = pSectionNode->selectSingleNode(L"volume");
	
	pSection->m_pVolume = new CFEEDConfigVolume();

	if (pVolumeNode != NULL)
	{
		if (!LoadVolume(pVolumeNode, pSection->m_pVolume))
			return FALSE;
	}

	////////////////////////////////////////////////////////
	// Table

	MSXML2::IXMLDOMNodeListPtr pTablesNodeList = pSectionNode->selectNodes(L"table");

	for (long lTable = 0; lTable < pTablesNodeList->length; lTable++)
	{
		MSXML2::IXMLDOMNodePtr pTableNode = pTablesNodeList->Getitem(lTable);

		CFEEDConfigTable* pTable = new CFEEDConfigTable();

		pSection->m_vecTables.push_back(pTable);

		if (!LoadTable(pTableNode, pTable))
			return FALSE;
	}

	return TRUE;
}

BOOL CFEEDConfig::LoadTable(MSXML2::IXMLDOMNodePtr pTableNode, CFEEDConfigTable* pTable)
{
	if(pTable == NULL)
		return FALSE;

	pTable->m_csTableName		= GetAttr(pTableNode, "name");
	pTable->m_csTableParams		= GetAttr(pTableNode, "params");
	pTable->m_bTableComplete	= (GetAttr(pTableNode, "complete").CompareNoCase("true") == 0);

	pTable->m_bSaveToDB		= GetParamBOOL(pTableNode, "SaveToDB");
	pTable->m_bPublish		= GetParamBOOL(pTableNode, "Publish");
	pTable->m_bTibrvFOPublish = GetParamBOOL(pTableNode, "TibrvFOPublish");

	try {
		if(m_pCfgTibrvCm->IsEnabled() && pTable->IsTibrvFOPublish()) {
			if(pTable->m_csTableName.Compare(TABLE_TRADES) == 0)
				CheckMandatoryParameter("tibrv\\subjectTrade", m_pCfgTibrvCm->m_sSubjectTrade);
			else if(pTable->m_csTableName.Compare(TABLE_USTRADES) == 0)
				CheckMandatoryParameter("tibrv\\subjectUSTrade", m_pCfgTibrvCm->m_sSubjectUSTrade);
			else if(pTable->m_csTableName.Compare(TABLE_SECURITIES) == 0)
				CheckMandatoryParameter("tibrv\\subjectSecurities", m_pCfgTibrvCm->m_sSubjectSecurities);
		}
	}
	catch (DllException& ex) {
		LOG_FATAL(SECTION(lConfig), ex.what());
		return FALSE;
	}

	pTable->m_pMessageCreator = new CFEEDConfigMessageCreator();

	if (pTable->IsPublishToRMDS()) {
		MSXML2::IXMLDOMNodePtr pMessageCreatorNode = pTableNode->selectSingleNode(L"messageCreator");
		
		if (pMessageCreatorNode == NULL) {
			LOG_ERROR(SECTION(lConfig), "Unable to read configuration - messageCreator for table.");
			return FALSE;
		}

		if (!LoadMessageCreator(pMessageCreatorNode, pTable->m_pMessageCreator))
			return FALSE;
	}

	return TRUE;
}

BOOL CFEEDConfig::LoadMessageCreator(MSXML2::IXMLDOMNodePtr pMessageCreatorNode, CFEEDConfigMessageCreator* pMessageCreator)
{
	if(pMessageCreator == NULL)
		return FALSE;

	pMessageCreator->m_csName		= GetAttr(pMessageCreatorNode, "name");

	pMessageCreator->m_bRMDS		= (GetAttr(pMessageCreatorNode, "rmds").CompareNoCase(pcszTrue) == 0);
	pMessageCreator->m_bTIBRV		= (GetAttr(pMessageCreatorNode, "tibrv").CompareNoCase(pcszTrue) == 0);
	pMessageCreator->m_bTIBRVCert	= (GetAttr(pMessageCreatorNode, "tibrvCert").CompareNoCase(pcszTrue) == 0);

	pMessageCreator->m_csField		= GetParamStr(pMessageCreatorNode, "field");
	
	pMessageCreator->m_csRMDSFormat	= GetParamStr(pMessageCreatorNode, "rmdsFormat");
	pMessageCreator->m_csTIBRVFormat= GetParamStr(pMessageCreatorNode, "tibrvFormat");

	pMessageCreator->m_iClearFrame	= GetParamInt(pMessageCreatorNode, "clearframe");

	MSXML2::IXMLDOMNodeListPtr pFieldsNodeList = pMessageCreatorNode->selectNodes(L"field");

	for (long lField = 0; lField < pFieldsNodeList->length; lField++)
	{
		MSXML2::IXMLDOMNodePtr pFieldNode = pFieldsNodeList->Getitem(lField);

		CFEEDConfigField* pField = new CFEEDConfigField();

		pMessageCreator->m_vecFields.push_back(pField);

		if (!LoadField(pFieldNode, pField))
			return FALSE;
	}

	return TRUE;
}

BOOL CFEEDConfig::LoadField(MSXML2::IXMLDOMNodePtr pFieldNode, CFEEDConfigField* pField)
{
	if(pField == NULL)
		return FALSE;

	pField->m_csSource		= GetAttr(pFieldNode, "source");
	
	pField->m_csRMDSFiled	= GetAttr(pFieldNode, "rmdsFiled");
	pField->m_csTIBRVFiled	= GetAttr(pFieldNode, "tibrvField");
	pField->m_csTIBRVDesc	= GetAttr(pFieldNode, "tibrvDesc");
	pField->m_csIgnoreValues= GetAttr(pFieldNode, "ignoreValues");

	pField->m_bLog			= (GetAttr(pFieldNode, "log").CompareNoCase("true") == 0);

	return TRUE;
}

	
BOOL CFEEDConfig::LoadAlive(MSXML2::IXMLDOMNodePtr pAliveNode, CFEEDConfigAlive* pAlive)
{
	if(pAlive == NULL)
		return FALSE;

	pAlive->m_bPublish		= GetParamBOOL(pAliveNode, "Publish");

	pAlive->m_pMessageCreator = new CFEEDConfigMessageCreator();

	if (pAlive->m_bPublish)
	{
		MSXML2::IXMLDOMNodePtr pMessageCreatorNode = pAliveNode->selectSingleNode(L"messageCreator");
		
		if (pMessageCreatorNode == NULL)
		{
			LOG_ERROR(SECTION(lConfig), "Unable to read configuration - messageCreator for alive.");
			return FALSE;
		}

		if (!LoadMessageCreator(pMessageCreatorNode, pAlive->m_pMessageCreator))
			return FALSE;
	}

	return TRUE;
}

BOOL CFEEDConfig::LoadVolume(MSXML2::IXMLDOMNodePtr pVolumeNode, CFEEDConfigVolume* pVolume)
{
	if(pVolume == NULL)
		return FALSE;

	pVolume->m_bPublish		= GetParamBOOL(pVolumeNode, "Publish");
	pVolume->m_bTibrvFOPublish = GetParamBOOL(pVolumeNode, "TibrvFOPublish");
	try {
		if(m_pCfgTibrvCm->IsEnabled() && pVolume->IsTibrvFOPublish())
			CheckMandatoryParameter("tibrv\\subjectVolume", m_pCfgTibrvCm->m_sSubjectVolume);
	}
	catch (DllException& ex) {
		LOG_FATAL(SECTION(lConfig), ex.what());
		return FALSE;
	}
	pVolume->m_pMessageCreator = new CFEEDConfigMessageCreator();

	if (pVolume->IsPublishToRMDS())
	{
		MSXML2::IXMLDOMNodePtr pMessageCreatorNode = pVolumeNode->selectSingleNode(L"messageCreator");
		
		if (pMessageCreatorNode == NULL)
		{
			LOG_ERROR(SECTION(lConfig), "Unable to read configuration - messageCreator for volume.");
			return FALSE;
		}

		if (!LoadMessageCreator(pMessageCreatorNode, pVolume->m_pMessageCreator))
			return FALSE;
	}

	return TRUE;
}

	
void CFEEDConfig::Save()
{
	LoadConfigFile((LPCSTR)m_csProfile);
	SaveAndCloseConfigFile();
}

bool CFEEDConfig::LoadTibrvParameters()
{
	static const char *tibrvPath	= "tibrv";
	static const char *enabledPath	= "enabled";
	static const char *servicePath	= "service";
	static const char *networkPath	= "network";
	static const char *daemonPath	= "daemon";
	static const char *subjectTrade	= "subjectTrade";
	static const char *subjectUSTrade	= "subjectUSTrade";
	static const char *subjectSec	= "subjectSecurities";
	static const char *subjectVol	= "subjectVolume";
	static const char *cmname		= "cmname";
	static const char *ledgerfile	= "ledgerFile";
	static const char *syncLedger	= "syncLedger";
	static const char *requestOld	= "requestOld";

	static const char *attrValue	= "value";

	LOG_DEBUG(SECTION(lConfig), "Loading Tibrv parameters...");
	try {
		MSXML2::IXMLDOMDocumentPtr	pDOMDocument = (MSXML2::IXMLDOMDocument*)GetConfigDomDocument();
		if (pDOMDocument == NULL)
			throw DllException("Unable to load global configuration - retriving document.");

		MSXML2::IXMLDOMElementPtr	pDocRoot = pDOMDocument->documentElement;
		MSXML2::IXMLDOMNodePtr		pMapNode = pDocRoot->selectSingleNode(tibrvPath);
		if (pMapNode == NULL)
			throw DllException("Mandatory section <%s> is not exist", tibrvPath);

		m_pCfgTibrvCm->m_bEnabled = GetBoolean(pMapNode, enabledPath, attrValue);
		if(!m_pCfgTibrvCm->IsEnabled()) {
			LOG_DEBUG(SECTION(lConfig), "Tibrv is disabled");
			return true;
		}

		CString sTemp;
		sTemp = GetAttributeStringVal(pMapNode, servicePath, attrValue);
		CheckForEmpty(servicePath, sTemp, "NULL");
		m_pCfgTibrvCm->m_csServiceName = sTemp;

		sTemp = GetAttributeStringVal(pMapNode, networkPath, attrValue);
		CheckForEmpty(networkPath, sTemp, "NULL");
		m_pCfgTibrvCm->m_csNetworkName = sTemp;

		sTemp = GetAttributeStringVal(pMapNode, daemonPath, attrValue);
		CheckForEmpty(daemonPath, sTemp, "NULL");
		m_pCfgTibrvCm->m_csDaemonName = sTemp;

		m_pCfgTibrvCm->m_sSubjectTrade = GetAttributeStringVal(pMapNode, subjectTrade, attrValue);
		m_pCfgTibrvCm->m_sSubjectUSTrade = GetAttributeStringVal(pMapNode, subjectUSTrade, attrValue);
		m_pCfgTibrvCm->m_sSubjectSecurities = GetAttributeStringVal(pMapNode, subjectSec, attrValue);
		m_pCfgTibrvCm->m_sSubjectVolume = GetAttributeStringVal(pMapNode, subjectVol, attrValue);

		sTemp = GetAttributeStringVal(pMapNode, cmname, attrValue);
		CheckMandatoryParameter(cmname, sTemp);
		m_pCfgTibrvCm->m_csCmName = sTemp;

		sTemp = GetAttributeStringVal(pMapNode, ledgerfile, attrValue);
		CheckMandatoryParameter(ledgerfile, sTemp);
		m_pCfgTibrvCm->m_csLedgerFile = sTemp;

		m_pCfgTibrvCm->m_bRequestOld = GetBoolean(pMapNode, requestOld, attrValue);
		m_pCfgTibrvCm->m_bSyncLedger = GetBoolean(pMapNode, syncLedger, attrValue);

		// load anticipated listeners
		MSXML2::IXMLDOMNodePtr pCmListenersNode = pMapNode->selectSingleNode(L"AnticipatedListeners");
		int nCmLoaded = LoadCmListeners(pCmListenersNode, m_pCfgTibrvCm);
		if(nCmLoaded > 0)
			LOG_DEBUG(SECTION(lConfig), "'%d' anticipated listeners were loaded from config", nCmLoaded);
		else
			LOG_DEBUG(SECTION(lConfig), "Not one anticipated listener was not loaded from config");

		LOG_TRACE(SECTION(lConfig), "Tibrv parameters are loaded: %s", (LPCSTR)m_pCfgTibrvCm->ToString());
		LOG_DEBUG(SECTION(lConfig), "Tibrv parameters are loaded");

	}
	catch (DllException &ex) {
		LOG_FATAL(SECTION(lConfig), ex.what());
		return false;
	}
	return true;
}

std::string CFEEDConfig::GetParamStringAtStart( const string& paramName )
{
	LPVOID pDoc = GetConfigDomDocument();

	MSXML2::IXMLDOMDocumentPtr pDOMDocument = (MSXML2::IXMLDOMDocument*)pDoc;
	if (pDOMDocument == NULL)
		return "";

	MSXML2::IXMLDOMElementPtr pRootElement = pDOMDocument->documentElement;
	if (pRootElement == NULL)
		return "";

	CString csSearch;
	csSearch.Format("param[@name='%s']", paramName.c_str());
	MSXML2::IXMLDOMNodePtr pNode = pRootElement->selectSingleNode((bstr_t)csSearch);

	if (pNode == NULL)
		return "";

	MSXML2::IXMLDOMNamedNodeMapPtr pAttrs = pNode->attributes;
	if(pAttrs == NULL)
		return "";
	MSXML2::IXMLDOMNodePtr pAttr = pAttrs->getNamedItem((bstr_t)"value");

	if (pAttr == NULL)
		return "";

	variant_t val = pAttr->nodeValue;
	return (LPSTR)(bstr_t)val;
}

int CFEEDConfig::GetTimeCorrectionValue()
{
	if(m_csExchange.Compare("PFTS") == 0)
		return 1;
	return 0;
}

CString CFEEDConfigTIBRV::ToString()
{
	CString csGeneral;
	CString csCertified;
	
	csGeneral.Format("service: %s; network: %s; daemon: %s;", 
		m_csServiceName, m_csNetworkName, m_csDaemonName);

	if(m_bCert)
	{
		csCertified.Format(" cmName: %s; ledgerFileName: %s; syncLedger: %s; requestOld: %s",
			m_csCmName, m_csLedgerFile,
			m_bSyncLedger ? pcszTrue : pcszFalse,
			m_bRequestOld ? pcszTrue : pcszFalse);
		csGeneral += csCertified;
	}

	return csGeneral;
}