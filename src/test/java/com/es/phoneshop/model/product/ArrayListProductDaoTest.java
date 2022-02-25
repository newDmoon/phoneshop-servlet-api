package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

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
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void shouldFindProductsNoResults() {
        assertTrue(productDao.findProducts(codeTest, null, null).isEmpty());
    }

    @Test
    public void shouldGetProductWhenGoodInput() {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();

        productDao.save(productTest);

        assertNotNull(productDao.getById(productTest.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenGetNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            productDao.getById(null);
        });
    }

    @Test
    public void shouldSaveProductIfExistsWithIdMoreThanZero() {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();

        productDao.save(productTest);
        Product result = productDao.getById(Long.valueOf(productTest.getId()));

        assertTrue(productTest.getId() > 0);
        assertNotNull(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldDeleteProductIfExistsThrowsException() {
        Product productTest = new ProductBuilderImpl().setCode(codeTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();

        productDao.save(productTest);
        productDao.delete(productTest.getId());
        productDao.getById(productTest.getId());
    }
}
