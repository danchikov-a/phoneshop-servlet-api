package com.es.phoneshop.dao.order.impl;


import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.exception.NoSuchOrderException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOrderDao implements OrderDao {
    private List<Order> orders;
    private final Object lock = new Object();
    private static OrderDao instance;

    public static synchronized OrderDao getInstance() {
        if(instance == null){
             instance = new ArrayListOrderDao();
        }
        return instance;
    }

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
    }

    @Override
    public Order getOrder(Long id) throws NoSuchOrderException {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            synchronized (lock) {
                return orders.stream()
                        .filter(order -> id.equals(order.getId()))
                        .findAny()
                        .orElseThrow(NoSuchOrderException::new);
            }
        }
    }

    @Override
    public Order getOrderBySecureId(String id) throws NoSuchOrderException {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            synchronized (lock) {
                return orders.stream()
                        .filter(order -> id.equals(order.getSecureId()))
                        .findAny()
                        .orElseThrow(NoSuchOrderException::new);
            }
        }
    }

    @Override
    public void save(Order order) {
        synchronized (lock) {
            orders.add(order);
        }
    }
}
