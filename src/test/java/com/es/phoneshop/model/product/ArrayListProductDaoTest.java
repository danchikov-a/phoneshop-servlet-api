package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private ProductDao productDaoSpy;

    private final String currencyUSD = "USD";
    private final String testCode = "test-code";
    private final String testDescription = "Samsung Galaxy S";
    private final String testImageURL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final int testStock = 100;
    private final int zeroStock = 0;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
        productDaoSpy = spy(productDao);
    }

    @Test
    public void shouldFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void shouldGetSavedProduct() {
        Currency usd = Currency.getInstance(currencyUSD);
        Product product = new ProductBuilder()
                .setCode(testCode)
                .setDescription(testDescription)
                .setPrice(new BigDecimal(100))
                .setCurrency(usd)
                .setStock(testStock)
                .setImageUrl(testImageURL)
                .build();
        productDao.save(product);
        assertNotNull(productDao.getProduct(product.getId()));
    }

    @Test
    public void shouldFindProductsWithoutZeroStock() {
        Currency usd = Currency.getInstance(currencyUSD);
        Product product = new ProductBuilder()
                .setCode(testCode)
                .setDescription(testDescription)
                .setPrice(new BigDecimal(100))
                .setCurrency(usd)
                .setStock(zeroStock)
                .setImageUrl(testImageURL)
                .build();
        productDao.save(product);
        assertFalse(productDao.findProducts().contains(product));
    }

    @Test(expected = NoSuchProductException.class)
    public void shouldDeleteProduct() {
        productDaoSpy.delete(anyLong());
        doThrow(NoSuchProductException.class)
                .when(productDaoSpy)
                .getProduct(anyLong());
        productDaoSpy.getProduct(anyLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenDeleteProductWithNullId() {
        doThrow(IllegalArgumentException.class)
                .when(productDaoSpy)
                .delete(null);
        productDaoSpy.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGetProductWithNullId() {
        doThrow(IllegalArgumentException.class)
                .when(productDaoSpy)
                .getProduct(null);
        productDaoSpy.getProduct(null);
    }

}
