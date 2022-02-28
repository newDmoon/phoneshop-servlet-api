package com.es.phoneshop.service.order.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilderImpl;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    private final Currency USD = Currency.getInstance("USD");
    private final String codeTest = "test-product";
    private final String descriptionTest = "Samsung Galaxy S";
    private final BigDecimal priceTest = new BigDecimal(100);
    private final int stockTest = 100;
    private final String imageUrlTest = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final Long productIdTest = 2L;
    private final int quantityNormalTest = 3;
    private final BigDecimal TOTAL_COST_TEST = new BigDecimal(5);
    private final int TOTAL_QUANTITY_TEST = 5;

    @Mock
    private HttpServletRequest request;
    @Mock
    private CartService cartService;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    public OrderServiceImplTest() {
    }

    @Before
    public void setup() {

    }

    @Test
    public void shouldEqualsTwoWhenGetPaymentMethods() {
        List<PaymentMethod> testList = orderService.getPaymentMethods();

        assertEquals(2, testList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCartIsNull() {
        orderService.getOrder(null);
    }

    @Test
    public void shouldWorkCorrectWhenCorrectInput() {
        Cart cart = new Cart();
        cart.setTotalCost(TOTAL_COST_TEST);
        cart.setTotalQuantity(TOTAL_QUANTITY_TEST);
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setId(productIdTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        CartItem cartItem = new CartItem(productTest, quantityNormalTest);
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        cart.setCartItems(cartItemList);

        Order order = orderService.getOrder(cart);

        orderService.placeOrder(order, request);

        verify(cartService, times(1)).clearCart(request);
    }
}