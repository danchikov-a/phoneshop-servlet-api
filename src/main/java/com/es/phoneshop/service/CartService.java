package com.es.phoneshop.service;

import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;

public interface CartService {
    Cart getCart();
    void add(Long productId, int quantity) throws NotEnoughStockException;
}
