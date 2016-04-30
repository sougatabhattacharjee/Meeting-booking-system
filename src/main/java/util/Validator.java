package util;

/**
 * Created by Sougata on 4/30/2016.
 */
public class Validator {

    public static <T> T checkNull(T obj, T defaultValue) {
        if (null == obj) {
            throw new NullPointerException(defaultValue + " cannot be null here.");
        }
        return obj;
    }


}
