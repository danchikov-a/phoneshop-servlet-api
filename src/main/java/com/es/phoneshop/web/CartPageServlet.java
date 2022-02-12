package com.es.phoneshop.web;

import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    private static final String CART_JSP = "/WEB-INF/pages/cart.jsp";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String REDIRECT_URL = "cart?message=success";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String PRODUCT_ID_PARAMETER = "productId";
    private static final String PROPERTY_BASE_NAME = "messages";
    private static final String ERROR_MESSAGE_PROPERTY = "errorMessage";
    private static final String ERROR_STOCK_MESSAGE_PROPERTY = "errorStockMessage";
    private static final String ERROR_NEGATIVE_NUM_PROPERTY = "errorNegativeNumberMessage";
    private static final String INPUT_ERRORS = "errors";

    private Locale locale;
    private ResourceBundle resourceBundle;
    private String errorMessage;
    private String errorStockMessage;
    private String errorNegativeNumberMessage;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartServiceImpl.getInstance();
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(PROPERTY_BASE_NAME, locale);
        errorMessage = resourceBundle.getString(ERROR_MESSAGE_PROPERTY);
        errorStockMessage = resourceBundle.getString(ERROR_STOCK_MESSAGE_PROPERTY);
        errorNegativeNumberMessage = resourceBundle.getString(ERROR_NEGATIVE_NUM_PROPERTY);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(CART_ATTRIBUTE, cart);
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Map<Long, String> errors = parseError(cart, request);

        if(!errors.isEmpty()) {
            request.setAttribute(INPUT_ERRORS, errors);
            doGet(request, response);
        }else {
            response.sendRedirect(REDIRECT_URL);
        }
    }

    private Map<Long, String> parseError(Cart cart, HttpServletRequest request) {
        String[] quantities = request.getParameterValues(QUANTITY_PARAMETER);
        String[] productIds = request.getParameterValues(PRODUCT_ID_PARAMETER);
        Map<Long, String> errors = new HashMap<>();

        if (productIds != null) {
            for (int i = 0; i < quantities.length; i++) {
                int quantityValue;
                Long productId = Long.valueOf(productIds[i]);
                try {
                    quantityValue = Integer.parseInt(quantities[i]);
                    if (quantityValue < 0) {
                        errors.put(productId, errorNegativeNumberMessage);
                    } else {
                        cartService.update(cart, Long.valueOf(productIds[i]), quantityValue);
                    }
                } catch (NumberFormatException | NotEnoughStockException exception) {
                    if (exception.getClass().equals(NumberFormatException.class)) {
                        errors.put(productId, errorMessage);
                    } else if (exception.getClass().equals(NotEnoughStockException.class)) {
                        errors.put(productId, errorStockMessage);
                    }

                }
            }
        }
        return errors;
    }

}
