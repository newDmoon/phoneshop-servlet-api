package com.es.phoneshop.model.product;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProductDao {
    Product getProduct(Long id) throws NoSuchElementException;
    List<Product> findProducts();
    void save(Product product);
    void delete(Long id) throws NoSuchElementException;
}
