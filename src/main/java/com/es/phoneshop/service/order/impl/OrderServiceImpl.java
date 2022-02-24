package com.es.phoneshop.service.order.impl;

import com.es.phoneshop.dao.order.impl.ArrayListOrderDao;
import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = ArrayListOrderDao.getInstance();
    private CartService cartService = CartServiceImpl.getInstance();

    private static final int DELIVERY_COST;
    private static final int DEFAULT_DELIVERY_COST = 5;
    private static final String ORDER_PROPERTIES = "order.properties";
    private static final String DELIVERY_COST_PROPERTY = "order.delivery.cost";
    private static final String LOGGER_ERROR_MESSAGE = "Error occurred";
    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

    static {
        String rootPath = Thread.currentThread()
                .getContextClassLoader()
                .getResource(ORDER_PROPERTIES)
                .getPath();

        Properties appProps = new Properties();

        try {
            appProps.load(new FileInputStream(rootPath));
        } catch (IOException ioException) {
            LOGGER.error(LOGGER_ERROR_MESSAGE, ioException);
        }

        String deliveryProperty = appProps.getProperty(DELIVERY_COST_PROPERTY);

        DELIVERY_COST = deliveryProperty == null
                ? DEFAULT_DELIVERY_COST
                : Integer.parseInt(deliveryProperty);
    }

    private static OrderServiceImpl instance;

    public static OrderService getInstance() {
        synchronized (OrderServiceImpl.class) {
            if (instance == null) {
                instance = new OrderServiceImpl();
            }
        }
        return instance;
    }
    @Override
    public Order createOrder(Cart cart) {
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
        order.setDeliveryCost(loadDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));

        return order;
    }

    private BigDecimal loadDeliveryCost() {
        return new BigDecimal(DELIVERY_COST);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order, HttpServletRequest request) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        cartService.clearCart(request);
    }
}
