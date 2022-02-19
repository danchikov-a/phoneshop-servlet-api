package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String PRICE_HISTORY_JSP = "/WEB-INF/pages/priceHistory.jsp";
    private static final String ATTRIBUTE_PRICE_HISTORY = "priceHistory";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private static final int POSITION_WITHOUT_SLASH = 1;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo().substring(POSITION_WITHOUT_SLASH);
        long productId = Long.parseLong(path);
        Product productOnPage = productDao.getProduct(productId);

        request.setAttribute(ATTRIBUTE_PRICE_HISTORY, productOnPage.getPriceHistory());
        request.setAttribute(ATTRIBUTE_DESCRIPTION, productOnPage.getDescription());
        request.getRequestDispatcher(PRICE_HISTORY_JSP).forward(request, response);
    }
}
