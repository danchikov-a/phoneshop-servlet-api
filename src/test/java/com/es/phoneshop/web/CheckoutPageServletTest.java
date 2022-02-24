package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.order.OrderService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private OrderService orderService;
    @Mock
    private CartService cartService;
    @Mock
    private ServletConfig servletConfig;
    @InjectMocks
    private CheckoutPageServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    private static final String ERRORS_ATTRIBUTE = "errors";

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        servlet = Mockito.spy(new CheckoutPageServlet());
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void shouldForwardWhenDoGet() throws ServletException, IOException {
        servlet.doGet(request,response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void shouldRedirectWhenFormIsValid() throws ServletException, IOException {
        Cart cart = new Cart();

        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(new Order());

        servlet.doPost(request,response);
        verify(request).setAttribute(eq(ERRORS_ATTRIBUTE), any());
    }
}
