package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao;
    private final String productJsp = "/WEB-INF/pages/priceHistory.jsp";
    private final String attributeProduct = "priceHistory";
    private final int positionWithoutSlash = 1;
    private final String attributeDescription = "description";
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getPathInfo().substring(positionWithoutSlash));
        request.setAttribute(attributeProduct, productDao.getProduct(productId).getPriceHistory());
        request.setAttribute(attributeDescription, productDao.getProduct(productId).getDescription());
        request.getRequestDispatcher(productJsp).forward(request, response);
    }

}
