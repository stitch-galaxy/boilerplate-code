#include "stdafx.h"
#include "TibrvConnector.h"
#include "FEEDConfig.h"
#include "FHLogger.h"

#include "MICEXDB.h"
#include "SafeQueue.h"
#include "FOMessageHelper.h"
#include "TradesFields.h"
#include "SecuritiesFields.h"

#include <tibrv/tibrvcpp.h>
#include <tibrv/cmcpp.h>

#include "com/rencap/common/protocol/connectivity/MICEXTrade.h"
#include "com/rencap/common/protocol/connectivity/MICEXUSTrade.h"
#include "com/rencap/common/protocol/connectivity/MICEXVolume.h"
#include "com/rencap/common/protocol/connectivity/MICEXSecurity.h"

namespace FOMessageHelper
{
	date_time string2time( const std::string& sDateTime )
	{
		time_t tRet;
		struct tm sTm;
		int nHour = 0;
		int nMinute = 0;
		int nSecond = 0;
		int nCnt;
		
		_tzset();
		time(&tRet);
		sTm = *gmtime(&tRet);
		sTm.tm_isdst = -1;

		nCnt = sscanf(sDateTime.c_str(), "%02d:%02d:%02d", 
			&nHour, &nMinute, &nSecond);
		
		if(nCnt == 3)
		{
			sTm.tm_hour = nHour + CFEEDConfig::s_pConfigInstance->GetTimeCorrectionValue();
			sTm.tm_min = nMinute;
			sTm.tm_sec = nSecond;
		}
		else
			return date_time(0, 0);

		return date_time(mktime(&sTm), 0);
	}

	time_t string2date( const std::string& sDateTime )
	{
		time_t tRet;
		struct tm sTm;
		int nYear = 0;
		int nMonth = 0;
		int nMDay = 0;
		int nCnt;

		_tzset();
		time(&tRet);
		sTm = *localtime(&tRet);
		sTm.tm_isdst = -1;

		nCnt = sscanf(sDateTime.c_str(), "%04d%02d%02d", 
			&nYear, &nMonth, &nMDay);
		if(nCnt == 3)
		{
			sTm.tm_year = nYear-1900;
			sTm.tm_mon = nMonth-1;
			sTm.tm_mday = nMDay;
			sTm.tm_hour = 12;
			sTm.tm_min = 0;
			sTm.tm_sec = 0;
		}
		else
			return 0;

		return mktime(&sTm);
	}

	date_time string2DateTime(const std::string& sDateTime)
	{
		time_t tRet;
		struct tm sTm;
		int nYear = 0;
		int nMonth = 0;
		int nDay = 0;
		int nHour = 0;
		int nMinute = 0;
		int nSecond = 0;
		int nCnt;

		int nLen = sDateTime.length();

		_tzset();
		time(&tRet);
		sTm = *gmtime(&tRet);
		sTm.tm_isdst = -1;

		if(nLen == 14)
		{
			nCnt = sscanf(sDateTime.c_str(), "%04d%02d%02d%02d%02d%02d", 
				&nYear, &nMonth, &nDay, &nHour, &nMinute, &nSecond);
			if(nCnt == 6)
			{
				sTm.tm_year = nYear - 1900;
				sTm.tm_mon = nMonth -1;
				sTm.tm_mday = nDay;
				sTm.tm_hour = nHour + CFEEDConfig::s_pConfigInstance->GetTimeCorrectionValue();
				sTm.tm_min = nMinute;
				sTm.tm_sec = nSecond;
			}
			else if(nCnt == 3)
			{
				sTm.tm_year = nYear - 1900;
				sTm.tm_mon = nMonth -1;
				sTm.tm_mday = nDay;
				sTm.tm_hour = 12;
				sTm.tm_min = 0;
				sTm.tm_sec = 0;
			}
		}
		else if(nLen == 8)
		{
			nCnt = sscanf(sDateTime.c_str(), "%04d%02d%02d", 
				&nYear, &nMonth, &nDay);
			if(nCnt == 3)
			{
				sTm.tm_year = nYear - 1900;
				sTm.tm_mon = nMonth -1;
				sTm.tm_mday = nDay;
				sTm.tm_hour = 12;
				sTm.tm_min = 0;
				sTm.tm_sec = 0;
			}
		}
		else if (nLen == 6)
		{
			nCnt = sscanf(sDateTime.c_str(), "%02d%02d%02d", 
				&nHour, &nMinute, &nSecond);
			if(nCnt == 3)
			{
				sTm.tm_hour = nHour + CFEEDConfig::s_pConfigInstance->GetTimeCorrectionValue();
				sTm.tm_min = nMinute;
				sTm.tm_sec = nSecond;
			}
		}
		else
			return date_time(0,0);

		return date_time(mktime(&sTm), 0);
	}

	enumeration::Side::Values MapBuySell(const CString& csVal)
	{
		enumeration::Side::Values ret = enumeration::Side::Unspecified;

		if(csVal.Compare("S") == 0)
			ret = enumeration::Side::Sell;
		else
			ret = enumeration::Side::Buy;

		return ret;
	}

	enumeration::TradingPeriod::Values MapTradingPeriod(const CString& csVal)
	{
		enumeration::TradingPeriod::Values ret = enumeration::TradingPeriod::Unspecified;
		switch(csVal[0]) {
		case 'O': ret = enumeration::TradingPeriod::Opened; break;
		case 'N': ret = enumeration::TradingPeriod::Normal; break;
		case 'C': ret = enumeration::TradingPeriod::Closed; break;
		default: break;
		}
		return ret;
	}

	enumeration::YesNo::Values MapYesNo(const CString& csVal)
	{
		enumeration::YesNo::Values ret = enumeration::YesNo::Unspecified;

		if(csVal.Compare("Y") == 0)
			ret = enumeration::YesNo::Yes;
		else
			if(csVal.Compare("N") == 0)
				ret = enumeration::YesNo::No;

		return ret;
	}
	enumeration::OperationType::Values MapOperationType(const CString& csVal)
	{
		enumeration::OperationType::Values ret = enumeration::OperationType::Unspecified;
		if(csVal.Compare("K") == 0)
			ret = enumeration::OperationType::Include;
		else
			if(csVal.Compare("D") == 0)
				ret = enumeration::OperationType::Discard;

		return ret;
	}
	enumeration::MICEXTradeType::Values MapMICEXTradeType(const CString& csVal)
	{
		enumeration::MICEXTradeType::Values ret = enumeration::MICEXTradeType::Unspecified;

		switch(csVal[0]) {
		case 'T': ret = enumeration::MICEXTradeType::Regular;			break;
		case 'N': ret = enumeration::MICEXTradeType::ExPitTransaction;	break;
		case 'P': ret = enumeration::MICEXTradeType::PrimaryPlacement;	break;
		case 'S': ret = enumeration::MICEXTradeType::Swap;				break;
		case 'W': ret = enumeration::MICEXTradeType::NonSystemSwap;		break;
		case 'F': ret = enumeration::MICEXTradeType::Transfer;			break;
		case 'R': ret = enumeration::MICEXTradeType::REPOFirstTail;		break;
		default: break;
		}
		return ret;
	}

	enumeration::USTradeStatus::Values MapUSTradeStatus(const CString& csVal)
	{
		enumeration::USTradeStatus::Values ret = enumeration::USTradeStatus::Unspecified;
		switch(csVal[0]) {
		case 'U': ret = enumeration::USTradeStatus::NotExecuted;		break;
		case 'P': ret = enumeration::USTradeStatus::IncludedInReport;	break;
		case 'M': ret = enumeration::USTradeStatus::Executed;			break;
		case 'G': ret = enumeration::USTradeStatus::Payment;			break;
		case 'N': ret = enumeration::USTradeStatus::Rejected;			break;
		case 'C': ret = enumeration::USTradeStatus::SystemCancelled;	break;
		case 'W': ret = enumeration::USTradeStatus::UserCancelled;		break;
		default: break;
		}
		return ret;
	}

	enumeration::USTradeType::Values MapUSTradeType(const CString& csVal)
	{
		enumeration::USTradeType::Values ret = enumeration::USTradeType::Unspecified;
		switch(csVal[0]) {
		case 'N': ret = enumeration::USTradeType::NonSystem;		break;
		case 'R': ret = enumeration::USTradeType::REPOFirstTail;	break;
		case 'r': ret = enumeration::USTradeType::REPOSecondTail;	break;
		case 'D': ret = enumeration::USTradeType::CompensationDue;	break;
		case 'P': ret = enumeration::USTradeType::PrimaryPlacement;	break;
		default:  break;
		}
		return ret;
	}

	enumeration::MICEXSecType::Values MapSecType(const CString& csVal)
	{
		enumeration::MICEXSecType::Values ret = enumeration::MICEXSecType::Unspecified;
		
		switch(csVal[0]) {
		case '1': ret = enumeration::MICEXSecType::regularShare;		break;
		case '2': ret = enumeration::MICEXSecType::privilegedShare;		break;
		case '3': ret = enumeration::MICEXSecType::SecurityRF;			break;
		case '4': ret = enumeration::MICEXSecType::SecurityMunicipal;	break;
		case '5': ret = enumeration::MICEXSecType::SecurityCBRF;		break;
		case '6': ret = enumeration::MICEXSecType::CorporateBonds;		break;
		case '7': ret = enumeration::MICEXSecType::MFOBonds;			break;
		case '8': ret = enumeration::MICEXSecType::ExchangeBonds;		break;
		case '9': ret = enumeration::MICEXSecType::openSharesPIF;		break;
		case 'A': ret = enumeration::MICEXSecType::intervalSharesPIF;	break;
		case 'B': ret = enumeration::MICEXSecType::closeSharesPIF;		break;
		default: break;
		}

		return ret;
	}

	enumeration::MICEXSecStatus::Values MapMICEXSecStatus(const CString& csVal)
	{
		enumeration::MICEXSecStatus::Values ret = enumeration::MICEXSecStatus::Unspecified;
		switch(csVal[0]) {
		case 'A': ret = enumeration::MICEXSecStatus::allowed; break;
		case 'S': ret = enumeration::MICEXSecStatus::forbidden; break;
		case 'N': ret = enumeration::MICEXSecStatus::blocked; break;
		default: break;
		}
		
		return ret;
	}

	enumeration::MICEXTradingStatus::Values MapMICEXTradingStatus(const CString& csVal)
	{
		enumeration::MICEXTradingStatus::Values ret = enumeration::MICEXTradingStatus::Unspecified;
		switch(csVal[0]) {
		case 'N': ret = enumeration::MICEXTradingStatus::inaccessible; break;
		case 'O': ret = enumeration::MICEXTradingStatus::openingPeriod; break;
		case 'C': ret = enumeration::MICEXTradingStatus::closed; break;
		case 'F': ret = enumeration::MICEXTradingStatus::closingPeriod; break;
		case 'B': ret = enumeration::MICEXTradingStatus::theBreak; break;
		case 'T': ret = enumeration::MICEXTradingStatus::tradingSession; break;
		default: break;
		}

		return ret;
	}

	enumeration::MICEXQuoteBasis::Values MapMICEXQuoteBasis(const CString& csVal)
	{
		enumeration::MICEXQuoteBasis::Values ret = enumeration::MICEXQuoteBasis::Unspecified;
		switch(csVal[0]) {
		case 'R': ret = enumeration::MICEXQuoteBasis::forUnit; break;
		case 'A': ret = enumeration::MICEXQuoteBasis::forFaceValue; break;
		case 'F': ret = enumeration::MICEXQuoteBasis::percentageOfFaceValue; break;
		default: break;
		}

		return ret;
	}

	enumeration::MICEXPutCall::Values MapMICEXPutCall(const CString& csVal)
	{
		enumeration::MICEXPutCall::Values ret = enumeration::MICEXPutCall::Unspecified;
		switch(csVal[0]) {
		case 'F': ret = enumeration::MICEXPutCall::futures; break;
		case 'C': ret = enumeration::MICEXPutCall::optionCall; break;
		case 'P': ret = enumeration::MICEXPutCall::optionPut; break;
		default: break;
		}

		return ret;
	}

	enumeration::MICEXDeliveryType::Values MapMICEXDeliveryType(const CString& csVal)
	{
		enumeration::MICEXDeliveryType::Values ret = enumeration::MICEXDeliveryType::Unspecified;
		switch(csVal[0]) {
		case 'D': ret = enumeration::MICEXDeliveryType::delivered; break;
		case 'C': ret = enumeration::MICEXDeliveryType::calculated; break;
		default: break;
		}

		return ret;
	}
};

CTibrvConnector* CTibrvConnector::s_pTibrvInstance = NULL;

CTibrvConnector::CTibrvConnector(void)
	: CFEEDThread("Tibrv")
{
	m_bInitialized = false;
}

CTibrvConnector::~CTibrvConnector(void)
{
}

BOOL CTibrvConnector::RunBegin()
{
	return Initialize();
}

void CTibrvConnector::RunEnd()
{
	RunStep();
	Destroy();
	m_bInitialized = false;
}

void CTibrvConnector::RunStep()
{
	MSG msg;
	while(PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) 
	{
		if (msg.message == WM_QUIT)
			return;

		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	CMICEXChangesInfo* pChangesInfo = NULL;
	while(m_changesQueue.get(pChangesInfo))
	{
		ProcessChangesInfo(pChangesInfo);

		if(::WaitForSingleObject(m_hStop, 0) == WAIT_OBJECT_0)
			break;
	}
}

bool CTibrvConnector::Initialize()
{
	LOG_INFO(SECTION(lTibrv), "Initializing tibrv");

	CFEEDConfigTIBRV* pTibrvCmCfg = CFEEDConfig::s_pConfigInstance->m_pCfgTibrvCm;
	
	if(!pTibrvCmCfg->IsEnabled()) {
		LOG_INFO(SECTION(lTibrv), "Tibrv certified delivery is disabled");
		return true;
	}

	Tibrv::open();

	LOG_INFO(SECTION(lTibrv), "Tibrv version %s", Tibrv::version());
	TibrvStatus status;
	// create transport
	LOG_INFO(SECTION(lTibrv), "Creating Tibrv cm transport (%s)", (LPCSTR)pTibrvCmCfg->ToString());
	status = transport.create(
		pTibrvCmCfg->m_csServiceName.IsEmpty() ? NULL : (LPCSTR)pTibrvCmCfg->m_csServiceName,
		pTibrvCmCfg->m_csNetworkName.IsEmpty() ? NULL : (LPCSTR)pTibrvCmCfg->m_csNetworkName,
		pTibrvCmCfg->m_csDaemonName.IsEmpty() ? NULL : (LPCSTR)pTibrvCmCfg->m_csDaemonName);

	if(status != TIBRV_OK) {
		LOG_ERROR(SECTION(lTibrv), "Unable to create Tibrv transport: [%d] %s", (int)status, status.getText());
		return false;
	}
	string sTransportDescr;
	sTransportDescr = CFEEDConfig::s_pConfigInstance->m_csExchange;
	sTransportDescr.append("_FEED");
	transport.setDescription(sTransportDescr.c_str());

	// create cm transport
	status = cmTransport.create(
		&transport,
		pTibrvCmCfg->m_csCmName.IsEmpty() ? NULL : (LPCSTR)pTibrvCmCfg->m_csCmName,
		pTibrvCmCfg->m_bRequestOld ? TIBRV_TRUE : TIBRV_FALSE,
		pTibrvCmCfg->m_csLedgerFile.IsEmpty() ? NULL : (LPCSTR)pTibrvCmCfg->m_csLedgerFile,
		pTibrvCmCfg->m_bSyncLedger ? TIBRV_TRUE : TIBRV_FALSE,
		NULL);

	if(status != TIBRV_OK) {
		LOG_ERROR(SECTION(lTibrv), "Unable to create Tibrv cm transport: [%d] %s", (int)status, status.getText());
		return false;
	}
	string sCmTransportDescr;
	sCmTransportDescr = CFEEDConfig::s_pConfigInstance->m_csExchange;
	sCmTransportDescr.append("_FEED_CM_TRANSPORT");
	cmTransport.setDescription(sCmTransportDescr.c_str());

	m_bInitialized = true;
	return true;
}

void CTibrvConnector::Destroy()
{
	LOG_DEBUG(SECTION(lTibrv), "Destroying tibrv objects");
	cmTransport.destroy();
	transport.destroy();
	Tibrv::close();
	LOG_DEBUG(SECTION(lTibrv), "Tibrv objects destroyed");
}

bool CTibrvConnector::ProcessChangesInfo( CMICEXChangesInfo* pChangesInfo )
{
	if (pChangesInfo->m_eChangesType == changesTable) {
		PublishTable(pChangesInfo);
	} else if (pChangesInfo->m_eChangesType == changesVolume) {
		PublishVolume(pChangesInfo);
	} else if (pChangesInfo->m_eChangesType == changesAlive) {
		PublishAlive(pChangesInfo);
	}

	delete pChangesInfo;

	return true;
}

bool CTibrvConnector::AddUpdate( CMICEXChangesInfo* pChangesInfo )
{
	if(!IsInitialized())
		return false;
	
	CMICEXChangesInfo *pChInfo = new CMICEXChangesInfo(*pChangesInfo);

	if(pChInfo->m_bIsPriority)
		m_changesQueue.insert(pChInfo);
	else
		m_changesQueue.append(pChInfo);

	return true;
}

void CTibrvConnector::PublishTable(CMICEXChangesInfo* pChangesInfo)
{
	if(pChangesInfo->m_pTable != NULL)
	{
		CMICEXTable &tbl = *pChangesInfo->m_pTable;
		CMICEXSection *pSection = pChangesInfo->m_pSection;
		enumeration::MICEXSection::Values sectionType = enumeration::MICEXSection::Unspecified;
		if(pSection != NULL && pSection->m_pConfig != NULL) {
			sectionType = pSection->m_pConfig->m_sectionType;
		}

		if(tbl.m_csTableName.CompareNoCase(TABLE_TRADES) == 0) {
			if(pChangesInfo->m_pRowData != NULL) {
				PublishTrade(sectionType, tbl, pChangesInfo->m_pRowData);
			}
		} else if(tbl.m_csTableName.CompareNoCase(TABLE_USTRADES) == 0) {
			if(pChangesInfo->m_pRowData != NULL) {
				PublishUSTrades(sectionType, tbl, pChangesInfo->m_pRowData);
			}
		} else if(tbl.m_csTableName.CompareNoCase(TABLE_SECURITIES) == 0) {
			if(pChangesInfo->m_pRowData != NULL) {
				PublishSecurities(sectionType, tbl, pChangesInfo->m_pRowData);
			}
		}
	}
}

void CTibrvConnector::PublishVolume(CMICEXChangesInfo* pChangesInfo)
{
	if(pChangesInfo->m_eChangesType != changesVolume)
		return;

	CMICEXSection *pSection = pChangesInfo->m_pSection;
	enumeration::MICEXSection::Values sectionType = enumeration::MICEXSection::Unspecified;
	if(pSection != NULL && pSection->m_pConfig != NULL) {
		sectionType = pSection->m_pConfig->m_sectionType;
	}
	
	MICEXVolume volume;
	TibrvMsg msg;

	volume.SetVolume(FOMessageHelper::double2decimal(pChangesInfo->m_dVolume));
	volume.SetSection(sectionType);
	volume.SetTimestamp(FOMessageHelper::makeTimeNow());
	
	EncodeMessage<MICEXVolume>(volume, msg);
	msg.setSendSubject(MakeVolumeSubject(sectionType).c_str());

	SendMessageToTibrv(msg, false);
}

void CTibrvConnector::PublishAlive(CMICEXChangesInfo* pChangesInfo)
{

}

void CTibrvConnector::RegisterAnticipatedListener( const CString& subject )
{
	const CmListeners &cmListeners = CFEEDConfig::s_pConfigInstance->m_pCfgTibrvCm->m_cmListeners;
	if(m_Subjects.find(subject) == m_Subjects.end())
	{
		m_Subjects.insert(subject);

		CmListeners::const_iterator cit = NULL;
		for(cit = cmListeners.begin(); cit != cmListeners.end(); ++cit)
		{
			const CString strCmName(*cit);
			TibrvStatus status;
			status = cmTransport.addListener((LPCSTR)strCmName, (LPCSTR)subject);
			if(status == TIBRV_OK)
				LOG_DEBUG(SECTION(lTibrv), "Anticipated listener '%s' for message with subject '%s' was registered",
					(LPCSTR)strCmName, (LPCSTR)subject);
			else
				LOG_ERROR(SECTION(lTibrv), "Anticipated listener '%s' for message with subject '%s' was not registered: [%d] %s",
					(LPCSTR)strCmName, (LPCSTR)subject, (int)status, status.getText());
		}
	} 
	return;
}

bool CTibrvConnector::SendMessageToTibrv( TibrvMsg& msg, bool bCertified /*= true*/ )
{
	const char* pSendSubject = NULL;
	msg.getSendSubject(pSendSubject);
	TibrvStatus status;
	if(bCertified) {
		RegisterAnticipatedListener(pSendSubject);
		status = cmTransport.send(msg);
	} else {
		status = transport.send(msg);
	}

	if(status != TIBRV_OK) {
		LOG_ERROR(SECTION(lTibrv), "Unable to publish message %s: [%d] %s",
			pSendSubject, (int)status, status.getText());
	} else {
		const char *pMessageText = NULL;
		msg.convertToString(pMessageText);
		LOG_TRACE(SECTION(lTibrv), "subject: %s, message=%s", pSendSubject, pMessageText);
	}
	return status == TIBRV_OK;
}

CString GetTableValue(CMICEXTable& tbl, LPBYTE pDataBuf, TradesTable::TradesField fieldId)
{
	CString csVal;
	int nFieldNum = tbl.GetFieldNumByName(TradesTable::CTradesFields::gTradeMap[fieldId]);
	tbl.FieldValToString(nFieldNum, pDataBuf, csVal);
	return csVal;
}

void CTibrvConnector::PublishTrade( enumeration::MICEXSection::Values sectionType, CMICEXTable& tbl, LPBYTE pRowData )
{
	if(sectionType == enumeration::MICEXSection::Unspecified)
		return;

	MICEXTrade trade;
	CString csVal;
	CString csSecBoard;
	CString csSecCode;
	decimal128 decVal;
	
	using namespace TradesTable;

	csVal = GetTableValue(tbl, pRowData, tradeId);
	trade.SetTradeNo(_atoi64((LPCSTR)csVal));	

	csVal = GetTableValue(tbl, pRowData, orderId);
	trade.SetOrderNo(_atoi64((LPCSTR)csVal));
	
	csVal = GetTableValue(tbl, pRowData, tradeTime);
	trade.SetTradeTime(FOMessageHelper::string2time((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, buySell);
	trade.SetBuySell(FOMessageHelper::MapBuySell(csVal));

	trade.SetBrokerRef((LPCSTR)GetTableValue(tbl, pRowData, brokerRef));
	trade.SetUserId((LPCSTR)GetTableValue(tbl, pRowData, userName));
	trade.SetFirmId((LPCSTR)GetTableValue(tbl, pRowData, firmId));
	trade.SetCpFirmId((LPCSTR)GetTableValue(tbl, pRowData, cpFirmId));
	
	if(sectionType == enumeration::MICEXSection::EQ || sectionType == enumeration::MICEXSection::GKO)
		trade.SetAccount((LPCSTR)GetTableValue(tbl, pRowData, account));

	if(sectionType == enumeration::MICEXSection::FO)
		trade.SetAccount((LPCSTR)GetTableValue(tbl, pRowData, trdAccount));

	csSecBoard = GetTableValue(tbl, pRowData, boardID);
	trade.SetSecBoard((LPCSTR)csSecBoard);
	csSecCode = GetTableValue(tbl, pRowData, secCode);
	trade.SetSecCode((LPCSTR)csSecCode);

	csVal = GetTableValue(tbl, pRowData, price);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetPrice(decVal);

	csVal = GetTableValue(tbl, pRowData, quantity);
	trade.SetQuantity(_atoi64((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, value);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetValue(decVal);

	trade.SetCurrency((LPCSTR)CFEEDConfig::s_pConfigInstance->m_csExchangeCurrency);

	csVal = GetTableValue(tbl, pRowData, period);
	trade.SetPeriod(FOMessageHelper::MapTradingPeriod(csVal));

	csVal = GetTableValue(tbl, pRowData, tradeType);
	trade.SetTradeType(FOMessageHelper::MapMICEXTradeType(csVal));

	trade.SetExtRef((LPCSTR)GetTableValue(tbl, pRowData, extRef));

	if(sectionType == enumeration::MICEXSection::EQ || sectionType == enumeration::MICEXSection::GKO)
	{
		csVal = GetTableValue(tbl, pRowData, settleDate);
		trade.SetSettleDate(FOMessageHelper::string2date((LPCSTR)csVal));

		csVal = GetTableValue(tbl, pRowData, accruedInt);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetAccruedInt(decVal);

		csVal = GetTableValue(tbl, pRowData, yield);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetYield(decVal);


		csVal = GetTableValue(tbl, pRowData, price2);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetPrice2(decVal);

		trade.SetSettleCode((LPCSTR)GetTableValue(tbl, pRowData, settleCode));

		csVal = GetTableValue(tbl, pRowData, commission);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetComission(decVal);

		csVal = GetTableValue(tbl, pRowData, repoRate);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetRepoRate(decVal);

		csVal = GetTableValue(tbl, pRowData, accrued2);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetAccrued2(decVal);

		csVal = GetTableValue(tbl, pRowData, repoValue);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetRepoValue(decVal);

		csVal = GetTableValue(tbl, pRowData, repo2Value);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetRepo2Value(decVal);

		csVal = GetTableValue(tbl, pRowData, repoTerm);
		trade.SetRepoTerm(atoi((LPCSTR)csVal));

		csVal = GetTableValue(tbl, pRowData, startDiscount);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetStartDiscount(decVal);

		csVal = GetTableValue(tbl, pRowData, lowerDiscount);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetLowerDiscount(decVal);

		csVal = GetTableValue(tbl, pRowData, upperDiscount);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetUpperDiscount(decVal);

		csVal = GetTableValue(tbl, pRowData, blockSecurities);
		trade.SetBlockSecurities(FOMessageHelper::MapYesNo(csVal));
	}

	if(sectionType == enumeration::MICEXSection::EQ)
	{
		csVal = GetTableValue(tbl, pRowData, clearingCenterComm);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetClearingCenterComm(decVal);

		csVal = GetTableValue(tbl, pRowData, exchangeComm);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetExchangeComm(decVal);

		csVal = GetTableValue(tbl, pRowData, tradingSystemComm);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		trade.SetTradingSystemComm(decVal);

		trade.SetClientCode((LPCSTR)GetTableValue(tbl, pRowData, clientCode));

		CString csSecType = tbl.m_pSchema->GetSECURITIESField(csSecBoard, csSecCode, FIELD_SECTYPE);
		trade.SetSecType(FOMessageHelper::MapSecType(csSecType));
	}
	
	trade.SetSection(sectionType);

	trade.SetFeedDatetime(FOMessageHelper::makeTimeNow());

	TibrvMsg msg;
	EncodeMessage<MICEXTrade>(trade, msg);
	CFEEDConfigTIBRV* pTibrvCmCfg = CFEEDConfig::s_pConfigInstance->m_pCfgTibrvCm;
	msg.setSendSubject((LPCSTR)pTibrvCmCfg->m_sSubjectTrade);
	
	SendMessageToTibrv(msg);
}

void CTibrvConnector::PublishUSTrades( enumeration::MICEXSection::Values sectionType, CMICEXTable& tbl, LPBYTE pRowData )
{
	if(sectionType != enumeration::MICEXSection::EQ && sectionType != enumeration::MICEXSection::GKO)
		return;

	MICEXUSTrade trade;
	CString csVal;
	CString csSecBoard;
	CString csSecCode;
	decimal128 decVal;

	using namespace TradesTable;

	csVal = GetTableValue(tbl, pRowData, tradeId);
	trade.SetTradeNo(_atoi64((LPCSTR)csVal));	

	csVal = GetTableValue(tbl, pRowData, tradeDate);
	trade.SetTradeDate(FOMessageHelper::string2date((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, settleDate);
	trade.SetSettleDate(FOMessageHelper::string2date((LPCSTR)csVal));
	csVal = GetTableValue(tbl, pRowData, settleTime);
	trade.SetSettleTime(FOMessageHelper::string2DateTime((LPCSTR)csVal));

	csSecBoard = GetTableValue(tbl, pRowData, boardID);
	trade.SetSecBoard((LPCSTR)csSecBoard);
	csSecCode = GetTableValue(tbl, pRowData, secCode);
	trade.SetSecCode((LPCSTR)csSecCode);

	csVal = GetTableValue(tbl, pRowData, buySell);
	trade.SetSide(FOMessageHelper::MapBuySell(csVal));

	csVal = GetTableValue(tbl, pRowData, price);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetPrice(decVal);

	csVal = GetTableValue(tbl, pRowData, quantity);
	trade.SetQuantity(_atoi64((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, value);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetValue(decVal);

	trade.SetCurrency((LPCSTR)CFEEDConfig::s_pConfigInstance->m_csExchangeCurrency);

	trade.SetSettleCode((LPCSTR)GetTableValue(tbl, pRowData, settleCode));

	trade.SetFirmId((LPCSTR)GetTableValue(tbl, pRowData, firmId));
	trade.SetTrdAccId((LPCSTR)GetTableValue(tbl, pRowData, trdAccount));
	trade.SetCpFirmId((LPCSTR)GetTableValue(tbl, pRowData, cpFirmId));
	trade.SetCpTrdAccId((LPCSTR)GetTableValue(tbl, pRowData, accountCp));

	csVal = GetTableValue(tbl, pRowData, reportNo);
	trade.SetReportNo(_atoi64((LPCSTR)csVal));	

	csVal = GetTableValue(tbl, pRowData, cpReportNo);
	trade.SetCpReportNo(_atoi64((LPCSTR)csVal));	

	csVal = GetTableValue(tbl, pRowData, usTradeStatus);
	trade.SetStatus(FOMessageHelper::MapUSTradeStatus(csVal));

	csVal = GetTableValue(tbl, pRowData, accruedInt);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetAccruedInt(decVal);

	trade.SetBrokerRef((LPCSTR)GetTableValue(tbl, pRowData, brokerRef));

	csVal = GetTableValue(tbl, pRowData, repoTradeNo);
	trade.SetRepoTradeNo(_atoi64((LPCSTR)csVal));	

	csVal = GetTableValue(tbl, pRowData, price1);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetPrice1(decVal);

	csVal = GetTableValue(tbl, pRowData, repoRate);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetRepoRate(decVal);

	csVal = GetTableValue(tbl, pRowData, price2);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetPrice2(decVal);

	csVal = GetTableValue(tbl, pRowData, commission);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetComission(decVal);

	csVal = GetTableValue(tbl, pRowData, balance);
	trade.SetBalance(_atoi64((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, amount);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetAmount(decVal);

	csVal = GetTableValue(tbl, pRowData, repoValue);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetRepoValue(decVal);

	csVal = GetTableValue(tbl, pRowData, repoTerm);
	trade.SetRepoTerm(atoi((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, repo2Value);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetRepo2Value(decVal);

	csVal = GetTableValue(tbl, pRowData, returnValue);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetReturnValue(decVal);

	csVal = GetTableValue(tbl, pRowData, discount);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetDiscount(decVal);

	csVal = GetTableValue(tbl, pRowData, lowerDiscount);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetLowerDiscount(decVal);

	csVal = GetTableValue(tbl, pRowData, upperDiscount);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetUpperDiscount(decVal);

	csVal = GetTableValue(tbl, pRowData, blockSecurities);
	trade.SetBlockSecurities(FOMessageHelper::MapYesNo(csVal));

	csVal = GetTableValue(tbl, pRowData, urgencyFlag);
	trade.SetUrgencyFlag(FOMessageHelper::MapYesNo(csVal));

	csVal = GetTableValue(tbl, pRowData, type);
	trade.SetType(FOMessageHelper::MapUSTradeType(csVal));

	csVal = GetTableValue(tbl, pRowData, operationType);
	trade.SetOperationType(FOMessageHelper::MapOperationType(csVal));

	csVal = GetTableValue(tbl, pRowData, expectedDiscount);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetExpectedDiscount(decVal);

	csVal = GetTableValue(tbl, pRowData, expectedQuantity);
	trade.SetExpectedQuantity(_atoi64((LPCSTR)csVal));

	csVal = GetTableValue(tbl, pRowData, expectedRepoValue);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetExpectedRepoValue(decVal);

	csVal = GetTableValue(tbl, pRowData, expectedRepo2Value);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	trade.SetExpectedRepo2Value(decVal);

	if(sectionType == enumeration::MICEXSection::EQ)
	{
		trade.SetClientCode((LPCSTR)GetTableValue(tbl, pRowData, clientCode));

		csVal = GetTableValue(tbl, pRowData, nextDaySettle);
		trade.SetNextDaySettle(FOMessageHelper::MapYesNo(csVal));

		csVal = GetTableValue(tbl, pRowData, orderId);
		trade.SetOrderNo(_atoi64((LPCSTR)csVal));

		csVal = GetTableValue(tbl, pRowData, repoTradeDate);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			trade.SetRepoTradeTradeDate(FOMessageHelper::string2date((LPCSTR)csVal));

		trade.SetExtRef((LPCSTR)GetTableValue(tbl, pRowData, extRef));

		CString csSecType = tbl.m_pSchema->GetSECURITIESField(csSecBoard, csSecCode, FIELD_SECTYPE);
		trade.SetSecType(FOMessageHelper::MapSecType(csSecType));
	}

	trade.SetSection(sectionType);

	trade.SetFeedDatetime(FOMessageHelper::makeTimeNow());

	TibrvMsg msg;
	EncodeMessage<MICEXUSTrade>(trade, msg);
	CFEEDConfigTIBRV* pTibrvCmCfg = CFEEDConfig::s_pConfigInstance->m_pCfgTibrvCm;
	msg.setSendSubject((LPCSTR)pTibrvCmCfg->m_sSubjectUSTrade);

	SendMessageToTibrv(msg);
}

CString GetSecTableValue(CMICEXTable& tbl, LPBYTE pDataBuf, SecuritiesTable::SecuritiesField fieldId)
{
	CString csVal;
	int nFieldNum = tbl.GetFieldNumByName(SecuritiesTable::CSecuritiesFields::gSecMap[fieldId]);
	tbl.FieldValToString(nFieldNum, pDataBuf, csVal);
	return csVal;
}

void CTibrvConnector::PublishSecurities( enumeration::MICEXSection::Values sectionType, CMICEXTable& tbl, LPBYTE pRowData ) {
	if(sectionType == enumeration::MICEXSection::Unspecified)
		return;

	using namespace SecuritiesTable;

	MICEXSecurity sec;
	CString csVal;
	string sSecCode;
	decimal128 decVal;

	sec.SetSecBoard((LPCSTR)GetSecTableValue(tbl, pRowData, secBoard));
	
	csVal = GetSecTableValue(tbl, pRowData, secCode);
	sec.SetSecCode((LPCSTR)csVal);
	sSecCode = (LPCSTR)csVal;

	sec.SetSecName((LPCSTR)GetSecTableValue(tbl, pRowData, secName));
	sec.SetRemarks((LPCSTR)GetSecTableValue(tbl, pRowData, remarks));
	sec.SetShortName((LPCSTR)GetSecTableValue(tbl, pRowData, shortName));
	
	csVal = GetSecTableValue(tbl, pRowData, status);
	sec.SetStatus(FOMessageHelper::MapMICEXSecStatus(csVal));

	csVal = GetSecTableValue(tbl, pRowData, tradingStatus);
	sec.SetTradingStatus(FOMessageHelper::MapMICEXTradingStatus(csVal));

	csVal = GetSecTableValue(tbl, pRowData, minStep);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetMinStep(decVal);
	
	csVal = GetSecTableValue(tbl, pRowData, prevDate);
	if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
		sec.SetPrevDate(FOMessageHelper::string2date((LPCSTR)csVal));
	
	csVal = GetSecTableValue(tbl, pRowData, decimals);
	sec.SetDecimals(atoi((LPCSTR)csVal));
	
	sec.SetLatName((LPCSTR)GetSecTableValue(tbl, pRowData, latName));

	csVal = GetSecTableValue(tbl, pRowData, bid);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetBid(decVal);

	csVal = GetSecTableValue(tbl, pRowData, bidDepth);
	sec.SetBidDepth(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, bidDepthT);
	sec.SetBidDepthT(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, numBids);
	sec.SetNumBids(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, offer);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);

	sec.SetOffer(decVal);
	csVal = GetSecTableValue(tbl, pRowData, offerDepth);
	sec.SetOfferDepth(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, offerDepthT);
	sec.SetOfferDepthT(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, numOffers);
	sec.SetNumOffers(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, open);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetOpen(decVal);

	csVal = GetSecTableValue(tbl, pRowData, high);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetHigh(decVal);

	csVal = GetSecTableValue(tbl, pRowData, low);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetLow(decVal);

	csVal = GetSecTableValue(tbl, pRowData, last);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetLast(decVal);
	
	csVal = GetSecTableValue(tbl, pRowData, fTime);
	sec.SetTime(FOMessageHelper::string2time((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, volToday);
	sec.SetVolToday(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, valToday);
	sec.SetValToday(_atoi64((LPCSTR)csVal));

	csVal = GetSecTableValue(tbl, pRowData, value);
	FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
	sec.SetValue(decVal);

	csVal = GetSecTableValue(tbl, pRowData, numTrades);
	sec.SetNumTrades(_atoi64((LPCSTR)csVal));

	if(sectionType == enumeration::MICEXSection::EQ || sectionType == enumeration::MICEXSection::GKO) {
		sec.SetMarketCode((LPCSTR)GetSecTableValue(tbl, pRowData, marketCode));
		sec.SetInstrID((LPCSTR)GetSecTableValue(tbl, pRowData, instrID));

		csVal = GetSecTableValue(tbl, pRowData, lotSize);
		sec.SetLotSize(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, faceValue);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetFaceValue(decVal);

		sec.SetFaceUnit((LPCSTR)GetSecTableValue(tbl, pRowData, faceUnit));

		csVal = GetSecTableValue(tbl, pRowData, prevPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPrevPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, yield);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetYield(decVal);

		csVal = GetSecTableValue(tbl, pRowData, accruedInt);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetAccruedInt(decVal);

		csVal = GetSecTableValue(tbl, pRowData, matDate);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			sec.SetMatDate(FOMessageHelper::string2date((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, couponValue);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetCouponValue(decVal);

		csVal = GetSecTableValue(tbl, pRowData, couponPeriod);
		sec.SetFaceValue(atoi((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, nextCoupon);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			sec.SetNextCoupon(FOMessageHelper::string2date((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, issueSize);
		sec.SetIssueSize(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, prevWAPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPrevWAPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, yieldAtPrevWAPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetYieldAtPrevWAPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, repo2Price);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetRepo2Price(decVal);

		sec.SetCurrencyID((LPCSTR)GetSecTableValue(tbl, pRowData, currencyID));

		csVal = GetSecTableValue(tbl, pRowData, buyBackPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetBuyBackPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, buyBackDate);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			sec.SetBuyBackDate(FOMessageHelper::string2date((LPCSTR)csVal));

		sec.SetAgentID((LPCSTR)GetSecTableValue(tbl, pRowData, agentID));
		sec.SetIsin((LPCSTR)GetSecTableValue(tbl, pRowData, isin));
		sec.SetRegNumber((LPCSTR)GetSecTableValue(tbl, pRowData, regNumber));

		csVal = GetSecTableValue(tbl, pRowData, change);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetChange(decVal);

		csVal = GetSecTableValue(tbl, pRowData, qty);
		sec.SetQuantity(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, wAPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetWAPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, highBid);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetHighBid(decVal);

		csVal = GetSecTableValue(tbl, pRowData, lowOffer);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLowOffer(decVal);

		csVal = GetSecTableValue(tbl, pRowData, yieldAtWAPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetYieldAtWAPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, priceMinusPrevWAPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPriceMinusPrevWAPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, closePrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetClosePrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, closeYield);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetCloseYield(decVal);

		csVal = GetSecTableValue(tbl, pRowData, lastBid);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLastBid(decVal);

		csVal = GetSecTableValue(tbl, pRowData, lastOffer);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLastOffer(decVal);

		sec.SetLastSettleCode((LPCSTR)GetSecTableValue(tbl, pRowData, lastSettleCode));
		csVal = GetSecTableValue(tbl, pRowData, marketPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetMarketPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, marketPriceToday);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetMarketPriceToday(decVal);

		csVal = GetSecTableValue(tbl, pRowData, duration);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetDuration(decVal);

		sec.SetSettleCode((LPCSTR)GetSecTableValue(tbl, pRowData, settleCode));

		csVal = GetSecTableValue(tbl, pRowData, marketPrice2);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetMarketPrice2(decVal);

		csVal = GetSecTableValue(tbl, pRowData, admittedQuote);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetAdmittedQuote(decVal);

		csVal = GetSecTableValue(tbl, pRowData, primaryDist);
		sec.SetPrimaryDist(FOMessageHelper::MapYesNo(csVal));

		csVal = GetSecTableValue(tbl, pRowData, quoteBasis);
		sec.SetQuoteBasis(FOMessageHelper::MapMICEXQuoteBasis(csVal));

	}

	if(sectionType == enumeration::MICEXSection::EQ) {
		csVal = GetSecTableValue(tbl, pRowData, prevLegalClosePrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPrevLegalClosePrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, prevAdmittedQuote);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPrevAdmittedQuote(decVal);

		csVal = GetSecTableValue(tbl, pRowData, secType);
		sec.SetSecType(FOMessageHelper::MapSecType(csVal));

		csVal = GetSecTableValue(tbl, pRowData, lOpenPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLOpenPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, lCurrentPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLCurrentPrice(decVal);
	
		csVal = GetSecTableValue(tbl, pRowData, openPeriodPrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetOpenPeriodPrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, lClosePrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLClosePrice(decVal);
	}

	if(sectionType == enumeration::MICEXSection::FO) {
		csVal = GetSecTableValue(tbl, pRowData, quantity);
		sec.SetQuantity(_atoi64((LPCSTR)csVal));
		
		sec.SetFoSecTypeID((LPCSTR)GetSecTableValue(tbl, pRowData, foSecTypeID));

		csVal = GetSecTableValue(tbl, pRowData, firstTradeDate);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			sec.SetFirstTradeDate(FOMessageHelper::string2date((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, lastTradeDate);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			sec.SetLastTradeDate(FOMessageHelper::string2date((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, lastDelDate);
		if(!csVal.IsEmpty() && csVal.Compare("00000000") != 0)
			sec.SetLastDelDate(FOMessageHelper::string2date((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, daysToMaturity);
		sec.SetDaysToMaturity(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, prevSettlePrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPrevSettlePrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, priceMVTLimit);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPriceMVTLimit(decVal);

		csVal = GetSecTableValue(tbl, pRowData, maxOutVolume);
		sec.SetMaxOutVolume(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, strike);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetStrike(decVal);

		sec.SetUnderlyingSecID((LPCSTR)GetSecTableValue(tbl, pRowData, underlyingSecID));

		csVal = GetSecTableValue(tbl, pRowData, priceValue);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPriceValue(decVal);

		csVal = GetSecTableValue(tbl, pRowData, mktShareLimit);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetMktShareLimit(decVal);

		csVal = GetSecTableValue(tbl, pRowData, mktShareThreshOld);
		sec.SetMktShareThreshOld(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, lastToPrevSTLPrc);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetLastToPrevSTLPrc(decVal);

		csVal = GetSecTableValue(tbl, pRowData, settlePrice);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetSettlePrice(decVal);

		csVal = GetSecTableValue(tbl, pRowData, negValToday);
		sec.SetNegValToday(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, negNumTrades);
		sec.SetNegNumTrades(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, priceMax);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPriceMax(decVal);

		csVal = GetSecTableValue(tbl, pRowData, priceMin);
		FOMessageHelper::string2decimal((LPCSTR)csVal, decVal);
		sec.SetPriceMin(decVal);

		csVal = GetSecTableValue(tbl, pRowData, numContracts);
		sec.SetNumContracts(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, openPositionChange);
		sec.SetOpenPositionChange(_atoi64((LPCSTR)csVal));

		csVal = GetSecTableValue(tbl, pRowData, putCall);
		sec.SetPutCall(FOMessageHelper::MapMICEXPutCall(csVal));

		csVal = GetSecTableValue(tbl, pRowData, deliveryType);
		sec.SetDeliveryType(FOMessageHelper::MapMICEXDeliveryType(csVal));
	}

	sec.SetTimestamp(FOMessageHelper::makeTimeNow());
	sec.SetSection(sectionType);

	TibrvMsg msg;
	EncodeMessage<MICEXSecurity>(sec, msg);
	msg.setSendSubject(MakeSecuritiesSubject(sectionType, sSecCode).c_str());

	SendMessageToTibrv(msg, false);
}

std::string CTibrvConnector::MapSection(enumeration::MICEXSection::Values section)
{
	string sRet("");
	switch(section) {
	case enumeration::MICEXSection::EQ: sRet = "EQ"; break;
	case enumeration::MICEXSection::GKO: sRet = "GKO"; break;
	case enumeration::MICEXSection::FO: sRet = "FO"; break;
	default: break;
	}
	return sRet;
}

std::string CTibrvConnector::MakeSecuritiesSubject(enumeration::MICEXSection::Values section, const std::string& secName)
{
	string subject = "";
	CFEEDConfigTIBRV* pTibrvCmCfg = CFEEDConfig::s_pConfigInstance->m_pCfgTibrvCm;
	if(pTibrvCmCfg != NULL)
		subject = (LPCSTR)pTibrvCmCfg->m_sSubjectSecurities;
	subject.append(".");
	subject.append(MapSection(section));
	subject.append(".");
	subject.append(secName);

	return subject;
}

std::string CTibrvConnector::MakeVolumeSubject(enumeration::MICEXSection::Values section)
{
	string subject = "";
	CFEEDConfigTIBRV* pTibrvCmCfg = CFEEDConfig::s_pConfigInstance->m_pCfgTibrvCm;
	if(pTibrvCmCfg != NULL)
		subject = (LPCSTR)pTibrvCmCfg->m_sSubjectVolume;
	subject.append(".");
	subject.append(MapSection(section));
	return subject;
}
