package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.NoSuchElementException;

public interface OrderDao {
    Order getOrder(Long id) throws NoSuchElementException;

    Order getOrderBySecureId(String secureId) throws OrderNotFoundException;

    void save(Order order);
}
