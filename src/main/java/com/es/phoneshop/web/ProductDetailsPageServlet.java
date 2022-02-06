package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.CartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private static final String PRODUCT_JSP = "/WEB-INF/pages/product.jsp";
    private static final String ATTRIBUTE_PRODUCT = "product";
    private static final int POSITION_WITHOUT_SLASH = 1;
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String REDIRECT_URL = "%s/products/%d?message=success";
    private static final String ATTRIBUTE_CART_ITEMS = "cartItems";
    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ERROR_MESSAGE = "Product not added to cart";
    private static final String ERROR_STOCK_MESSAGE = "Out of stock";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ATTRIBUTE_CART_ITEMS,cartService.getCart().getCartItems());
        long productId = parseId(request.getPathInfo());

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
            request.setAttribute(ATTRIBUTE_ERROR, ERROR_MESSAGE);
            doGet(request,response);
            return;
        }

        try {
            cartService.add(productId, quantity);
        } catch (NotEnoughStockException e) {
            request.setAttribute(ATTRIBUTE_ERROR, ERROR_STOCK_MESSAGE);
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
