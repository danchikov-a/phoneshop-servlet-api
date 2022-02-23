package com.es.phoneshop.service.order;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    Order createOrder(Cart cart);
    List<PaymentMethod> getPaymentMethods();
    void placeOrder(Order order, HttpServletRequest request);
}
