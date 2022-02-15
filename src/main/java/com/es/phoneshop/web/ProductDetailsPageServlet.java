package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;
import com.es.phoneshop.service.product.RecentlyViewedProductsService;
import com.es.phoneshop.service.product.impl.RecentlyViewedProductsServiceImpl;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;


public class ProductDetailsPageServlet extends HttpServlet {
    private final String PRODUCT_ATTRIBUTE = "product";
    private final String CART_ATTRIBUTE = "sessionCart";
    private final String ERROR_ATTRIBUTE = "error";
    private final String RECENT_PRODUCTS_ATTRIBUTE = "recentlyViewedProducts";
    private final String ERROR_STOCK_MESSAGE = "error.outOfStock.message";
    private final String ERROR_NUMBER_FORMAT_MESSAGE = "error.numberFormat.message";
    private final String ERROR_NON_POSITIVE_MESSAGE = "error.nonPositive.message";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART = "?message=Product added to cart";
    private final String PRODUCT_DETAILS_PAGE = "/WEB-INF/pages/productDetails.jsp";
    private final String PRODUCTS_PATH = "/products/";
    private final String BASE_NAME_PATH = "error";
    private final int START_INDEX_WITHOUT_SLASH = 1;
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;
    private Locale currentLocale;
    private ResourceBundle messages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        currentLocale = Locale.getDefault();
        messages = ResourceBundle.getBundle(BASE_NAME_PATH , currentLocale);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartSessionServiceImpl.getInstance();
        recentlyViewedProductsService = RecentlyViewedProductsServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        ArrayList<Product> recentlyViewedProducts = recentlyViewedProductsService.getRecentlyViewedProducts(request);
        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productId);
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(productId));
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request));
        request.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentlyViewedProducts);
        request.getRequestDispatcher(PRODUCT_DETAILS_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = 0L;
        int quantity = 0;
        try {
            productId = parseProductId(request);
            quantity = parseQuantity(request);
        } catch (ParseException e) {
            request.setAttribute(ERROR_ATTRIBUTE, messages.getString(ERROR_NUMBER_FORMAT_MESSAGE));
            doGet(request, response);
            return;
        } catch (IllegalArgumentException e) {
            request.setAttribute(ERROR_ATTRIBUTE, messages.getString(ERROR_NON_POSITIVE_MESSAGE));
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR_ATTRIBUTE,
                    String.join(StringUtils.SPACE, messages.getString(ERROR_STOCK_MESSAGE),
                            String.valueOf(e.getStockAvailable())));
            doGet(request, response);
            return;
        }
        response.sendRedirect(String.format("%s%s%s%s", request.getContextPath(),
                PRODUCTS_PATH,
                productId,
                MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART));
    }

    private Long parseProductId(HttpServletRequest request) throws NumberFormatException {
        String productInfo = request.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);
        return Long.valueOf(productInfo);
    }

    private int parseQuantity(HttpServletRequest request) throws NumberFormatException, ParseException, IllegalArgumentException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        String parameterQuantity = request.getParameter(QUANTITY_PARAMETER);
        int quantity = numberFormat.parse(parameterQuantity).intValue();
        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }
        return quantity;
    }
}