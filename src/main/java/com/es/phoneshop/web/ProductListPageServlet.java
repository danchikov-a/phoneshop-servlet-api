package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private final String attributeProducts = "products";
    private final String productListJsp = "/WEB-INF/pages/productList.jsp";
    private final String queryParameter = "query";
    private final String sortFieldParameter = "field";
    private final String orderParameter = "order";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = new ArrayListProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(queryParameter);
        String sortField = request.getParameter(sortFieldParameter);
        String order = request.getParameter(orderParameter);

        request.setAttribute(attributeProducts, productDao.findProducts(query,sortField,order));
        request.getRequestDispatcher(productListJsp).forward(request, response);
    }


}
