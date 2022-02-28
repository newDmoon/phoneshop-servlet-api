package com.es.phoneshop.securiry.impl;

import com.es.phoneshop.securiry.DosProtectionService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.temporal.ChronoUnit.MINUTES;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static volatile DosProtectionService instance;
    private static Object lock = new Object();

    private final String CONSTANT_PROPERTY = "constant";
    private final String CONSTANT_THRESHOLD_VALUE_PROPERTY = "constant.threshold.value";
    private final String CONSTANT_MINUTES_LIMIT_VALUE_PROPERTY = "constant.minutesLimit.value";

    private ResourceBundle constant = ResourceBundle.getBundle(CONSTANT_PROPERTY);
    private Map<String, Long> amountOfRequestsMap = new ConcurrentHashMap();
    private Map<String, LocalDateTime> lastRequestDateMap = new ConcurrentHashMap();

    private DosProtectionServiceImpl() {
    }

    public static DosProtectionService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DosProtectionServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long counter = amountOfRequestsMap.get(ip);
        LocalDateTime lastDate = lastRequestDateMap.get(ip);
        LocalDateTime currentDate = LocalDateTime.now();
        long minutesActive = calculateActiveTime(currentDate, lastDate);

        if (counter == null || lastDate == null
                || minutesActive > Integer.parseInt(constant.getString(CONSTANT_MINUTES_LIMIT_VALUE_PROPERTY))) {
            lastDate = LocalDateTime.now();
            counter = 1L;
        } else {
            if (counter > Long.parseLong(constant.getString(CONSTANT_THRESHOLD_VALUE_PROPERTY))
                    && minutesActive < Integer.parseInt(constant.getString(CONSTANT_MINUTES_LIMIT_VALUE_PROPERTY))) {
                return false;
            }
            counter++;
        }

        lastRequestDateMap.put(ip, lastDate);
        amountOfRequestsMap.put(ip, counter);
        return true;
    }

    private long calculateActiveTime(LocalDateTime currentDate, LocalDateTime lastDate) {
        if (lastDate == null) {
            return 0;
        }
        return MINUTES.between(lastDate, currentDate);
    }
}