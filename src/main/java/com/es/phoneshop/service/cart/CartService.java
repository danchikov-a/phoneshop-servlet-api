package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException;
    void update(Cart cart, Long productId, int quantity) throws NotEnoughStockException;
    void delete(Cart cart, Long productId);
    void clearCart(HttpServletRequest request);
}
