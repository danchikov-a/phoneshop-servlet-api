package com.es.phoneshop.service;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilder;
import com.es.phoneshop.service.impl.CartServiceImpl;
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
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private CartService cartService = new CartServiceImpl();

    private static final Currency USD = Currency.getInstance("USD");
    private static final String TEST_CODE = "iphone6";
    private static final String TEST_DESCRIPTION = "Apple iPhone 6";
    private static final BigDecimal TEST_PRICE = new BigDecimal(1000);
    private static final int TEST_STOCK = 30;
    private static final String TEST_IMAGE_URL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg";
    private static final int STOCK_LESS_THEN_TEST = 12;
    private static final int REMAINING_STOCK_LESS_THEN_TEST = 25;
    private static final String FIELD_INSTANCE = "instance";
    private static final int STOCK_MORE_THEN_TEST = 35;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = CartServiceImpl.class.getDeclaredField(FIELD_INSTANCE);
        instance.setAccessible(true);
        instance.set(null, null);
        cartService = Mockito.spy(new CartServiceImpl());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddToCartWithQuantityLessThenStock() throws NotEnoughStockException {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        Cart cart = new Cart();

        when(productDao.getProduct(productId)).thenReturn(product);

        cartService.add(cart,productId,STOCK_LESS_THEN_TEST);

        List<CartItem> cartItems = cart.getCartItems();
        int sizeOfCartItems = cartItems.size();
        assertTrue(sizeOfCartItems > 0);
    }

    @Test(expected = NotEnoughStockException.class)
    public void shouldNotAddToCartWithQuantityMoreThenStock() throws NotEnoughStockException {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();

        when(productDao.getProduct(productId)).thenReturn(product);

        cartService.add(new Cart(),productId,STOCK_MORE_THEN_TEST);
    }

    @Test
    public void shouldAddToCartWithRemainingQuantityLessThenStock() throws NotEnoughStockException {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        Cart cart = new Cart();

        when(productDao.getProduct(productId)).thenReturn(product);

        cartService.add(cart, productId,STOCK_LESS_THEN_TEST);
        cartService.add(cart, productId,STOCK_LESS_THEN_TEST);
        List<CartItem> cartItems = cart.getCartItems();
        int sizeOfCartItems = cartItems.size();
        assertTrue(sizeOfCartItems > 0);
    }

    @Test(expected = NotEnoughStockException.class)
    public void shouldNotAddToCartWithRemainingQuantityMoreThenStock() throws NotEnoughStockException {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        Cart cart = new Cart();

        when(productDao.getProduct(productId)).thenReturn(product);

        cartService.add(cart,productId,STOCK_LESS_THEN_TEST);
        cartService.add(cart,productId,REMAINING_STOCK_LESS_THEN_TEST);
    }
}