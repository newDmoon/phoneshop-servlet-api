package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class OrderOverviewPageServlet extends HttpServlet {
    private final String ORDER_OVERVIEW_PAGE = "/WEB-INF/pages/orderOverview.jsp";
    private final String ORDER_ATTRIBUTE = "order";
    private final int START_INDEX_WITHOUT_SLASH = 1;
    private final String BASE_NAME_PATH = "error";
    private OrderDao orderDao;
    private Locale currentLocale;
    private ResourceBundle messages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        currentLocale = Locale.getDefault();
        messages = ResourceBundle.getBundle(BASE_NAME_PATH, currentLocale);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);
        request.setAttribute(ORDER_ATTRIBUTE, orderDao.getOrderBySecureId(secureOrderId));

        request.getRequestDispatcher(ORDER_OVERVIEW_PAGE).forward(request, response);
    }
}