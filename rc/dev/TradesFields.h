#ifndef _TRADES_FIELDS_IDENTIFIERS_H_
#define _TRADES_FIELDS_IDENTIFIERS_H_

#include <map>
#include <string>

using namespace std;

namespace TradesTable
{
	enum TradesField
	{
		tradeId = 0,
		orderId,
		tradeTime,
		buySell,
		brokerRef,
		userName,
		firmId,
		cpFirmId,
		account,
		boardID,
		secCode,
		price,
		quantity,
		value,
		currency,
		settleDate,
		accruedInt,
		yield,
		period,
		price2,
		settleCode,
		tradeType,
		extRef,
		commission,
		repoRate,
		accrued2,
		repoValue,
		repo2Value,
		repoTerm,
		startDiscount,
		lowerDiscount,
		upperDiscount,
		blockSecurities,
		clearingCenterComm,
		exchangeComm,
		tradingSystemComm,
		clientCode,
		feedDatetime,

		tradeDate,
		settleTime,

		trdAccount,
		accountCp,

		reportNo,
		cpReportNo,
		usTradeStatus,

		repoTradeNo,
		price1,

		balance,
		amount,

		returnValue,
		discount,
		type,
		operationType,

		expectedDiscount,
		expectedQuantity,
		expectedRepoValue,
		expectedRepo2Value,
		expectedReturnValue,

		repoTradeDate,

		urgencyFlag,
		nextDaySettle
	};


class CTradesFields
{
	typedef map<TradesField, string> TradesFieldsMap;
	TradesFieldsMap fieldsMap;
	
	CTradesFields();
	~CTradesFields();

public:
	static CTradesFields gTradeMap;
	const char* operator[](TradesField key);
};

};
#endif