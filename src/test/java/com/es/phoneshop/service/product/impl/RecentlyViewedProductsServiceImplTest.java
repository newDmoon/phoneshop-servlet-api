package com.es.phoneshop.service.product.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilderImpl;
import com.es.phoneshop.service.product.RecentlyViewedProductsService;
import com.es.phoneshop.web.ProductDetailsPageServlet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecentlyViewedProductsServiceImplTest {
    private final Currency USD = Currency.getInstance("USD");
    private final String codeTest = "test-product";
    private final String descriptionTest = "Samsung Galaxy S";
    private final BigDecimal priceTest = new BigDecimal(100);
    private final int stockTest = 100;
    private final String imageUrlTest = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final Long productIdTest = 2L;

    @Mock
    private HttpServletRequest request;
    @Mock
    private RecentlyViewedProductsService recentlyViewedProductsService;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        when(request.getSession()).thenReturn(session);
        recentlyViewedProductsService = RecentlyViewedProductsServiceImpl.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenRecentlyViewedProductsIsNull() {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setId(productIdTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        productDao.save(productTest);

        recentlyViewedProductsService.addToRecentlyViewed(null, productIdTest);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenInvalidProductId() {
        ArrayList<Product> recentlyViewedProducts = new ArrayList<>();

        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullProductId() {
        ArrayList<Product> recentlyViewedProducts = new ArrayList<>();

        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, null);
    }

    @Test
    public void shouldHaveSizeOneWhenAddSameProducts() {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setId(productIdTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        productDao.save(productTest);
        ArrayList<Product> recentlyViewedProducts = new ArrayList<>();

        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);
        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);
        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);
        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);
        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);

        Assert.assertEquals(1, recentlyViewedProducts.size());
    }

    @Test
    public void shouldAddProductToListWhenCorrectData() {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setId(productIdTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        productDao.save(productTest);
        ArrayList<Product> recentlyViewedProducts = new ArrayList<>();

        recentlyViewedProductsService.addToRecentlyViewed(recentlyViewedProducts, productIdTest);

        Assert.assertFalse((recentlyViewedProducts.isEmpty()));
    }
}