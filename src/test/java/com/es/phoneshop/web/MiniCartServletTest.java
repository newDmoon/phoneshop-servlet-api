package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest extends HttpServlet {
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

    private final String CART_PAGE = "/WEB-INF/pages/minicart.jsp";
    private final String CART_ATTRIBUTE = "cart";
    MiniCartServlet servlet = new MiniCartServlet();

    @Before
    public void setUp() throws Exception {
        servlet.init(config);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void shouldInvokeRequestDispatcherWhenCorrectInitialPage() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq(CART_PAGE));
    }

    @Test
    public void shouldSetAttributeWhenDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(CART_ATTRIBUTE), any());
    }

    @Test
    public void shouldInvokeIncludeWhenDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher, times(1)).include(request, response);
    }
}