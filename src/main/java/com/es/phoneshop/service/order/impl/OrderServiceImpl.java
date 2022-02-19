package com.es.phoneshop.service.order.impl;

import com.es.phoneshop.dao.order.impl.ArrayListOrderDao;
import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.order.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    private static final int DELIVERY_COST = 5;

    private static OrderServiceImpl instance;

    public static OrderService getInstance(){
        synchronized (OrderServiceImpl.class) {
            if (instance == null) {
                instance = new OrderServiceImpl();
            }
        }
        return instance;
    }
    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();

        order.setCartItems(cart.getCartItems().stream()
                .map(item -> {
            try {
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));

        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));

        return order;
    }

    private BigDecimal calculateDeliveryCost(){
        return new BigDecimal(DELIVERY_COST);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        orderDao.save(order);
    }
}
