package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ArrayListProductDaoTest {
    private final Currency USD = Currency.getInstance("USD");
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void SaveProduct_IfExistsWithIdMoreThanZero_AssertCompile() {
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertTrue(product.getId() > 0);
        Product result = productDao.getProduct(Long.valueOf(product.getId()));
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test(expected = NoSuchElementException.class)
    public void DeleteProduct_IfExists_ThrowsException() {
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        productDao.delete(product.getId());
        productDao.getProduct(product.getId());
    }
}
