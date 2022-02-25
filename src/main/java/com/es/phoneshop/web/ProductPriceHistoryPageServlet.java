package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private final String PRODUCT_ATTRIBUTE = "product";
    private final String PRODUCT_PRICE_HISTORY_PAGE = "/WEB-INF/pages/productPriceHistory.jsp";
    private final int START_INDEX_WITHOUT_SLASH = 1;
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);

        req.setAttribute(PRODUCT_ATTRIBUTE, productDao.getById(Long.valueOf(productId)));

        req.getRequestDispatcher(PRODUCT_PRICE_HISTORY_PAGE).forward(req, resp);
    }
}