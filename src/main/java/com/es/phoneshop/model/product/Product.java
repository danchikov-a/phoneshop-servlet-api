package com.es.phoneshop.model.product;

import com.es.phoneshop.model.pricehistory.PriceHistoryItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class Product implements Serializable {
    private static final String PRODUCT_FORMAT =
            "Product{id=%d, code=%s, description=%s, price=%s, currency=%s, stock=%d, imageUrl=%s";

    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistoryItem> priceHistory;
    private static Long counter = 1L;

    public List<PriceHistoryItem> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistoryItem> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public static void setCounter(Long counter) {
        Product.counter = counter;
    }

    public Product(String description) {
        this.description = description;
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
        return String.format(PRODUCT_FORMAT, id, code, description, price, currency, stock, imageUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())){
            return false;
        }
        Product product = (Product) o;
        return stock == product.stock &&
                Objects.equals(id, product.id) &&
                Objects.equals(code, product.code) &&
                Objects.equals(description, product.description) &&
                Objects.equals(price, product.price) &&
                Objects.equals(currency, product.currency) &&
                Objects.equals(imageUrl, product.imageUrl) &&
                Objects.equals(priceHistory, product.priceHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, price, currency, stock, imageUrl, priceHistory);
    }
}