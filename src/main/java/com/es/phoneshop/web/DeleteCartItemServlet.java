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

public class DeleteCartItemServlet extends HttpServlet {
    private CartService cartService;

    private static final int POSITION_WITHOUT_SLASH = 1;
    private static final String REDIRECT_URL = "/cart?message=Cart item removed";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = parseId(request.getPathInfo());
        Cart cart = cartService.getCart(request);

        cartService.delete(cart, productId);
        String redirectPath = request.getContextPath() + REDIRECT_URL;
        response.sendRedirect(redirectPath);
    }

    private long parseId(String startPath){
        String path = startPath.substring(POSITION_WITHOUT_SLASH);
        return Long.parseLong(path);
    }
}
