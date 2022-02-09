package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilderImpl;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    private static final String SLASH = "/";
    private final Currency USD = Currency.getInstance("USD");
    private final Long idTest = 1L;
    private final String codeTest = "test-product";
    private final String descriptionTest = "Samsung Galaxy S";
    private final BigDecimal priceTest = new BigDecimal(100);
    private final int stockTest = 100;
    private final String imageUrlTest = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final String INVALID_STRING_PATH = "/sdfs";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    private ProductDao productDao;
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        productDao = ArrayListProductDao.getInstance();
        Product testProduct = new ProductBuilderImpl().setId(idTest)
                .setCode(codeTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        productDao.save(testProduct);
        when(request.getPathInfo()).thenReturn(SLASH + idTest);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionWhenInvalidIdProduct() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(INVALID_STRING_PATH);
        servlet.doGet(request, response);
    }
}
