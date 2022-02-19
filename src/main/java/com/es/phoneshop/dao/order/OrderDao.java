package com.es.phoneshop.dao.order;

import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrder(Long id);
    void save(Order order);
}
