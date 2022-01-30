package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String PRODUCT_JSP = "/WEB-INF/pages/product.jsp";
    private static final String ATTRIBUTE_PRODUCT = "product";
    private static final int POSITION_WITHOUT_SLASH = 1;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getPathInfo().substring(POSITION_WITHOUT_SLASH));
        request.setAttribute(ATTRIBUTE_PRODUCT, productDao.getProduct(productId));
        request.getRequestDispatcher(PRODUCT_JSP).forward(request, response);
    }

}
