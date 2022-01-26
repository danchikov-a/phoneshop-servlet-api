package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private final long testId = 2L;
    private final String currencyUSD = "USD";
    private final String testCode = "test-code";
    private final String testDescription = "Samsung Galaxy S";
    private final String testImageURL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";


    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void shouldFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void shouldGetSavedProduct() {
        Currency usd = Currency.getInstance(currencyUSD);
        Product product = new Product(testCode, testDescription, new BigDecimal(100), usd, 100, testImageURL);
        productDao.save(product);
        assertNotNull(productDao.getProduct(product.getId()));
    }

    @Test
    public void shouldFindProductsWithoutZeroStock() {
        Currency usd = Currency.getInstance(currencyUSD);
        Product product = new Product(testCode, testDescription, new BigDecimal(100), usd, 0, testImageURL);
        productDao.save(product);
        assertFalse(productDao.findProducts().contains(product));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldDeleteProduct() {
        productDao.delete(testId);
        productDao.getProduct(testId);
    }

    @Test(expected = NoSuchProductException.class)
    public void shouldThrowNoSuchProductExceptionWhenNullId() {
        productDao.delete(null);
        productDao.getProduct(null);
    }


}
