package com.es.phoneshop.model.pricehistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PriceHistoryItem {
    private LocalDate startDate;
    private BigDecimal price;
    private Currency currency;

    public PriceHistoryItem(LocalDate startDate, BigDecimal price, Currency currency) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
