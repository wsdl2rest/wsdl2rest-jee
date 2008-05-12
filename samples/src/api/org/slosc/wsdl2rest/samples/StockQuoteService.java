package org.slosc.wsdl2rest.samples;


public interface StockQuoteService{
	
	StockQuote getStockQuote(String ticker);

    StockQuote[] getStockQuoteHistory(String ticker, String fromDate, String toDate);
	
}	