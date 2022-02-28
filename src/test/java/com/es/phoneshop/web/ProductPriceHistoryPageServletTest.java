package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.ProductBuilderImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryPageServletTest extends HttpServlet {
    private final String PRODUCT_ATTRIBUTE = "product";
    private final String CORRECT_ID_PATH_TEST = "/1";
    private final String INCORRECT_ID_PATH_TEST = "/250";
    private final Long ID_TEST = 1L;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private ServletConfig config;
    @InjectMocks
    private ProductPriceHistoryPageServlet servlet = new ProductPriceHistoryPageServlet();
    @Mock
    private ProductDao productDao;

    @Before
    public void setup() throws ServletException {
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenIncorrectProductId() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(INCORRECT_ID_PATH_TEST);
        servlet.init(config);

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute(anyString(), any());
        verify(request, times(1)).getRequestDispatcher(anyString());
    }

    @Test
    public void shouldVerifyMethodsWhenDoGetInvoked() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(CORRECT_ID_PATH_TEST);
        productDao.saveItem(new ProductBuilderImpl().setId(ID_TEST).build());

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute(PRODUCT_ATTRIBUTE, eq(any()));
        verify(request, times(1)).getRequestDispatcher(anyString());
    }
}