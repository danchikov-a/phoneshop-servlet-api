package com.es.phoneshop.web;

import com.es.phoneshop.service.cart.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddCartItemPLPServletTest {
    @Mock
    private CartService cartService;
    @Mock
    private ServletConfig servletConfig;
    @InjectMocks
    private AddCartItemPLPServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private static final String PRODUCT_ID_PARAMETER = "productId";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String TEST_ID = "1";
    private static final String TEST_QUANTITY = "1";
    private static final String STRING_QUANTITY = "eee";

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        servlet = Mockito.spy(new AddCartItemPLPServlet());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldRedirectWhenNoErrors() throws ServletException, IOException {
        when(request.getParameter(PRODUCT_ID_PARAMETER)).thenReturn(TEST_ID);
        when(request.getParameter(QUANTITY_PARAMETER)).thenReturn(TEST_QUANTITY);

        servlet.doPost(request, response);

        verify(request).setAttribute(CART_ATTRIBUTE, eq(any()));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowNumberFormatExceptionWhenQuantityIsString() throws ServletException, IOException {
        when(request.getParameter(PRODUCT_ID_PARAMETER)).thenReturn(TEST_ID);
        when(request.getParameter(QUANTITY_PARAMETER)).thenReturn(STRING_QUANTITY);
        doThrow(NumberFormatException.class).when(request).setAttribute(ERROR_ATTRIBUTE, eq(any()));

        servlet.doPost(request, response);
    }
}
