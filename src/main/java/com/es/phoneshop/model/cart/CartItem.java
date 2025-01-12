package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private static final String CART_ITEM_FORMAT = "[%s,%d]";

    private Product product;
    private int quantity;

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(CART_ITEM_FORMAT, product.getCode(),quantity);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
