package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.service.cart.CartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private final String PRODUCT_ATTRIBUTE = "product";
    private final String CART_ATTRIBUTE = "cart";
    private final String ERROR_ATTRIBUTE = "error";
    private final String ERROR_STOCK_MESSAGE = "Out of stock, available ";
    private final String ERROR_NUMBER_FORMAT_MESSAGE = "Not a number";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART = "?message=Product added to cart";
    private final String PRODUCT_DETAILS_PAGE = "/WEB-INF/pages/productDetails.jsp";
    private final String PRODUCTS_PATH = "/products/";
    private final String SPACE_DELIMITER = " ";
    private final int START_INDEX_WITHOUT_SLASH = 1;
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(Long.valueOf(productId)));
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request));
        request.getRequestDispatcher(PRODUCT_DETAILS_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId =0L;
        int quantity = 0;
        try {
            productId = parseProductId(request);
            quantity = parseQuantity(request);
        }catch (ParseException e){
            request.setAttribute(ERROR_ATTRIBUTE, ERROR_NUMBER_FORMAT_MESSAGE);
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR_ATTRIBUTE,
                    String.join(SPACE_DELIMITER, ERROR_STOCK_MESSAGE, String.valueOf(e.getStockAvailable())));
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath()
                + PRODUCTS_PATH
                + productId
                + MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART);
    }

    private Long parseProductId(HttpServletRequest request) throws NumberFormatException {
        String productInfo = request.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);
        return Long.valueOf(productInfo);
    }

    private int parseQuantity(HttpServletRequest request) throws NumberFormatException, ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        String parameterQuantity = request.getParameter(QUANTITY_PARAMETER);
        return (int) numberFormat.parse(parameterQuantity);
    }
}