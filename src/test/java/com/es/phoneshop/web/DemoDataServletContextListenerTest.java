package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.PriceHistoryItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {
    private final int EXCEPTED_TIMES_SAVING_PRODUCT_HISTORY_ITEM = 3;
    private final int EXCEPTED_TIMES_SAVING_PRODUCT_ITEM = 13;
    private final String CONTEXT_PARAM_INSERT_DEMO_DATA = "insertDemoData";

    @InjectMocks
    private DemoDataServletContextListener listener = new DemoDataServletContextListener();
    @Mock
    private ServletContextEvent servletContextEvent;
    @Mock
    private ProductDao productDao;
    @Mock
    private ServletContext servletContext;
    @Mock
    private List<PriceHistoryItem> priceHistoryItems;

    @Before
    public void setup() {
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
    }

    @Test
    public void shouldSavePriceHistoryItemsThreeTimesWhenCorrectInvocationSavePriceHistoryItems() {
        listener.savePriceHistoryItems();

        verify(priceHistoryItems, times(EXCEPTED_TIMES_SAVING_PRODUCT_HISTORY_ITEM)).add(any());
    }

    @Test
    public void shouldSaveSampleProducts() {
        listener.saveSampleProducts();

        verify(productDao, times(EXCEPTED_TIMES_SAVING_PRODUCT_ITEM)).saveItem(any());
    }

    @Test
    public void shouldInvokeContextInitialized() {
        when(servletContext.getInitParameter(CONTEXT_PARAM_INSERT_DEMO_DATA)).thenReturn("true");

        listener.contextInitialized(servletContextEvent);

        verify(priceHistoryItems, times(EXCEPTED_TIMES_SAVING_PRODUCT_HISTORY_ITEM)).add(any());
    }
}