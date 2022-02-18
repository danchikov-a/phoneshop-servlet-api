package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Currency;

public class MiniCartServlet extends HttpServlet {
    private CartService cartService;

    private static final String MINI_CART_JSP = "/WEB-INF/pages/miniCart.jsp";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String CURRENCY_ATTRIBUTE = "curr";
    private static final Currency currency = Currency.getInstance("USD");

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        request.setAttribute(CART_ATTRIBUTE, cart);
        request.setAttribute(CURRENCY_ATTRIBUTE, currency);
        request.getRequestDispatcher(MINI_CART_JSP).include(request, response);
    }

}
