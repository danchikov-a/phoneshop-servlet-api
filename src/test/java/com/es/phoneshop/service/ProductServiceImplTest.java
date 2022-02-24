package com.es.phoneshop.service;

import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilder;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Deque;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService = ProductServiceImpl.getInstance();

    private static final Currency USD = Currency.getInstance("USD");
    private static final String TEST_CODE = "iphone6";
    private static final String TEST_DESCRIPTION = "Apple iPhone 6";
    private static final BigDecimal TEST_PRICE = new BigDecimal(1000);
    private static final int TEST_STOCK = 30;
    private static final String TEST_IMAGE_URL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg";
    private static final String FIELD_INSTANCE = "instance";
    private static final int SIZE_OF_RECENT_VIEWED = 3;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = CartServiceImpl.class.getDeclaredField(FIELD_INSTANCE);
        instance.setAccessible(true);
        instance.set(null, null);
        productService = Mockito.spy(ProductServiceImpl.getInstance());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddToRecentViewedWhenEmpty(){
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        Deque<Product> products = new LinkedList<>();
        productService.add(products, productId);
        assertTrue(products.size() > 0);
    }

    @Test
    public void shouldAddToBeginAndDeleteLastWhenSizeMoreThanThree(){
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        Deque<Product> products = mock(LinkedList.class);
        when(products.size()).thenReturn(SIZE_OF_RECENT_VIEWED);
        productService.add(products,productId);
        assertEquals(SIZE_OF_RECENT_VIEWED, products.size());
    }

}
