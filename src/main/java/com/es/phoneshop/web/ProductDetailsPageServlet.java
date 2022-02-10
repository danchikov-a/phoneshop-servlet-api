package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.impl.ProductServiceImpl;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Deque;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private ProductService productService;

    private static final String PRODUCT_JSP = "/WEB-INF/pages/product.jsp";
    private static final String ATTRIBUTE_PRODUCT = "product";
    private static final int POSITION_WITHOUT_SLASH = 1;
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String REDIRECT_URL = "%s/products/%d?message=success";
    private static final String ATTRIBUTE_CART_ITEMS = "cartItems";
    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ATTRIBUTE_RECENT_VIEWED_PRODUCTS = "recentProducts";
    private static final String PROPERTY_BASE_NAME = "messages";
    private static final String ERROR_MESSAGE_PROPERTY = "errorMessage";
    private static final String ERROR_STOCK_MESSAGE_PROPERTY = "errorStockMessage";
    private static final String ERROR_NEGATIVE_NUM_PROPERTY = "errorNegativeNumberMessage";

    private Locale locale;
    private ResourceBundle resourceBundle;
    private String errorMessage;
    private String errorStockMessage;
    private String errorNegativeNumberMessage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
        productService = ProductServiceImpl.getInstance();
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(PROPERTY_BASE_NAME, locale);
        errorMessage = resourceBundle.getString(ERROR_MESSAGE_PROPERTY);
        errorStockMessage = resourceBundle.getString(ERROR_STOCK_MESSAGE_PROPERTY);
        errorNegativeNumberMessage = resourceBundle.getString(ERROR_NEGATIVE_NUM_PROPERTY);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(ATTRIBUTE_CART_ITEMS, cart.getCartItems());
        long productId = parseId(request.getPathInfo());
        Deque<Product> recentViewedProds = productService.getProducts(request);

        productService.add(recentViewedProds, productId);
        request.setAttribute(ATTRIBUTE_RECENT_VIEWED_PRODUCTS, productService.getProducts(request));
        request.setAttribute(ATTRIBUTE_PRODUCT, productDao.getProduct(productId));
        request.getRequestDispatcher(PRODUCT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = parseId(request.getPathInfo());
        String quantityString = request.getParameter(QUANTITY_PARAMETER);
        int quantity;
        String formatUrl = String.format(REDIRECT_URL, request.getContextPath(), productId);

        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException numberFormatException) {
            request.setAttribute(ATTRIBUTE_ERROR, errorMessage);
            doGet(request,response);
            return;
        }

        if(quantity <= 0){
            request.setAttribute(ATTRIBUTE_ERROR, errorNegativeNumberMessage);
            doGet(request,response);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (NotEnoughStockException e) {
            request.setAttribute(ATTRIBUTE_ERROR, errorStockMessage);
            doGet(request,response);
            return;
        }
        response.sendRedirect(formatUrl);
    }

    private long parseId(String startPath){
        String path = startPath.substring(POSITION_WITHOUT_SLASH);
        return Long.parseLong(path);
    }
}
