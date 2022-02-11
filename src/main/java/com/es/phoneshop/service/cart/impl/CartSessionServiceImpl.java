package com.es.phoneshop.service.cart.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CartSessionServiceImpl implements CartService {
    private static final String SESSION_CART = CartSessionServiceImpl.class.getName() + ".cart";
    private final Object lock = new Object();

    private ProductDao productDao;

    private static volatile CartService instance;

    public CartSessionServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        if (instance == null) {
            synchronized (CartService.class) {
                if (instance == null) {
                    instance = new CartSessionServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        synchronized (lock) {
            Cart cart = (Cart) request.getSession().getAttribute(SESSION_CART);
            if (cart == null) {
                request.getSession().setAttribute(SESSION_CART, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (lock) {
            if (cart == null) {
                throw new IllegalArgumentException();
            }
            Product product = productDao.getProduct(productId);
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny();
            if (cartItemOptional.isPresent()) {
                CartItem cartItemToRewrite = new CartItem(cartItemOptional.get().getProduct(),
                        cartItemOptional.get().getQuantity() + quantity);
                cart.getCartItems().remove(cartItemOptional.get());
                cart.getCartItems().add(cartItemToRewrite);
            } else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
        }
    }
}
