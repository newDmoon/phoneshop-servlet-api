package com.es.phoneshop.service.cart.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilderImpl;
import com.es.phoneshop.service.cart.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class CartSessionServiceImplTest {
    private final Currency USD = Currency.getInstance("USD");
    private final String codeTest = "test-product";
    private final String descriptionTest = "Samsung Galaxy S";
    private final BigDecimal priceTest = new BigDecimal(100);
    private final int stockTest = 100;
    private final String imageUrlTest = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final Long productIdTest = 2L;
    private final int quantityNormalTest = 3;

    @Mock
    private CartService cartService;
    @Mock
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        cartService = CartSessionServiceImpl.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInvalidId() throws OutOfStockException {
        Cart cart = new Cart();

        cartService.add(cart, null, quantityNormalTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInvalidCart() throws OutOfStockException {
        cartService.add(null, productIdTest, quantityNormalTest);
    }

    @Test
    public void shouldAddToCartWhenInputCorrectData() throws OutOfStockException {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setId(productIdTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        Cart cart = new Cart();
        productDao.save(productTest);

        cartService.add(cart, productIdTest, 0);

        assertFalse(cart.getCartItems().isEmpty());
    }

    @Test
    public void shouldBeTrueWhenAddTwoSameProducts() throws OutOfStockException {
        int expectedSize = 1;
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setId(productIdTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        Cart cart = new Cart();
        productDao.save(productTest);

        cartService.add(cart, productIdTest, quantityNormalTest);
        cartService.add(cart, productIdTest, quantityNormalTest);

        assertEquals(expectedSize, cart.getCartItems().size());
    }
}
