package util;

import org.apache.log4j.Logger;

import static errors.ExceptionUtil.logAndThrow;

/**
 * Created by Sougata on 4/30/2016.
 */
public class Validator {

    final static Logger LOGGER = Logger.getLogger(Validator.class);

    public static <T> T checkNull(T obj, T defaultValue) {
        if (null == obj) {
            logAndThrow(new NullPointerException(defaultValue + " cannot be null here."), LOGGER);
        }
        return obj;
    }
}
