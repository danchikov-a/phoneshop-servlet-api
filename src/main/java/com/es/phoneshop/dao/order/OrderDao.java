package com.es.phoneshop.dao.order;

import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrder(Long id);
    Order getOrderBySecureId(String secureId);
    void save(Order order);
}
