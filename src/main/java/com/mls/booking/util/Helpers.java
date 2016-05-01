package com.mls.booking.util;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mls.booking.errors.ExceptionUtil.logAndThrow;
import static com.mls.booking.util.Validator.checkNull;

/**
 * Created by Sougata on 4/29/2016.
 */
public class Helpers {

    private final static Logger LOGGER = Logger.getLogger(Helpers.class);

    /**
     * Returns a string whose value is this string, with any leading and trailing
     * whitespace removed.
     *
     * @param input input string to be trimmed.
     * @return string without any leading and trailing whitespace
     */
    public static String trimWhiteSpaceOfString(@Nonnull final String input) {
        checkNull(input, "input");

        if (Boolean.FALSE.equals(isStringEmpty(input)))
            return input.trim();
        return input;
    }

    /**
     * Check if the string is empty or not. Returns true if the string is empty otherwise false.
     *
     * @param input input string to be checked.
     * @return true | false
     */
    public static boolean isStringEmpty(@Nullable final String input) {
        return (input == null || "".equals(input));
    }

    /**
     * Check if the list is empty or not. Returns true if the list is empty otherwise false.
     *
     * @param list list to be checked.
     * @return true | false
     */
    public static <T> boolean isListEmpty(@Nullable final List<T> list) {
        return (list == null || list.size() == 0);
    }

    /**
     * Convert a file into List of Strings
     *
     * @param file the input file to be converted.
     * @return the converted list
     * @throws IOException
     */
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

    /**
     * Concatenate two strings by a whitespace.
     *
     * @param dateTime1 1st string
     * @param dateTime2 2nd string
     * @return dateTime1 + " " + dateTime2
     */
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

    /**
     * Convert a string to LocalDateTime format.
     *
     * @param time           input string to be converted.
     * @param dateTimeFormat the specified format, an example is "yyyy-MM-dd HH:mm".
     * @return the converted datetime
     */
    @Nonnull
    public static LocalDateTime convertStringToLocalDateTimeFormat(@Nonnull final String time,
                                                                   @Nonnull final String dateTimeFormat) {
        checkNull(time, "time");
        checkNull(dateTimeFormat, "dateTimeFormat");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);

        return LocalDateTime.parse(time, dateTimeFormatter);
    }

    /**
     * Convert a string to LocalDate format.
     *
     * @param time           input string to be converted.
     * @param dateTimeFormat the specified format, an example is "yyyy-MM-dd HH:mm".
     * @return the converted date
     */
    @Nonnull
    public static LocalDate convertStringToLocalDate(@Nonnull final String time,
                                                     @Nonnull final String dateTimeFormat) {
        checkNull(time, "time");
        checkNull(dateTimeFormat, "dateTimeFormat");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat);

        return LocalDate.parse(time, dateTimeFormatter);
    }

}
