package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
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
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest extends HttpServlet {
    private final String ORDER_OVERVIEW_PAGE = "/WEB-INF/pages/orderOverview.jsp";
    private final String ORDER_ATTRIBUTE = "order";
    private final String SECURE_ID_PATH = "/234";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @InjectMocks
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    @Mock
    private OrderDao orderDao;

    @Before
    public void setup() {
        when(request.getPathInfo()).thenReturn(SECURE_ID_PATH);
        when(request.getRequestDispatcher(ORDER_OVERVIEW_PAGE)).thenReturn(requestDispatcher);
    }

    @Test
    public void shouldSetAttributeWhenInvokeDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ORDER_ATTRIBUTE), any());
    }

    @Test
    public void shouldForwardWhenInvokeDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSecureIdIsNull() throws ServletException, IOException {
        servlet.init(servletConfig);
        orderDao = ArrayListOrderDao.getInstance();

        orderDao.getOrderBySecureId(null);
    }
}