package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.NoSuchProductException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private ProductDao productDao;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_DESCRIPTION = "Samsung Galaxy S";
    private static final String TEST_IMAGE_URL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private static final String EMPTY_STRING = "";
    private static final int TEST_STOCK = 100;
    private static final int TEST_PRICE = 100;
    private static final int ZERO_STOCK = 0;
    private static final int FIRST_ELEM = 0;
    private static final int EXPECTED_VALUE = 2;
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PRICE = "price";
    private static final String ORDER_ASC = "asc";
    private static final String ORDER_DESC = "desc";
    private static final String FIELD_INSTANCE = "instance";
    private static final Currency CURRENCY_USD = Currency.getInstance("USD");
    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ArrayListProductDao.class.getDeclaredField(FIELD_INSTANCE);
        instance.setAccessible(true);
        instance.set(null, null);
        productDao = spy(ArrayListProductDao.getInstance());
    }

    @Test
    public void shouldFindProductsNoResults() {
        assertTrue(productDao.findProducts(anyString(),anyString(),anyString()).isEmpty());
    }

    @Test
    public void shouldGetSavedProduct() {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(new BigDecimal(TEST_PRICE))
                .setCurrency(CURRENCY_USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        productDao.save(product);
        assertNotNull(productDao.getProduct(productId));
    }

    @Test
    public void shouldFindProductsWithoutZeroStock() {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(new BigDecimal(TEST_PRICE))
                .setCurrency(CURRENCY_USD)
                .setStock(ZERO_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        assertFalse(productDao.findProducts(anyString(),anyString(),anyString()).contains(product));
    }

    @Test(expected = NoSuchProductException.class)
    public void shouldDeleteProduct() {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(new BigDecimal(TEST_PRICE))
                .setCurrency(CURRENCY_USD)
                .setStock(ZERO_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        productDao.save(product);
        productDao.delete(productId);
        doThrow(NoSuchProductException.class)
                .when(productDao)
                .getProduct(productId);
        productDao.getProduct(productId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenDeleteProductWithNullId() {
        doThrow(IllegalArgumentException.class)
                .when(productDao)
                .delete(null);
        productDao.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGetProductWithNullId() {
        doThrow(IllegalArgumentException.class)
                .when(productDao)
                .getProduct(null);
        productDao.getProduct(null);
    }

    @Test
    public void shouldFindAllProductsWhenQueryIsNull() {
        productDao.findProducts(null,null,null);
        verify(productDao).findProducts(null,null,null);
    }

    @Test
    public void shouldFindAllProductsWhenQueryIsEmpty() {
        productDao.findProducts(EMPTY_STRING,null,null);
        verify(productDao).findProducts(EMPTY_STRING,null,null);
    }

    @Test
    public void shouldFindProductsWithMatchingWordsInQuery() {
        Product prod1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), CURRENCY_USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product prod2 = new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), CURRENCY_USD, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        Product prod3 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), CURRENCY_USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        productDao.save(prod1);
        productDao.save(prod2);
        productDao.save(prod3);

        int restListSize = productDao.findProducts(TEST_DESCRIPTION,null,null).size();
        assertEquals(restListSize,EXPECTED_VALUE);
    }

    @Test
    public void shouldFindProductsInOrderOfMatchingWords() {
        Product prod1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), CURRENCY_USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product prod2 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), CURRENCY_USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        Product prod3 = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), CURRENCY_USD, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(prod1);
        productDao.save(prod2);
        productDao.save(prod3);

        Product firstElement = productDao.findProducts(prod2.getDescription(),null,null)
                .get(FIRST_ELEM);
        assertEquals(firstElement, productDao.getProduct(prod2.getId()));
    }

    @Test
    public void shouldFindProductsInCorrectSort() {
        Product prod1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), CURRENCY_USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product prod2 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), CURRENCY_USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        Product prod3 = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), CURRENCY_USD, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(prod1);
        productDao.save(prod2);
        productDao.save(prod3);
        Product element;
        element = productDao.findProducts(null, FIELD_DESCRIPTION, ORDER_ASC).get(FIRST_ELEM);
        assertEquals(element, productDao.getProduct(prod2.getId()));

        element = productDao.findProducts(null, FIELD_DESCRIPTION, ORDER_DESC).get(FIRST_ELEM);
        assertEquals(element, productDao.getProduct(prod3.getId()));

        element = productDao.findProducts(null, FIELD_PRICE, ORDER_ASC).get(FIRST_ELEM);
        assertEquals(element, productDao.getProduct(prod1.getId()));

        element = productDao.findProducts(null, FIELD_PRICE, ORDER_DESC).get(FIRST_ELEM);
        assertEquals(element, productDao.getProduct(prod2.getId()));
    }


}
