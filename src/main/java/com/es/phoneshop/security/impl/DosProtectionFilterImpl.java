package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionFilterImpl implements DosProtectionFilter {
    private static final long LIMIT = 20;
    private static final long ASSIGN_COUNT_WHEN_NULL = 1L;
    private static final long ONE_MINUTE = 60_000;
    private static final long ASSIGN_COUNT_WHEN_NOT_NULL = 0L;

    private static DosProtectionFilter instance;
    private Map<String, Long> countMap = new ConcurrentHashMap();
    private Map<String, Long> timeMap = new ConcurrentHashMap();

    public static synchronized DosProtectionFilter getInstance() {
        if(instance == null){
            instance = new DosProtectionFilterImpl();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip, long time) {
        Long count = countMap.get(ip);

        if(count == null){
            count = ASSIGN_COUNT_WHEN_NULL;
            timeMap.put(ip, time);
        }else{

            if(time - timeMap.get(ip) > ONE_MINUTE){
                count = ASSIGN_COUNT_WHEN_NOT_NULL;
                timeMap.put(ip, time);
            }

            if(count > LIMIT) {
                return false;
            }

            count++;
        }

        countMap.put(ip, count);
        return true;
    }
}
