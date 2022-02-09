package com.es.phoneshop.service.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private final Object lock = new Object();

    private ProductDao productDao;

    private static volatile CartService instance;

    public DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        if (instance == null) {
            synchronized (CartService.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        synchronized (lock){
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if(cart==null){
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;

        }
    }


    // TODO check products already in cart (lecture 3 - 1:18:05)
    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (lock){
            Product product = productDao.getProduct(productId);
            if(product.getStock() < quantity){
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            cart.getCartItems().add(new CartItem(product, quantity));
        }
    }
}
