package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private final String PRODUCT_ATTRIBUTE = "product";
    private final String PRODUCT_DETAILS_PAGE = "/WEB-INF/pages/product.jsp";
    private final int START_INDEX_WITHOUT_SLASH = 1;
    private ProductDao productDao;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(Long.valueOf(productId)));
        request.getRequestDispatcher(PRODUCT_DETAILS_PAGE).forward(request, response);
    }
}