package com.es.phoneshop.web;

import com.es.phoneshop.exception.UnexpectedDateException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.impl.OrderServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private final String CHECKOUT_PAGE = "/WEB-INF/pages/checkout.jsp";
    private final String ORDER_ATTRIBUTE = "order";
    private final String OVERVIEW_PATH = "/order/overview/";
    private final String ERRORS_ATTRIBUTE = "errors";
    private final String FIRST_NAME_PARAMETER = "firstName";
    private final String LAST_NAME_PARAMETER = "lastName";
    private final String PHONE_PARAMETER = "phone";
    private final String DELIVERY_DATE_PARAMETER = "deliveryDate";
    private final String DELIVERY_ADDRESS_PARAMETER = "deliveryAddress";
    private final String PAYMENT_METHOD_PARAMETER = "paymentMethod";
    private final String PAYMENT_METHODS_ATTRIBUTE = "paymentMethods";
    private final String BASE_NAME_PATH = "error";
    private final String REDIRECT_FORMAT = "%s%s%s";
    private final String ERROR_VALUE_REQUIRED = "error.valueRequired.message";
    private final String ERROR_DATE_FORMAT = "error.dateFormat.message";
    private final int AMOUNT_ACTIVE_PERIOD_OF_MONTHS = 1;

    private CartService cartService;
    private OrderService orderService;
    private Locale currentLocale;
    private ResourceBundle messages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        currentLocale = Locale.getDefault();
        messages = ResourceBundle.getBundle(BASE_NAME_PATH, currentLocale);
        cartService = CartSessionServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        request.setAttribute(ORDER_ATTRIBUTE, orderService.getOrder(cart));
        request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getPaymentMethods());

        request.getRequestDispatcher(CHECKOUT_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        setRequiredParameters(request, errors, order);

        handleError(request, response, errors, order);
    }

    private void setRequiredParameters(HttpServletRequest request, Map<String, String> errors, Order order) {
        setRequiredParameter(request, FIRST_NAME_PARAMETER, errors, order::setFirstName);
        setRequiredParameter(request, LAST_NAME_PARAMETER, errors, order::setLastName);
        setRequiredParameter(request, PHONE_PARAMETER, errors, order::setPhone);
        setRequiredParameter(request, DELIVERY_ADDRESS_PARAMETER, errors, order::setDeliveryAddress);
        setRequiredParameter(request, FIRST_NAME_PARAMETER, errors, order::setFirstName);
        setPaymentMethod(request, errors, order);
        setDeliveryDateParameter(request, errors, order);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors,
                             Order order) throws IOException, ServletException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order, request);
            response.sendRedirect(String.format(REDIRECT_FORMAT, request.getContextPath(), OVERVIEW_PATH,
                    order.getSecureId()));
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
            request.setAttribute(ORDER_ATTRIBUTE, order);
            request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_PAGE).forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        if (!validateIsEmpty(value, errors, parameter)) {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(PAYMENT_METHOD_PARAMETER);

        if (!validateIsEmpty(value, errors, PAYMENT_METHOD_PARAMETER)) {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }

    private void setDeliveryDateParameter(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(DELIVERY_DATE_PARAMETER);

        if (!validateIsEmpty(value, errors, DELIVERY_DATE_PARAMETER)) {
            try {
                order.setDeliveryDate(validateDate(value));
            } catch (UnexpectedDateException exception) {
                errors.put(DELIVERY_DATE_PARAMETER, messages.getString(ERROR_DATE_FORMAT));
            }
        }
    }

    private LocalDate validateDate(String dateString) throws UnexpectedDateException {
        LocalDate localDate = LocalDate.parse(dateString);

        if (localDate.isBefore(LocalDate.now())
                || localDate.isAfter(LocalDate.now().plusMonths(AMOUNT_ACTIVE_PERIOD_OF_MONTHS))) {
            throw new UnexpectedDateException();
        }
        return localDate;
    }

    private boolean validateIsEmpty(String value, Map<String, String> errors, String parameter) {
        if (StringUtils.isEmpty(value)) {
            errors.put(parameter, messages.getString(ERROR_VALUE_REQUIRED));
            return true;
        } else {
            return false;
        }
    }
}