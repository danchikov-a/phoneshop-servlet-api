package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;



@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private ProductDao productDaoSpy;
    private final String currencyUSD = "USD";
    private final String testCode = "test-code";
    private final String testDescription = "Samsung Galaxy S";
    private final String testImageURL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final String emptyString = "";
    private final int testStock = 100;
    private final int zeroStock = 0;
    private final int amountOfSamsGalaxys = 2;
    private final String descrToTestRightOrder = "Samsung Galaxy S III";
    private final int firstElem = 0;
    private final long idOfSamsGalaxyIII = 16L;
    private final String fieldDescription = "description";
    private final String fieldPrice = "price";
    private final String orderAsc = "asc";
    private final String orderDesc = "desc";
    private final long idOfProd1ToCheckSort = 17L;
    private final long idOfProd2ToCheckSort = 21L;
    private final long idOfProd3ToCheckSort = 22L;
    private final long idOfProd4ToCheckSort = 18L;

    @Before
    public void setup() {
        productDaoSpy = spy(ArrayListProductDao.getInstance());
    }

    @Test
    public void shouldFindProductsNoResults() {
        assertFalse(productDaoSpy.findProducts(anyString(),anyString(),anyString()).isEmpty());
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
        productDaoSpy.save(product);
        assertNotNull(productDaoSpy.getProduct(product.getId()));
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
        productDaoSpy.save(product);
        assertFalse(productDaoSpy.findProducts(anyString(),anyString(),anyString()).contains(product));
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

    @Test
    public void shouldFindAllProductsWhenQueryIsNull() {
        productDaoSpy.findProducts(null,null,null);
        verify(productDaoSpy).findProducts(null,null,null);
    }

    @Test
    public void shouldFindAllProductsWhenQueryIsEmpty() {
        productDaoSpy.findProducts(emptyString,null,null);
        verify(productDaoSpy).findProducts(emptyString,null,null);
    }

    @Test
    public void shouldFindProductsWithMatchingWordsInQuery() {
        assertEquals(productDaoSpy.findProducts(testDescription,null,null).size(),amountOfSamsGalaxys);
    }

    @Test
    public void shouldFindProductsInOrderOfMatchingWords() {
        Product firstElement = productDaoSpy.findProducts(descrToTestRightOrder,null,null).get(firstElem);
        assertEquals(firstElement, productDaoSpy.getProduct(idOfSamsGalaxyIII));
    }

    @Test
    public void shouldFindProductsInCorrectSort() {
        System.out.println(productDaoSpy.findProducts(null,fieldDescription,orderAsc));
        Product element;
        element = productDaoSpy.findProducts(null,fieldDescription,orderAsc).get(firstElem);
        assertEquals(element, productDaoSpy.getProduct(idOfProd1ToCheckSort));

        element = productDaoSpy.findProducts(null,fieldDescription,orderDesc).get(firstElem);
        assertEquals(element, productDaoSpy.getProduct(idOfProd2ToCheckSort));

        element = productDaoSpy.findProducts(null,fieldPrice,orderAsc).get(firstElem);
        assertEquals(element, productDaoSpy.getProduct(idOfProd3ToCheckSort));

        element = productDaoSpy.findProducts(null,fieldPrice,orderDesc).get(firstElem);
        assertEquals(element, productDaoSpy.getProduct(idOfProd4ToCheckSort));
    }

}
