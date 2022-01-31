package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private final String PRODUCTS_ATTRIBUTE = "products";
    private final String PRODUCT_LIST_PAGE = "/WEB-INF/pages/productList.jsp";
    private ProductDao productDao;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = new ArrayListProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findProducts());
        request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
    }
}