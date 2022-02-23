package com.es.phoneshop.service.order.impl;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.order.OrderService;
import org.apache.commons.lang3.SerializationUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    private final Object lock = new Object();
    private OrderDao orderDao;


    private static volatile OrderService instance;

    public OrderServiceImpl() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (OrderService.class) {
                if (instance == null) {
                    instance = new OrderServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();

        order.setCartItems(SerializationUtils.clone((ArrayList<CartItem>) cart.getCartItems()));
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(getDeliveryCost());
        order.setTotalCost(calculateTotalCost(order.getSubTotal(), order.getDeliveryCost()));

        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal getDeliveryCost() {
        return new BigDecimal(5);
    }

    private BigDecimal calculateTotalCost(BigDecimal subtotal, BigDecimal deliveryCost) {
        return subtotal.add(deliveryCost);
    }
}
