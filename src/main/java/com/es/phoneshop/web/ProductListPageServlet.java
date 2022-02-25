package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.util.SortField;
import com.es.phoneshop.model.product.util.SortOrder;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ProductListPageServlet extends HttpServlet {
    private final String PRODUCTS_ATTRIBUTE = "products";
    private final String RECENT_PRODUCTS_ATTRIBUTE = "recentlyViewedProducts";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String PRODUCT_ID_PARAMETER = "productId";
    private final String QUERY_PARAMETER = "query";
    private final String SORT_PARAMETER = "sort";
    private final String ORDER_PARAMETER = "order";
    private final String ERROR_PARAMETER = "error";
    private final String ERROR_STOCK_MESSAGE = "error.outOfStock.message";
    private final String ERROR_NUMBER_FORMAT_MESSAGE = "error.numberFormat.message";
    private final String ERROR_NON_POSITIVE_MESSAGE = "error.nonPositive.message";
    private final String PRODUCTS_PATH = "/products";
    private final String REDIRECT_FORMAT = "%s%s";
    private final String PRODUCT_LIST_PAGE = "/WEB-INF/pages/productList.jsp";
    private final String DIGIT_REGEX = "^\\d+$";

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;
    private Locale currentLocale;
    private ResourceBundle messages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        currentLocale = Locale.getDefault();
        messages = ResourceBundle.getBundle(ERROR_PARAMETER, currentLocale);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedProductsService = RecentlyViewedProductsServiceImpl.getInstance();
        cartService = CartSessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(QUERY_PARAMETER);
        String sortField = request.getParameter(SORT_PARAMETER);
        String sortOrder = request.getParameter(ORDER_PARAMETER);
        ArrayList<Product> recentlyViewedProducts = recentlyViewedProductsService.getRecentlyViewedProducts(request);

        request.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentlyViewedProducts);
        request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
        ));

        request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter(QUANTITY_PARAMETER);
        String productIdString = request.getParameter(PRODUCT_ID_PARAMETER);

        try {
            int quantity = parseQuantity(request, quantityString);
            Long productId = parseProductId(productIdString);
            cartService.add(cartService.getCart(request), productId, quantity);
        } catch (ParseException exception) {
            request.setAttribute(ERROR_PARAMETER, messages.getString(ERROR_NUMBER_FORMAT_MESSAGE));
            doGet(request, response);
        } catch (IllegalArgumentException exception) {
            request.setAttribute(ERROR_PARAMETER, messages.getString(ERROR_NON_POSITIVE_MESSAGE));
            doGet(request, response);
        } catch (OutOfStockException exception) {
            Integer availableStock = exception.getStockAvailable();
            request.setAttribute(ERROR_PARAMETER, String.join(StringUtils.SPACE,
                    messages.getString(ERROR_STOCK_MESSAGE), availableStock.toString()));
            doGet(request, response);
        }

        response.sendRedirect(String.format(REDIRECT_FORMAT, request.getContextPath(), PRODUCTS_PATH));
    }

    private int parseQuantity(HttpServletRequest request, String quantityString) throws ParseException {
        if (!validateInteger(quantityString)) {
            throw new NumberFormatException();
        }
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());

        return numberFormat.parse(quantityString).intValue();
    }

    private Long parseProductId(String productIdString) throws NumberFormatException {
        return Long.valueOf(productIdString);
    }

    private boolean validateInteger(String stringToCheck) {
        return Pattern.matches(DIGIT_REGEX, stringToCheck);
    }
}