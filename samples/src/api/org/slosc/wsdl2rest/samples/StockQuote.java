package org.slosc.wsdl2rest.samples;

import java.net.URL;
import java.math.BigDecimal;

public class StockQuote{
	String ticker;
	String currency;
	BigDecimal lastPrice;
	BigDecimal highestPriceToday;
	BigDecimal lowestPriceToday;
	
	URL bid;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getHighestPriceToday() {
        return highestPriceToday;
    }

    public void setHighestPriceToday(BigDecimal highestPriceToday) {
        this.highestPriceToday = highestPriceToday;
    }

    public BigDecimal getLowestPriceToday() {
        return lowestPriceToday;
    }

    public void setLowestPriceToday(BigDecimal lowestPriceToday) {
        this.lowestPriceToday = lowestPriceToday;
    }

    public URL getBid() {
        return bid;
    }

    public void setBid(URL bid) {
        this.bid = bid;
    }
}