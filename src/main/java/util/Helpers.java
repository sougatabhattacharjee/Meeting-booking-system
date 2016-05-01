package util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    @Nonnull
    public static List<String> getFileAsList(@Nonnull final File file) throws IOException {
        checkNull(file, "file");

        final List<String> inputs = new ArrayList<String>();

        final BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            inputs.add(line);
        }

        return isListEmpty(inputs) ? Collections.<String>emptyList() : inputs;
    }

}
