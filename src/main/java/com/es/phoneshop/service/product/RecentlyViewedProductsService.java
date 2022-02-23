package com.es.phoneshop.service.product;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface RecentlyViewedProductsService {
    ArrayList<Product> getRecentlyViewedProducts(HttpServletRequest request);

    void addToRecentlyViewed(ArrayList<Product> recentlyViewedProducts, Long productId);
}
