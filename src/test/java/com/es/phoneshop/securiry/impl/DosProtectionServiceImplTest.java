package com.es.phoneshop.securiry.impl;

import com.es.phoneshop.securiry.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosProtectionServiceImplTest {
    private final Long NORMAL_AMOUNT_OF_REQUESTS_TEST = 5L;
    private final Long INCORRECT_AMOUNT_OF_REQUESTS_TEST = 25L;
    private final String IP_TEST = "128.0.0.1";
    private final LocalDateTime INCORRECT_DATE_TEST = LocalDateTime.of(1, 1, 1, 1, 1);

    @InjectMocks
    private DosProtectionService dosProtectionService = DosProtectionServiceImpl.getInstance();
    @Mock
    private Map<String, Long> amountOfRequestsMap = new ConcurrentHashMap();
    @Mock
    private Map<String, LocalDateTime> lastRequestDateMap = new ConcurrentHashMap();

    @Before
    public void setup() {
        when(amountOfRequestsMap.get(anyString())).thenReturn(NORMAL_AMOUNT_OF_REQUESTS_TEST);
        when(lastRequestDateMap.get(anyString())).thenReturn(LocalDateTime.now());
    }

    @Test
    public void shouldReturnTrueWhenAmountOfRequestsLessThanThreshold() {
        assertTrue(dosProtectionService.isAllowed(IP_TEST));
    }

    @Test
    public void shouldReturnFalseWhenAmountOfRequestsMoreThanThreshold() {
        when(amountOfRequestsMap.get(anyString())).thenReturn(INCORRECT_AMOUNT_OF_REQUESTS_TEST);

        assertFalse(dosProtectionService.isAllowed(IP_TEST));
    }

    @Test
    public void shouldReturnTrueWhenLastRequestDateMoreThanMinuteFromNow() {
        when(lastRequestDateMap.get(anyString())).thenReturn(INCORRECT_DATE_TEST);

        assertTrue(dosProtectionService.isAllowed(IP_TEST));
    }
}