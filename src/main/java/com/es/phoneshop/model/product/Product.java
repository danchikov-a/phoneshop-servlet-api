package com.es.phoneshop.model.product;

import com.es.phoneshop.model.pricehistory.PriceHistoryElement;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class Product {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistoryElement> priceHistory;
    private static Long counter = 1L;

    public List<PriceHistoryElement> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistoryElement> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public static void setCounter(Long counter) {
        Product.counter = counter;
    }

    public Product(ProductBuilder productBuilder) {
        id = counter++;
        code = productBuilder.getCode();
        description = productBuilder.getDescription();
        price = productBuilder.getPrice();
        currency = productBuilder.getCurrency();
        stock = productBuilder.getStock();
        imageUrl = productBuilder.getImageUrl();
    }

    public static Long getCounter() {
        return counter;
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = counter++;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, code=%s, description=%s, price=%s, currency=%s, stock=%d, imageUrl=%s",
                id,code,description,price,currency,stock,imageUrl);
    }
}