package com.es.phoneshop.web;

import com.es.phoneshop.service.cart.CartService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest extends HttpServlet {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private ServletConfig config;
    @Mock
    private CartService cartService;
    @InjectMocks
    private DeleteCartItemServlet servlet = new DeleteCartItemServlet();

    private final String MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART = "/cart?message=Cart item removed successfully";
    private final String PATH_INFO = "/5";
    private final int WANTED_INVOCATION_TIMES = 0;

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn(PATH_INFO);
    }

    @Test
    public void shouldGetCartWhenDoPost() throws ServletException, IOException {
        servlet.init(config);

        servlet.doPost(request, response);

        verify(cartService, times(WANTED_INVOCATION_TIMES)).getCart(any());
    }

    @Test
    public void shouldDeleteWhenDoPost() throws ServletException, IOException {
        servlet.init(config);

        servlet.doPost(request, response);

        verify(cartService, times(WANTED_INVOCATION_TIMES)).delete(any(), any());
    }

    @Test
    public void shouldRedirectWhenCorrectPath() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(eq(String.join(StringUtils.EMPTY,
                request.getContextPath(),
                MESSAGE_PARAMETER_SUCCESS_PRODUCT_ADD_TO_CART)));
    }
}