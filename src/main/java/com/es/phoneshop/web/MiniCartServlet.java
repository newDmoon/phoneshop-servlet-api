package com.es.phoneshop.web;

import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO tests
public class MiniCartServlet extends HttpServlet {
    private final String CART_PAGE = "/WEB-INF/pages/minicart.jsp";
    private final String CART_ATTRIBUTE = "cart";
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartSessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request));

        request.getRequestDispatcher(CART_PAGE).include(request, response);
    }
}