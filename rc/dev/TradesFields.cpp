#include "stdafx.h"
#include "TradesFields.h"

namespace TradesTable
{

CTradesFields CTradesFields::gTradeMap;

CTradesFields::CTradesFields()
{
	fieldsMap[tradeId]				= "TRADENO";
	fieldsMap[orderId]				= "ORDERNO";
	fieldsMap[tradeTime]			= "TRADETIME";
	fieldsMap[buySell]				= "BUYSELL";
	fieldsMap[brokerRef]			= "BROKERREF";
	fieldsMap[userName]				= "USERID";
	fieldsMap[firmId]				= "FIRMID";
	fieldsMap[cpFirmId]				= "CPFIRMID";
	fieldsMap[account]				= "ACCOUNT";
	fieldsMap[boardID]				= "SECBOARD";          
	fieldsMap[secCode]				= "SECCODE";           
	fieldsMap[price]				= "PRICE";             
	fieldsMap[quantity]				= "QUANTITY";          
	fieldsMap[value]				= "VALUE";             
	fieldsMap[settleDate]			= "SETTLEDATE";        
	fieldsMap[accruedInt]			= "ACCRUEDINT";        
	fieldsMap[yield]				= "YIELD";             
	fieldsMap[period]				= "PERIOD";            
	fieldsMap[price2]				= "PRICE2";            
	fieldsMap[settleCode]			= "SETTLECODE";        
	fieldsMap[tradeType]			= "TRADETYPE";         
	fieldsMap[extRef]				= "EXTREF";            
	fieldsMap[commission]			= "COMMISSION";        
	fieldsMap[repoRate]				= "REPORATE";          
	fieldsMap[accrued2]				= "ACCRUED2";          
	fieldsMap[repoValue]			= "REPOVALUE";         
	fieldsMap[repo2Value]			= "REPO2VALUE";        
	fieldsMap[repoTerm]				= "REPOTERM";          
	fieldsMap[startDiscount]		= "STARTDISCOUNT";     
	fieldsMap[lowerDiscount]		= "LOWERDISCOUNT";     
	fieldsMap[upperDiscount]		= "UPPERDISCOUNT";     
	fieldsMap[blockSecurities]		= "BLOCKSECURITIES";   
	fieldsMap[clearingCenterComm]	= "CLEARINGCENTERCOMM";
	fieldsMap[exchangeComm]			= "EXCHANGECOMM";      
	fieldsMap[tradingSystemComm]	= "TRADINGSYSTEMCOMM"; 
	fieldsMap[clientCode]			= "CLIENTCODE";
	fieldsMap[tradeDate]			= "TRADEDATE";
	fieldsMap[settleTime]			= "SETTLETIME";
	fieldsMap[trdAccount]			= "TRDACCID";
	fieldsMap[accountCp]			= "CPTRDACCID";
	fieldsMap[reportNo]				= "REPORTNO";
	fieldsMap[cpReportNo]			= "CPREPORTNO";
	fieldsMap[usTradeStatus]		= "STATUS";
	fieldsMap[repoTradeNo]			= "REPOTRADENO";
	fieldsMap[price1]				= "PRICE1";
	fieldsMap[balance]				= "BALANCE";
	fieldsMap[amount]				= "AMOUNT";
	fieldsMap[returnValue]			= "RETURNVALUE";
	fieldsMap[discount]				= "DISCOUNT";
	fieldsMap[type]					= "TYPE";
	fieldsMap[operationType]		= "OPERATIONTYPE";
	fieldsMap[expectedDiscount]		= "EXPECTEDISCOUNT";
	fieldsMap[expectedQuantity]		= "EXPECTEDQUANTITY";
	fieldsMap[expectedRepoValue]	= "EXPECTEDREPOVALUE";
	fieldsMap[expectedRepo2Value]	= "EXPECTEDREPO2VALUE";
	fieldsMap[expectedReturnValue]	= "EXPECTEDRETURNVALUE";
	fieldsMap[repoTradeDate]		= "REPOTRADETRADEDATE";
	fieldsMap[urgencyFlag]			= "URGENCYFLAG";
	fieldsMap[nextDaySettle]		= "NEXTDAYSETTLE";
}

CTradesFields::~CTradesFields()
{
	fieldsMap.clear();
}

const char* CTradesFields::operator[](TradesField key)
{
	TradesFieldsMap::iterator it = fieldsMap.find(key);
	if(it != fieldsMap.end())
		return ((*it).second).c_str();
	return "";
}
};
