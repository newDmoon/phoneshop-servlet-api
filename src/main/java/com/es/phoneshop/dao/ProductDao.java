package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.util.SortField;
import com.es.phoneshop.model.product.util.SortOrder;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProductDao {
    Product getProduct(Long id) throws NoSuchElementException;

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product);

    void delete(Long id);
}
