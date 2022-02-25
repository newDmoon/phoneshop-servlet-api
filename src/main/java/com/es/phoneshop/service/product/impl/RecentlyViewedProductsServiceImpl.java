package com.es.phoneshop.service.product.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.RecentlyViewedProductsService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class RecentlyViewedProductsServiceImpl implements RecentlyViewedProductsService {
    private final String SESSION_RECENTLY_VIEWED_PRODUCTS_ATTRIBUTE
            = RecentlyViewedProductsServiceImpl.class.getName() + ".recentlyViewedProducts";
    private final int MAX_AMOUNT_RECENT_PRODUCTS = 3;
    private static volatile RecentlyViewedProductsService instance;
    private ProductDao productDao;
    private static Object lock = new Object();

    private RecentlyViewedProductsServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static RecentlyViewedProductsService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new RecentlyViewedProductsServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public ArrayList<Product> getRecentlyViewedProducts(HttpServletRequest request) {
        synchronized (lock) {
            ArrayList<Product> recentlyViewedProducts
                    = (ArrayList<Product>) request.getSession().getAttribute(SESSION_RECENTLY_VIEWED_PRODUCTS_ATTRIBUTE);
            if (recentlyViewedProducts == null) {
                request.getSession().setAttribute(SESSION_RECENTLY_VIEWED_PRODUCTS_ATTRIBUTE,
                        recentlyViewedProducts = new ArrayList<>(MAX_AMOUNT_RECENT_PRODUCTS));
            }
            return recentlyViewedProducts;
        }
    }

    @Override
    public void addToRecentlyViewed(ArrayList<Product> recentlyViewedProducts, Long productId) {
        Product product = productDao.getById(productId);
        if (recentlyViewedProducts == null) {
            throw new IllegalArgumentException();
        }
        if (recentlyViewedProducts.stream()
                .noneMatch(productItem -> productItem.equals(product))) {
            if (recentlyViewedProducts.size() == MAX_AMOUNT_RECENT_PRODUCTS) {
                recentlyViewedProducts.remove(recentlyViewedProducts.get(0));
            }
            recentlyViewedProducts.add(product);
        }
    }
}