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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {
    @Mock
    private CartService cartService;
    @Mock
    private ServletConfig servletConfig;
    @InjectMocks
    private DeleteCartItemServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private static final String TEST_PATH_INFO = "/1";
    private static final Long TEST_PRODUCT_ID = 1L;

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        servlet = Mockito.spy(new DeleteCartItemServlet());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldDeleteAndThenRedirect() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(TEST_PATH_INFO);

        servlet.doPost(request, response);

        verify(cartService).delete(cartService.getCart(request), TEST_PRODUCT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenProductIdNull() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(TEST_PATH_INFO);
        doThrow(IllegalArgumentException.class).when(cartService)
                .delete(cartService.getCart(request), null);

        servlet.doPost(request, response);

        verify(cartService).delete(cartService.getCart(request), null);
    }
}
