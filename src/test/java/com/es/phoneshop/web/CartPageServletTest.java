package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    private final String CART_PAGE = "/WEB-INF/pages/cart.jsp";
    private final String QUANTITY_PARAMETER = "quantity";
    private final String PRODUCT_ID_PARAMETER = "productId";
    private final String PRODUCT_ID_STRING_TEST = "500";

    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;

    private CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(CART_PAGE)).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.getDefault());
    }

    @Test
    public void shouldForwardWhenDoGet() throws ServletException, IOException {
        servlet.init(config);

        servlet.doGet(request, response);

        verify(request.getRequestDispatcher(CART_PAGE)).forward(request, response);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenInvalidProductID() throws ServletException, IOException {
        servlet.init(config);
        when(request.getParameterValues(PRODUCT_ID_PARAMETER)).thenReturn(new String[]{PRODUCT_ID_STRING_TEST});
        when(request.getParameterValues(QUANTITY_PARAMETER)).thenReturn(new String[]{PRODUCT_ID_STRING_TEST});

        servlet.doPost(request, response);
    }

    @Test
    public void shouldDoPostWhenCorrectAllData() throws ServletException, IOException, OutOfStockException {
        servlet.init(config);

        cartService.update(any(), anyLong(), anyInt());
    }
}