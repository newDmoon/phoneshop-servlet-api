package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
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
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    // TODO why static final fields?
    // TODO tests
    private final String PRODUCTS_ATTRIBUTE = "products";
    private final String RECENT_PRODUCTS_ATTRIBUTE = "recentlyViewedProducts";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String QUERY_PARAM = "query";
    private final String SORT_PARAM = "sort";
    private final String ORDER_PARAM = "order";
    private final String PRODUCT_LIST_PAGE = "/WEB-INF/pages/productList.jsp";
    private final int START_INDEX_WITHOUT_SLASH = 1;

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedProductsService = RecentlyViewedProductsServiceImpl.getInstance();
        cartService = CartSessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(QUERY_PARAM);
        String sortField = request.getParameter(SORT_PARAM);
        String sortOrder = request.getParameter(ORDER_PARAM);
        ArrayList<Product> recentlyViewedProducts = recentlyViewedProductsService.getRecentlyViewedProducts(request);
        request.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentlyViewedProducts);
        request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
        ));
        request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
    }

    // TODO post method
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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