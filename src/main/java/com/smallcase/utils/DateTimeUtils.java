package com.smallcase.utils;

import org.springframework.stereotype.Service;

@Service
public class DateTimeUtils {
    public Long getCurrentTimeInSeconds() {
        return System.currentTimeMillis() / 1000L;
    }

    public Integer getIntCurrentTimeInSeconds() {
        return Math.toIntExact(getCurrentTimeInSeconds());
    }
}
