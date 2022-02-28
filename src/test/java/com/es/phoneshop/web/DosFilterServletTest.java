package com.es.phoneshop.web;

import com.es.phoneshop.securiry.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterServletTest {
    @InjectMocks
    private DosFilterServlet servlet = new DosFilterServlet();
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private DosProtectionService dosProtectionService;

    @Before
    public void setup() {
        when(request.getRemoteAddr()).thenReturn("");
    }

    @Test
    public void shouldInvokeDoFilterOneTimeWhenIsAllowedReturnsTrue() throws ServletException, IOException {
        servlet.init(filterConfig);
        servlet.doFilter(request, response, filterChain);

        verify((filterChain), times(1)).doFilter(request, response);
    }

    @Test
    public void shouldInvokeSetStatusWhenIsAllowedReturnsFalse() throws ServletException, IOException {
        when(dosProtectionService.isAllowed("")).thenReturn(false);

        servlet.doFilter(request, response, filterChain);

        verify(response, times(1)).setStatus(anyInt());
    }
}
