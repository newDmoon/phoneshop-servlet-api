package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private final Currency USD = Currency.getInstance("USD");
    private final String codeTest = "test-product";
    private final String descriptionTest = "Samsung Galaxy S";
    private final BigDecimal priceTest = new BigDecimal(100);
    private final int stockTest = 100;
    private final String imageUrlTest = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void shouldFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void shouldGetProductWhen() {
        Product productTest = new Product(codeTest, descriptionTest, priceTest, USD, stockTest, imageUrlTest);
        productDao.save(productTest);
        assertNotNull(productDao.getProduct(productTest.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenGetNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            productDao.getProduct(null);
        });
    }

    @Test
    public void shouldSaveProductIfExistsWithIdMoreThanZero() {
        Product productTest = new Product(codeTest, descriptionTest, priceTest, USD, stockTest, imageUrlTest);
        productDao.save(productTest);
        assertTrue(productTest.getId() > 0);
        Product result = productDao.getProduct(Long.valueOf(productTest.getId()));
        assertNotNull(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldDeleteProductIfExistsThrowsException() {
        Product productTest = new Product(codeTest, descriptionTest, priceTest, USD, stockTest, imageUrlTest);
        productDao.save(productTest);
        productDao.delete(productTest.getId());
        productDao.getProduct(productTest.getId());
    }
}
