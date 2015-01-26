#pragma once

#include <map>
#include <string>

using namespace std;

namespace SecuritiesTable
{
	enum SecuritiesField
	{
		secBoard = 0,
		secCode,
		secName,
		remarks,
		shortName,
		status,
		tradingStatus,
		marketCode,
		instrID,
		lotSize,
		minStep,
		faceValue,
		faceUnit,
		prevDate,
		prevPrice,
		decimals,
		yield,
		accruedInt,
		matDate,
		couponValue,
		couponPeriod,
		nextCoupon,
		issueSize,
		prevWAPrice,
		yieldAtPrevWAPrice,
		repo2Price,
		currencyID,
		buyBackPrice,
		buyBackDate,
		agentID,
		isin,
		latName,
		regNumber,
		prevLegalClosePrice,
		prevAdmittedQuote,
		bid,
		bidDepth,
		bidDepthT,
		numBids,
		offer,
		offerDepth,
		offerDepthT,
		numOffers,
		open,
		high,
		low,
		last,
		change,
		quantity,
		qty,
		fTime,
		volToday,
		valToday,
		value,
		wAPrice,
		highBid,
		lowOffer,
		numTrades,
		yieldAtWAPrice,
		priceMinusPrevWAPrice,
		closePrice,
		closeYield,
		lastBid,
		lastOffer,
		lastSettleCode,
		marketPrice,
		marketPriceToday,
		duration,
		settleCode,
		lOpenPrice,
		lCurrentPrice,
		lClosePrice,
		marketPrice2,
		admittedQuote,
		openPeriodPrice,

		foSecTypeID,
		firstTradeDate,
		lastTradeDate,
		lastDelDate,
		daysToMaturity,
		prevSettlePrice,
		priceMVTLimit,
		maxOutVolume,
		strike,
		underlyingSecID,
		priceValue,
		mktShareLimit,
		mktShareThreshOld,
		lastToPrevSTLPrc,
		settlePrice,
		negValToday,
		negNumTrades,
		priceMax,
		priceMin,
		numContracts,
		openPositionChange,

		primaryDist,
		quoteBasis,
		secType,

		putCall,
		deliveryType
	};


	class CSecuritiesFields
	{
		typedef map<SecuritiesField, string> SecuritiesFieldsMap;
		SecuritiesFieldsMap fieldsMap;
		CSecuritiesFields(void);
		virtual ~CSecuritiesFields(void);

	public:
		static CSecuritiesFields gSecMap;
		const char* operator[](SecuritiesField key);
	};
};