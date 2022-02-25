package com.es.phoneshop.service.order.impl;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.impl.CartSessionServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import org.apache.commons.lang3.SerializationUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    private static volatile OrderService instance;
    private final String CONSTANT_PROPERTY = "constant";
    private final String DELIVERY_COST_VALUE_PROPERTY = "constant.deliveryCost.value";
    private final Object lock = new Object();

    private ResourceBundle constants;
    private CartService cartService;
    private OrderDao orderDao;

    public OrderServiceImpl() {
        constants = ResourceBundle.getBundle(CONSTANT_PROPERTY);
        orderDao = ArrayListOrderDao.getInstance();
        cartService = CartSessionServiceImpl.getInstance();
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
        return completeOrder(order, cart);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order, HttpServletRequest request) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        cartService.clearCart(request);
    }

    private BigDecimal getDeliveryCost() {
        return BigDecimal.valueOf(Long.parseLong(constants.getString(DELIVERY_COST_VALUE_PROPERTY)));
    }

    private BigDecimal calculateTotalCost(BigDecimal subtotal, BigDecimal deliveryCost) {
        return subtotal.add(deliveryCost);
    }

    private Order completeOrder(Order order, Cart cart) {
        order.setCartItems(SerializationUtils.clone((ArrayList<CartItem>) cart.getCartItems()));
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(getDeliveryCost());
        order.setTotalCost(calculateTotalCost(order.getSubTotal(), order.getDeliveryCost()));
        return order;
    }
}
