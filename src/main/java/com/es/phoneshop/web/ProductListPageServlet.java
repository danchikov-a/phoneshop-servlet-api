package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.impl.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.impl.ProductServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private ProductService productService;

    private static final String ATTRIBUTE_RECENT_VIEWED_PRODUCTS = "recentProducts";
    private ProductDao productDao;
    private static final String ATTRIBUTE_PRODUCTS = "products";
    private static final String PRODUCT_LIST_JSP = "/WEB-INF/pages/productList.jsp";
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_FIELD_PARAMETER = "field";
    private static final String ORDER_PARAMETER = "order";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(QUERY_PARAMETER);
        String sortField = request.getParameter(SORT_FIELD_PARAMETER);
        String order = request.getParameter(ORDER_PARAMETER);

        request.setAttribute(ATTRIBUTE_RECENT_VIEWED_PRODUCTS, productService.getProducts(request));
        request.setAttribute(ATTRIBUTE_PRODUCTS, productDao.findProducts(query,sortField,order));
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }
}
