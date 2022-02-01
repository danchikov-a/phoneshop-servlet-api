package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilder;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductDetailsPageServlet servlet;

    private static final Currency USD = Currency.getInstance("USD");
    private static final String ATTRIBUTE_PRODUCT = "product";
    private static final String RETURN_FORMAT = "/%d";
    private static final String TEST_CODE = "iphone6";
    private static final String TEST_DESCRIPTION = "Apple iPhone 6";
    private static final BigDecimal TEST_PRICE = new BigDecimal(1000);
    private static final int TEST_STOCK = 30;
    private static final String TEST_IMAGE_URL = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg";
    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        servlet = Mockito.spy(new ProductDetailsPageServlet());
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        String path = request.getPathInfo();
        when(path).thenReturn(String.format(RETURN_FORMAT,productId));
        when(productDao.getProduct(any())).thenReturn(product);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testProductLoadedOnThePage() throws ServletException, IOException {
        Product product = new ProductBuilder()
                .setCode(TEST_CODE)
                .setDescription(TEST_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setCurrency(USD)
                .setStock(TEST_STOCK)
                .setImageUrl(TEST_IMAGE_URL)
                .build();
        long productId = product.getId();
        String path = request.getPathInfo();
        when(path).thenReturn(String.format(RETURN_FORMAT,productId));
        when(productDao.getProduct(any())).thenReturn(product);
        servlet.doGet(request, response);
        verify(request).setAttribute(eq(ATTRIBUTE_PRODUCT), any());
    }
}