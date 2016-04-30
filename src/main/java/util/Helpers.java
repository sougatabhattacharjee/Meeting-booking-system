package util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import static util.Validator.checkNull;

/**
 * Created by Sougata on 4/29/2016.
 */
public class Helpers {

    public static String trimWhiteSpaceOfString(@Nonnull final String input) {
        checkNull(input, "input");

        if(Boolean.FALSE.equals(isStringEmpty(input)))
            return input.trim();
        return input;
    }

    public static boolean isStringEmpty(@Nullable final String input) {
        return (input == null || "".equals(input));
    }

    public static <T> boolean isListEmpty(@Nullable final List<T> list) {
        return (list == null || list.size()==0);
    }

}
