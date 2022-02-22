package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private final int START_INDEX_WITHOUT_SLASH = 1;
    private final String MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART = "/cart?message=Cart item removed successfully";
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartSessionServiceImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(START_INDEX_WITHOUT_SLASH);
        Cart cart = cartService.getCart(request);

        cartService.delete(cart, Long.valueOf(productId));

        response.sendRedirect(request.getContextPath() + MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART);
    }
}