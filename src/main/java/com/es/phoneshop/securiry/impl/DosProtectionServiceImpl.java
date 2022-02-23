package com.es.phoneshop.securiry.impl;

import com.es.phoneshop.securiry.DosProtectionService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.temporal.ChronoUnit.MINUTES;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private static final long MINUTES_LIMIT = 1L;
    private static volatile DosProtectionService instance;
    private static Object lock = new Object();
    private Map<String, Long> countMap = new ConcurrentHashMap();
    private Map<String, LocalDateTime> timeMap = new ConcurrentHashMap();

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

    private DosProtectionServiceImpl() {
    }

    @Override
    public boolean isAllowed(String ip) {
        Long counter = countMap.get(ip);
        LocalDateTime lastDate = timeMap.get(ip);
        LocalDateTime currentDate = LocalDateTime.now();

        long minutesActive = calculateActiveTime(currentDate, lastDate);

        if (counter == null || lastDate == null || minutesActive > MINUTES_LIMIT) {
            lastDate = LocalDateTime.now();
            counter = 1L;
        } else {
            if (counter > THRESHOLD && minutesActive < MINUTES_LIMIT) {
                return false;
            }
            counter++;
        }
        timeMap.put(ip, lastDate);
        countMap.put(ip, counter);
        return true;
    }

    private long calculateActiveTime(LocalDateTime currentDate, LocalDateTime lastDate) {
        if (lastDate == null) {
            return 0;
        }
        return MINUTES.between(lastDate, currentDate);
    }
}
