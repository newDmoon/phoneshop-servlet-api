package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends ArrayListGenericDao<Order> implements OrderDao {
    private static volatile OrderDao instance;
    private static long maxId = 0;

    private final Object lock = new Object();

    private ArrayListOrderDao() {
        maxId = getList().size();
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            synchronized (OrderDao.class) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        synchronized (lock) {
            if (secureId != null) {
                return getList().stream()
                        .filter(order -> secureId.equals(order.getSecureId()))
                        .findAny()
                        .orElseThrow(OrderNotFoundException::new);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}