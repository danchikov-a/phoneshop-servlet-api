package com.es.phoneshop.web;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.impl.OrderServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {

    private static final String PARAMETER_PAYMENT_METHOD = "paymentMethod";
    private static final Currency USD = Currency.getInstance("USD");
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private static final String CURRENCY_ATTRIBUTE = "curr";
    private static final String PAYMENT_METHODS_ATTRIBUTE = "paymentMethods";
    private static final String PARAMETER_FIRST_NAME = "firstName";
    private static final String PARAMETER_LAST_NAME = "lastName";
    private static final String PARAMETER_PHONE = "phone";
    private static final String PARAMETER_DELIVERY_ADDRESS = "deliveryAddress";
    private static final String SUCCESSFUL_URL_FORMAT = "/cart/overview/%d";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String ERROR_REQUIRED_FIELD = "Field should not be empty";
    private static final String PARAMETER_DELIVERY_DATE = "deliveryDate";

    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        request.setAttribute(CART_ATTRIBUTE, cart);
        request.setAttribute(ORDER_ATTRIBUTE, order);
        request.setAttribute(CURRENCY_ATTRIBUTE, USD);
        request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getPaymentMethods());

        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        Order order = new Order();

        checkFieldByError(request, PARAMETER_FIRST_NAME, errors, order::setFirstName);
        checkFieldByError(request, PARAMETER_LAST_NAME, errors, order::setLastName);
        checkFieldByError(request, PARAMETER_PHONE, errors, order::setPhone);
        checkDateByError(request, errors, order::setDeliveryDate);
        checkFieldByError(request, PARAMETER_DELIVERY_ADDRESS, errors, order::setDeliveryAddress);
        checkPaymentMethodByError(request, errors, order::setPaymentMethod);

        if(errors.isEmpty()){
            String successfulUrl = String.format(SUCCESSFUL_URL_FORMAT, order.getId());

            orderService.placeOrder(order);
            response.sendRedirect(successfulUrl);
            return;
        }else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
        }

        doGet(request, response);
    }

    private void checkFieldByError(HttpServletRequest request, String parameterName,
                                              Map<String, String> errors, Consumer<String> consumer){
        String orderField = request.getParameter(parameterName);

        if(StringUtils.isEmpty(orderField)){
            errors.put(parameterName, ERROR_REQUIRED_FIELD);
        } else {
            consumer.accept(orderField);
        }
    }

    private void checkDateByError(HttpServletRequest request,
                                  Map<String, String> errors, Consumer<LocalDate> consumer){
        String orderField = request.getParameter(PARAMETER_DELIVERY_DATE);

        if(StringUtils.isEmpty(orderField)){
            errors.put(PARAMETER_DELIVERY_DATE, ERROR_REQUIRED_FIELD);
        } else {
            LocalDate localDate = LocalDate.parse(orderField);
            consumer.accept(localDate);
        }
    }

    private void checkPaymentMethodByError(HttpServletRequest request,
                                  Map<String, String> errors, Consumer<PaymentMethod> consumer){
        String orderField = request.getParameter(PARAMETER_PAYMENT_METHOD);

        if(StringUtils.isEmpty(orderField)){
            errors.put(PARAMETER_PAYMENT_METHOD, ERROR_REQUIRED_FIELD);
        } else {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(orderField);
            consumer.accept(paymentMethod);
        }
    }
}
