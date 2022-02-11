package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable{
    private static final String CART_FORMAT = "Cart{%s}";

    private List<CartItem> cartItems;

    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public String toString() {
        return String.format(CART_FORMAT, cartItems);
    }
}
