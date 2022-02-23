package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOrderDao implements OrderDao {
    private final Object lock = new Object();

    private static volatile OrderDao instance;
    private static long maxId = 0;

    private List<Order> orderList;

    private ArrayListOrderDao() {
        this.orderList = new ArrayList<>();
        maxId = orderList.size();
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
    public Order getOrder(Long id) throws OrderNotFoundException {
        synchronized (lock) {
            if (id != null) {
                return orderList.stream()
                        .filter(order -> id.equals(order.getId()))
                        .findAny()
                        .orElseThrow(OrderNotFoundException::new);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        synchronized (lock) {
            if (secureId != null) {
                return orderList.stream()
                        .filter(order -> secureId.equals(order.getSecureId()))
                        .findAny()
                        .orElseThrow(OrderNotFoundException::new);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public void save(Order order) {
        synchronized (lock) {
            if (order != null) {
                if (order.getId() == null) {
                    order.setId(maxId++);
                    orderList.add(order);
                } else {
                    if (orderList.stream()
                            .anyMatch(productElem -> order.getId().equals(productElem.getId()))) {
                        orderList.set(orderList.indexOf(orderList.stream()
                                .filter(productElem -> order.getId().equals(productElem.getId()))
                                .findAny()
                                .get()), order);
                    } else {
                        orderList.add(order);
                    }
                }
            } else throw new IllegalArgumentException();
        }
    }
}