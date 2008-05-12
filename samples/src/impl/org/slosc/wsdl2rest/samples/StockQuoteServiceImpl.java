package org.slosc.wsdl2rest.samples;


public class StockQuoteServiceImpl implements StockQuoteService {
    public StockQuote getStockQuote(String ticker) {
        return null;
    }

    public StockQuote[] getStockQuoteHistory(String ticker, String fromDate, String toDate) {
        return new StockQuote[0];  
    }
}
