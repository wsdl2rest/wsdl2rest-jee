package org.slosc.wsdl2rest.samples;


public class StockQuoteServiceImpl implements StockQuoteService {

    //@Context, @Header, @Param, @CookieParam, @MatrixParam, @QueryParam or @PathParam
    public StockQuoteServiceImpl(){

    }
    
    public StockQuote getStockQuote(String ticker) {
        return null;
    }

    public StockQuote[] getStockQuoteHistory(String ticker, String fromDate, String toDate) {
        return new StockQuote[0];  
    }
}
