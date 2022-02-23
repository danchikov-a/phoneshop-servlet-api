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
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private static final String SUCCESSFUL_URL_FORMAT = "/order/overview/%s";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String PARAMETER_DELIVERY_DATE = "deliveryDate";
    private static final String ERROR_REQUIRED_FIELD_PROPERTY = "messages.error.required.field";
    private static final String ERROR_FORMATTING_DATE_PROPERTY = "messages.error.formatting.date";
    private static final String ERROR_NO_SUCH_PAYMENT_METHOD_PROPERTY = "messages.error.no.such.payment.method";
    private static final String PROPERTY_BASE_NAME = "messages";

    private Locale locale;
    private ResourceBundle resourceBundle;
    private String errorRequiredField;
    private String errorFormattingDate;
    private String errorNoSuchPaymentMethod;
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(PROPERTY_BASE_NAME, locale);
        errorRequiredField = resourceBundle.getString(ERROR_REQUIRED_FIELD_PROPERTY);
        errorFormattingDate = resourceBundle.getString(ERROR_FORMATTING_DATE_PROPERTY);
        errorNoSuchPaymentMethod = resourceBundle.getString(ERROR_NO_SUCH_PAYMENT_METHOD_PROPERTY);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);

        request.setAttribute(CART_ATTRIBUTE, cart);
        request.setAttribute(ORDER_ATTRIBUTE, order);
        request.setAttribute(CURRENCY_ATTRIBUTE, USD);
        request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getPaymentMethods());

        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);

        validateParameter(request, PARAMETER_FIRST_NAME, errors, order::setFirstName);
        validateParameter(request, PARAMETER_LAST_NAME, errors, order::setLastName);
        validateParameter(request, PARAMETER_PHONE, errors, order::setPhone);
        checkDateByError(request, errors, order::setDeliveryDate);
        validateParameter(request, PARAMETER_DELIVERY_ADDRESS, errors, order::setDeliveryAddress);
        checkPaymentMethodByError(request, errors, order::setPaymentMethod);

        if(errors.isEmpty()){
            orderService.placeOrder(order, request);
            String successfulUrl = String.format(SUCCESSFUL_URL_FORMAT, order.getSecureId());

            response.sendRedirect(request.getContextPath() + successfulUrl);
            return;
        }else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
        }

        doGet(request, response);
    }

    private void validateParameter(HttpServletRequest request, String parameterName,
                                   Map<String, String> errors, Consumer<String> consumer){
        String parameterField = request.getParameter(parameterName);

        if(StringUtils.isEmpty(parameterField)){
            errors.put(parameterName, errorRequiredField);
        } else {
            consumer.accept(parameterField);
        }
    }

    private void checkDateByError(HttpServletRequest request,
                                  Map<String, String> errors, Consumer<LocalDate> consumer){
        String parameterField = request.getParameter(PARAMETER_DELIVERY_DATE);

        if(StringUtils.isEmpty(parameterField)){
            errors.put(PARAMETER_DELIVERY_DATE, errorRequiredField);
        } else {
            LocalDate localDate = null;

            try {
                localDate = LocalDate.parse(parameterField);
            } catch (DateTimeParseException dateTimeParseException) {
                errors.put(PARAMETER_DELIVERY_DATE, errorFormattingDate);
            }

            consumer.accept(localDate);
        }
    }

    private void checkPaymentMethodByError(HttpServletRequest request,
                                  Map<String, String> errors, Consumer<PaymentMethod> consumer){
        String parameterField = request.getParameter(PARAMETER_PAYMENT_METHOD);

        if(StringUtils.isEmpty(parameterField)){
            errors.put(PARAMETER_PAYMENT_METHOD, errorRequiredField);
        } else {
            PaymentMethod paymentMethod = null;
            try {
                paymentMethod = PaymentMethod.valueOf(parameterField);
            } catch (IllegalArgumentException e) {
                errors.put(PARAMETER_PAYMENT_METHOD, errorNoSuchPaymentMethod);
            }
            consumer.accept(paymentMethod);
        }
    }
}
