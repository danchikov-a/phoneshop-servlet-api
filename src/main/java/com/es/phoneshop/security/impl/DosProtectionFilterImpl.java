package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionFilter;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionFilterImpl implements DosProtectionFilter {
    private static final long THRESHOLD;
    private static final long MILLISECONDS_TO_UPDATE_REQUEST_COUNT;
    private static final long COUNT_WHEN_FIRST_REQUEST = 1L;
    private static final long COUNT_WHEN_UPDATE_REQUESTS = 0L;
    private static final String FILTER_PROPERTIES = "filter.properties";
    private static final String THRESHOLD_PROPERTY = "filter.threshold";
    private static final String MILLISECONDS_TO_UPDATE_REQUEST_COUNT_PROPERTY =
            "filter.milliseconds.to.update.request.count";
    private static final Logger LOGGER = Logger.getLogger(DosProtectionFilterImpl.class);
    private static final long DEFAULT_THRESHOLD = 20;
    private static final long DEFAULT_MILLISECONDS_TO_UPDATE_REQUEST_COUNT = 60_000;
    private static final String LOGGER_ERROR_MESSAGE = "Error occurred";

    private static DosProtectionFilter instance;

    private Map<String, Long> countMap = new ConcurrentHashMap();
    private Map<String, Long> timeMap = new ConcurrentHashMap();

    static {
        String rootPath = Thread.currentThread()
                .getContextClassLoader()
                .getResource(FILTER_PROPERTIES).getPath();

        Properties appProps = new Properties();

        try {
            appProps.load(new FileInputStream(rootPath));
        } catch (IOException ioException) {
            LOGGER.error(LOGGER_ERROR_MESSAGE, ioException);
        }

        String thresholdProperty = appProps.getProperty(THRESHOLD_PROPERTY);
        String msToUpdateRequestCountProperty = appProps.getProperty(MILLISECONDS_TO_UPDATE_REQUEST_COUNT_PROPERTY);

        THRESHOLD = thresholdProperty == null
                ? DEFAULT_THRESHOLD
                : Integer.parseInt(thresholdProperty);

        MILLISECONDS_TO_UPDATE_REQUEST_COUNT = msToUpdateRequestCountProperty == null
                ? DEFAULT_MILLISECONDS_TO_UPDATE_REQUEST_COUNT
                : Integer.parseInt(msToUpdateRequestCountProperty);
    }

    public static synchronized DosProtectionFilter getInstance() {
        if (instance == null) {
            instance = new DosProtectionFilterImpl();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip, long time) {
        Long count = countMap.get(ip);

        if (count == null) {
            count = COUNT_WHEN_FIRST_REQUEST;
            timeMap.put(ip, time);
        } else {

            if (time - timeMap.get(ip) > MILLISECONDS_TO_UPDATE_REQUEST_COUNT) {
                count = COUNT_WHEN_UPDATE_REQUESTS;
                timeMap.put(ip, time);
            }

            if (count > THRESHOLD) {
                return false;
            }

            count++;
        }

        countMap.put(ip, count);
        return true;
    }
}
