package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// TODO all localization
// TODO tests
public class CartPageServlet extends HttpServlet {
    private final String CART_PAGE = "/WEB-INF/pages/cart.jsp";
    private final String CART_ATTRIBUTE = "cart";
    private final String ERRORS_ATTRIBUTE = "errors";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String PRODUCT_ID_PARAMETER = "productId";
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
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

        for (int i = 0; i < productIds.length || i < quantities.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity = 0;
            try {
                quantity = Integer.parseInt(quantities[i]);
                cartService.update(cartService.getCart(request), productId, quantity);
            } catch (NumberFormatException | OutOfStockException e) {
                handleError(errors, productId, e);
            }
        }
        request.setAttribute(ERRORS_ATTRIBUTE, errors);
        doGet(request, response);
    }

    //TODO make constants, add localization
    private void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(NumberFormatException.class)) {
            errors.put(productId, "Not a number");
        } else {
            if (((OutOfStockException) e).getStockRequested() <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available " + ((OutOfStockException) e).getStockRequested());
            }
        }
    }
}