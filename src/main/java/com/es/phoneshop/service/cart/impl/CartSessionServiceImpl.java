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
import java.util.stream.Collectors;

public class CartSessionServiceImpl implements CartService {
    private static final String SESSION_CART = "sessionCart";
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
            Product product = productDao.getProduct(productId);
            Optional<CartItem> cartItemOptional = find(cart, productId, quantity);
            if (cartItemOptional.isPresent()) {
                CartItem cartItemToRewrite = new CartItem(cartItemOptional.get().getProduct(),
                        cartItemOptional.get().getQuantity() + quantity);
                cart.getCartItems().remove(cartItemOptional.get());
                cart.getCartItems().add(cartItemToRewrite);
            } else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
            recalculateCartTotalQuantity(cart);
            recalculateCartTotalCost(cart);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (lock) {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> cartItemOptional = find(cart, productId, quantity);
            if (cartItemOptional.isPresent()) {
                cartItemOptional.get().setQuantity(quantity);
            } else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
            recalculateCartTotalQuantity(cart);
            recalculateCartTotalCost(cart);
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getCartItems().removeIf(cartItem ->
                productId.equals(cartItem.getProduct().getId()));
        recalculateCartTotalQuantity(cart);
        recalculateCartTotalCost(cart);
    }

    private Optional<CartItem> find(Cart cart, Long productId, int quantity) throws OutOfStockException {
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
        return cartItemOptional;
    }

    private void recalculateCartTotalQuantity(Cart cart){
        cart.setTotalQuantity(cart.getCartItems().stream()
                .map(CartItem::getQuantity).mapToInt(cartItemQuality -> cartItemQuality).sum());
    }

    private void recalculateCartTotalCost(Cart cart){
//        cart.setTotalCost(cart.getCartItems().stream()
//                .map(CartItem::getProduct)
//                .map(Product::getPrice)
//                .map());
    }
}
