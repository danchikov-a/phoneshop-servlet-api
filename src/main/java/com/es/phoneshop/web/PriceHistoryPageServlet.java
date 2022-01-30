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
    private static final String PRICE_HISTORY_JSP = "/WEB-INF/pages/priceHistory.jsp";
    private static final String ATTRIBUTE_PRICE_HISTORY = "priceHistory";
    private static final int POSITION_WITHOUT_SLASH = 1;
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getPathInfo().substring(POSITION_WITHOUT_SLASH));
        request.setAttribute(ATTRIBUTE_PRICE_HISTORY, productDao.getProduct(productId).getPriceHistory());
        request.setAttribute(ATTRIBUTE_DESCRIPTION, productDao.getProduct(productId).getDescription());
        request.getRequestDispatcher(PRICE_HISTORY_JSP).forward(request, response);
    }

}
