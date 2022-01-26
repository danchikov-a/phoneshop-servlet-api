package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;

public class ProductBuilder {
    private String code;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private int stock;
    private String imageUrl;

    public ProductBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public ProductBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public ProductBuilder setStock(int stock) {
        this.stock = stock;
        return this;
    }

    public ProductBuilder setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Product build(){
        return new Product(this);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
