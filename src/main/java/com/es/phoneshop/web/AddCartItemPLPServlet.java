package com.es.phoneshop.web;

import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddCartItemPLPServlet extends HttpServlet {
    private CartService cartService;
    private Locale locale;
    private ResourceBundle resourceBundle;
    private String errorMessage;
    private String errorStockMessage;
    private String errorNegativeNumberMessage;

    private static final String PRODUCTS_URL = "/products?message=error";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String PRODUCT_ID_PARAMETER = "productId";
    private static final String PROPERTY_BASE_NAME = "messages";
    private static final String ERROR_MESSAGE_PROPERTY = "errorMessage";
    private static final String ERROR_STOCK_MESSAGE_PROPERTY = "errorStockMessage";
    private static final String ERROR_NEGATIVE_NUM_PROPERTY = "errorNegativeNumberMessage";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String SUCCESS_REDIRECT = "/products?message=success";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(PROPERTY_BASE_NAME, locale);
        errorMessage = resourceBundle.getString(ERROR_MESSAGE_PROPERTY);
        errorStockMessage = resourceBundle.getString(ERROR_STOCK_MESSAGE_PROPERTY);
        errorNegativeNumberMessage = resourceBundle.getString(ERROR_NEGATIVE_NUM_PROPERTY);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter(PRODUCT_ID_PARAMETER);
        String quantity = request.getParameter(QUANTITY_PARAMETER);
        Cart cart = cartService.getCart(request);
        String redirectURL = request.getContextPath() + PRODUCTS_URL;

        int quantityValue;
        Long productIdValue = Long.valueOf(productId);
        try {
            quantityValue =  Integer.parseInt(quantity);
            if(quantityValue < 0){
                request.setAttribute(ERROR_ATTRIBUTE, errorNegativeNumberMessage);
            }else {
                cartService.add(cart, productIdValue, quantityValue);
                redirectURL = request.getContextPath() + SUCCESS_REDIRECT;
            }
        } catch (NumberFormatException exception) {
            request.setAttribute(ERROR_ATTRIBUTE, errorMessage);
        } catch (NotEnoughStockException exception) {
            request.setAttribute(ERROR_ATTRIBUTE, errorStockMessage);
        }

        request.setAttribute(CART_ATTRIBUTE, cart);
        response.sendRedirect(redirectURL);
    }
}
