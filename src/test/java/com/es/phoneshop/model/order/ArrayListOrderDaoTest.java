package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.dao.order.impl.ArrayListOrderDao;
import com.es.phoneshop.dao.product.impl.ArrayListProductDao;
import com.es.phoneshop.exception.NoSuchProductException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {
    
    private OrderDao orderDao;
    private static final String FIELD_INSTANCE = "instance";

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ArrayListOrderDao.class.getDeclaredField(FIELD_INSTANCE);
        instance.setAccessible(true);
        instance.set(null, null);
        orderDao = spy(ArrayListOrderDao.getInstance());
    }

    @Test
    public void shouldGetSavedOrder() {
        Order order = new Order();
        long orderId = order.getId();

        orderDao.save(order);

        assertNotNull(orderDao.getOrder(orderId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGetOrderWithNullId() {
        doThrow(IllegalArgumentException.class)
                .when(orderDao)
                .getOrder(null);

        orderDao.getOrder(null);
    }
}
