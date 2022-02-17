package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

// TODO all localization
// TODO tests
public class CartPageServlet extends HttpServlet {
    private final String CART_PAGE = "/WEB-INF/pages/cart.jsp";
    private final String CART_ATTRIBUTE = "cart";
    private final String ERROR_STOCK_MESSAGE = "error.outOfStock.message";
    private final String ERROR_NUMBER_FORMAT_MESSAGE = "error.numberFormat.message";
    private final String ERROR_NON_POSITIVE_MESSAGE = "error.nonPositive.message";
    private final String MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART = "/cart?message=Product updated successfully";
    private final String ERRORS_ATTRIBUTE = "errors";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String PRODUCT_ID_PARAMETER = "productId";
    private final String BASE_NAME_PATH = "error";
    private CartService cartService;
    private Locale currentLocale;
    private ResourceBundle messages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        currentLocale = Locale.getDefault();
        messages = ResourceBundle.getBundle(BASE_NAME_PATH, currentLocale);
        cartService = CartSessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(CART_ATTRIBUTE, cart);
        request.getRequestDispatcher(CART_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues(PRODUCT_ID_PARAMETER);
        String[] quantities = request.getParameterValues(QUANTITY_PARAMETER);
        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity = 0;
            try {
                quantity = parseQuantity(request, quantities[i]);
            } catch (ParseException | IllegalArgumentException e) {
                handleError(errors, productId, e);
            }
            try {
                cartService.update(cartService.getCart(request), productId, quantity);
            } catch (IllegalArgumentException | OutOfStockException e) {
                handleError(errors, productId, e);
            }
        }
        if(errors.isEmpty()){
            response.sendRedirect(String.format("%s%s", request.getContextPath(),
                    MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART));
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
            doGet(request, response);
        }
    }

    //TODO add localization
    private void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass() == ParseException.class) {
            errors.put(productId, messages.getString(ERROR_NON_POSITIVE_MESSAGE));
        } else if (e.getClass() == IllegalArgumentException.class) {
            errors.put(productId, messages.getString(ERROR_NUMBER_FORMAT_MESSAGE));
        } else if (e.getClass() == OutOfStockException.class) {
            errors.put(productId, String.join(StringUtils.SPACE,
                    messages.getString(ERROR_STOCK_MESSAGE), "sdfd"));
        }
    }

    private int parseQuantity(HttpServletRequest request, String quantityParameter) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityParameter).intValue();
    }
}