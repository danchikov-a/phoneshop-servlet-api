package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private final long testId = 2L;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetSavedProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertNotNull(productDao.getProduct(product.getId()));
    }

    @Test
    public void testFindProductsWithZeroStock() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-code", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertFalse(productDao.findProducts().contains(product));
    }

    @Test
    public void testReplaceProduct() {
        Currency usd = Currency.getInstance("USD");
        Product oldProduct = productDao.getProduct(testId);
        Product product = new Product(testId,"test-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertTrue(productDao.findProducts().contains(oldProduct));
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProduct() {
        productDao.delete(testId);
        productDao.getProduct(testId);
    }


}
