package com.mls.booking.util;

import org.apache.log4j.Logger;

/**
 * Created by Sougata on 4/30/2016.
 */
public class Validator {

    private final static Logger LOGGER = Logger.getLogger(Validator.class);

    /**
     * Check an object is null or not. If null then throw a NullPointerException exception.
     *
     * @param obj          object to be checked.
     * @param defaultValue defaultvalue of object name
     */
    public static <T> T checkNull(T obj, T defaultValue) {
        if (null == obj) {
            LOGGER.error(defaultValue + " cannot be null here.");
            throw new NullPointerException(defaultValue + " cannot be null here.");
        }
        return obj;
    }
}
