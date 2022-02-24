package com.es.phoneshop.web;

import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.dao.order.impl.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Currency;

public class OrderOverviewPageServlet extends HttpServlet {

    private static final Currency USD = Currency.getInstance("USD");
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String CART_ITEMS_ATTRIBUTE = "cartItems";
    private static final String OVERVIEW_JSP = "/WEB-INF/pages/overview.jsp";
    private static final String CURRENCY_ATTRIBUTE = "currency";
    private static final int POSITION_WITHOUT_SLASH = 1;

    private OrderDao orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = parseId(request.getPathInfo());
        Order order = orderDao.getOrderBySecureId(secureOrderId);

        request.setAttribute(CART_ITEMS_ATTRIBUTE, order.getCartItems());
        request.setAttribute(ORDER_ATTRIBUTE, order);
        request.setAttribute(CURRENCY_ATTRIBUTE, USD);

        request.getRequestDispatcher(OVERVIEW_JSP).forward(request, response);
    }

    private String parseId(String startPath){
        return startPath.substring(POSITION_WITHOUT_SLASH);
    }
}
