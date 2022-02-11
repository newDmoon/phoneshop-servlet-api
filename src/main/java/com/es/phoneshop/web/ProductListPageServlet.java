package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.util.SortField;
import com.es.phoneshop.model.product.util.SortOrder;
import com.es.phoneshop.service.product.RecentlyViewedProductsService;
import com.es.phoneshop.service.product.impl.RecentlyViewedProductsServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private final String PRODUCTS_ATTRIBUTE = "products";
    private final String RECENT_PRODUCTS_ATTRIBUTE = "recentlyViewedProducts";
    private final String PRODUCT_LIST_PAGE = "/WEB-INF/pages/productList.jsp";
    private final String QUERY_PARAM = "query";
    private final String SORT_PARAM = "sort";
    private final String ORDER_PARAM = "order";
    private ProductDao productDao;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedProductsService = RecentlyViewedProductsServiceImpl.getInstance();
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
}