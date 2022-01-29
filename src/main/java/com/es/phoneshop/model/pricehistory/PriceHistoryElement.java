package com.es.phoneshop.model.pricehistory;

import java.math.BigDecimal;
import java.util.Currency;

public class PriceHistoryElement {
    private String startDate;
    private BigDecimal price;
    private Currency currency;

    public PriceHistoryElement(String startDate, BigDecimal price, Currency currency) {
        this.startDate = startDate;
        this.price = price;
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
