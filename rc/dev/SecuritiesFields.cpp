#include "stdafx.h"
#include ".\securitiesfields.h"

namespace SecuritiesTable
{

CSecuritiesFields CSecuritiesFields::gSecMap;

CSecuritiesFields::CSecuritiesFields(void)
{
	fieldsMap[secBoard]					= "SECBOARD";
	fieldsMap[secCode]					= "SECCODE";
	fieldsMap[secName]					= "SECNAME";
	fieldsMap[remarks]					= "REMARKS";
	fieldsMap[shortName]				= "SHORTNAME";
	fieldsMap[status]					= "STATUS";
	fieldsMap[tradingStatus]			= "TRADINGSTATUS";
	fieldsMap[marketCode]				= "MARKETCODE";
	fieldsMap[instrID]					= "INSTRID";
	fieldsMap[lotSize]					= "LOTSIZE";
	fieldsMap[minStep]					= "MINSTEP";
	fieldsMap[faceValue]				= "FACEVALUE";
	fieldsMap[faceUnit]					= "FACEUNIT";
	fieldsMap[prevDate]					= "PREVDATE";
	fieldsMap[prevPrice]				= "PREVPRICE";
	fieldsMap[decimals]					= "DECIMALS";
	fieldsMap[yield]					= "YIELD";
	fieldsMap[accruedInt]				= "ACCRUEDINT";
	fieldsMap[matDate]					= "MATDATE";
	fieldsMap[couponValue]				= "COUPONVALUE";
	fieldsMap[couponPeriod]				= "COUPONPERIOD";
	fieldsMap[nextCoupon]				= "NEXTCOUPON";
	fieldsMap[issueSize]				= "ISSUESIZE";
	fieldsMap[prevWAPrice]				= "PREVWAPRICE";
	fieldsMap[yieldAtPrevWAPrice]		= "YIELDATPREVWAPRICE";
	fieldsMap[repo2Price]				= "REPO2PRICE";
	fieldsMap[currencyID]				= "CURRENCYID";
	fieldsMap[buyBackPrice]				= "BUYBACKPRICE";
	fieldsMap[buyBackDate]				= "BUYBACKDATE";
	fieldsMap[agentID]					= "AGENTID";
	fieldsMap[isin]						= "ISIN";
	fieldsMap[latName]					= "LATNAME";
	fieldsMap[regNumber]				= "REGNUMBER";
	fieldsMap[prevLegalClosePrice]		= "PREVLEGALCLOSEPRICE";
	fieldsMap[prevAdmittedQuote]		= "PREVADMITTEDQUOTE";
	fieldsMap[bid]						= "BID";
	fieldsMap[bidDepth]					= "BIDDEPTH";
	fieldsMap[bidDepthT]				= "BIDDEPTHT";
	fieldsMap[numBids]					= "NUMBIDS";
	fieldsMap[offer]					= "OFFER";
	fieldsMap[offerDepth]				= "OFFERDEPTH";
	fieldsMap[offerDepthT]				= "OFFERDEPTHT";
	fieldsMap[numOffers]				= "numOFFERS";
	fieldsMap[open]						= "OPEN";
	fieldsMap[high]						= "HIGH";
	fieldsMap[low]						= "LOW";
	fieldsMap[last]						= "LAST";
	fieldsMap[change]					= "CHANGE";
	fieldsMap[quantity]					= "QUANTITY";
	fieldsMap[qty]						= "QTY";
	fieldsMap[fTime]					= "TIME";
	fieldsMap[volToday]					= "VOLTODAY";
	fieldsMap[valToday]					= "VALTODAY";
	fieldsMap[value]					= "VALUE";
	fieldsMap[wAPrice]					= "WAPRICE";
	fieldsMap[highBid]					= "HIGHBID";
	fieldsMap[lowOffer]					= "LOWOFFER";
	fieldsMap[numTrades]				= "NUMTRADES";
	fieldsMap[yieldAtWAPrice]			= "YIELDATWAPRICE";
	fieldsMap[priceMinusPrevWAPrice]	= "PRICEMINUSPREVWAPRICE";
	fieldsMap[closePrice]				= "CLOSEPRICE";
	fieldsMap[closeYield]				= "CLOSEYIELD";
	fieldsMap[lastBid]					= "LASTBID";
	fieldsMap[lastOffer]				= "LASTOFFER";
	fieldsMap[lastSettleCode]			= "LASTSETTLECODE";
	fieldsMap[marketPrice]				= "MARKETPRICE";
	fieldsMap[marketPriceToday]			= "MARKETPRICETODAY";
	fieldsMap[duration]					= "DURATION";
	fieldsMap[settleCode]				= "SETTLECODE";
	fieldsMap[lOpenPrice]				= "LOPENPRICE";
	fieldsMap[lCurrentPrice]			= "LCURRENTPRICE";
	fieldsMap[lClosePrice]				= "LCLOSEPRICE";
	fieldsMap[marketPrice2]				= "MARKETPRICE2";
	fieldsMap[admittedQuote]			= "ADMITTEDQUOTE";
	fieldsMap[openPeriodPrice]			= "OPENPERIODPRICE";

	fieldsMap[foSecTypeID]				= "FOSECTYPEID";
	fieldsMap[firstTradeDate]			= "FIRSTTRADEDATE";
	fieldsMap[lastTradeDate]			= "LASTTRADEDATE";
	fieldsMap[lastDelDate]				= "LASTDELDATE";
	fieldsMap[daysToMaturity]			= "DAYSTOMATURITY";
	fieldsMap[prevSettlePrice]			= "PREVSETTLEPRICE";
	fieldsMap[priceMVTLimit]			= "priceMVTLimit";
	fieldsMap[maxOutVolume]				= "MAXOUTVOLUME";
	fieldsMap[strike]					= "STRIKE";
	fieldsMap[underlyingSecID]			= "UNDERLYINGSECID";
	fieldsMap[priceValue]				= "PRICEVALUE";
	fieldsMap[mktShareLimit]			= "MKTSHARELIMIT";
	fieldsMap[mktShareThreshOld]		= "MKTSHARETHRESHOLD";
	fieldsMap[lastToPrevSTLPrc]			= "LASTTOPREVSTLPRC";
	fieldsMap[settlePrice]				= "SETTLEPRICE";
	fieldsMap[negValToday]				= "NEGVALTODAY";
	fieldsMap[negNumTrades]				= "NEGNUMTRADES";
	fieldsMap[priceMax]					= "PRICEMAX";
	fieldsMap[priceMin]					= "PRICEMIN";
	fieldsMap[numContracts]				= "NUMCONTRACTS";
	fieldsMap[openPositionChange]		= "OPENPOSITIONCHANGE";

	fieldsMap[primaryDist]				= "PRIMARYDIST";
	fieldsMap[quoteBasis]				= "QUOTEBASIS";
	fieldsMap[secType]					= "SECTYPE";

	fieldsMap[putCall]					= "PUTCALL";
	fieldsMap[deliveryType]				= "DELIVERYTYPE";
}

CSecuritiesFields::~CSecuritiesFields(void)
{
	fieldsMap.clear();
}

const char* CSecuritiesFields::operator[](SecuritiesTable::SecuritiesField key)
{
	SecuritiesFieldsMap::iterator it = fieldsMap.find(key);
	if(it != fieldsMap.end())
		return ((*it).second).c_str();
	return "";
}
};