package util;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static errors.ExceptionUtil.logAndThrow;
import static util.Validator.checkNull;

/**
 * Created by Sougata on 4/29/2016.
 */
public class Helpers {

    final static Logger LOGGER = Logger.getLogger(Helpers.class);

    public static String trimWhiteSpaceOfString(@Nonnull final String input) {
        checkNull(input, "input");

        if (Boolean.FALSE.equals(isStringEmpty(input)))
            return input.trim();
        return input;
    }

    public static boolean isStringEmpty(@Nullable final String input) {
        return (input == null || "".equals(input));
    }

    public static <T> boolean isListEmpty(@Nullable final List<T> list) {
        return (list == null || list.size() == 0);
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

    @Nullable
    public static String getTimeFormat(@Nonnull final String dateTime1, @Nonnull final String dateTime2) {
        checkNull(dateTime1, "dateTime1");
        checkNull(dateTime2, "dateTime2");

        if (Boolean.FALSE.equals(isStringEmpty(dateTime1)) && Boolean.FALSE.equals(isStringEmpty(dateTime2)))
            return trimWhiteSpaceOfString(dateTime1) + " " + trimWhiteSpaceOfString(dateTime2);
        else
            logAndThrow(new NullPointerException(), LOGGER);
        return null;
    }

    @Nonnull
    public static LocalDateTime convertStringToLocalDateTimeFormat(@Nonnull final String time,
                                                                   @Nonnull final String dateTimeFormat) {
        checkNull(time, "time");
        checkNull(dateTimeFormat, "dateTimeFormat");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);
        final LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);

        return localDateTime;
    }

    @Nonnull
    public static LocalDate convertStringToLocalDate(@Nonnull final String time,
                                               @Nonnull final String dateTimeFormat) {
        checkNull(time, "time");
        checkNull(dateTimeFormat, "dateTimeFormat");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);
        final LocalDate localDate = LocalDate.parse(time, dateTimeFormatter);

        return localDate;
    }

}
