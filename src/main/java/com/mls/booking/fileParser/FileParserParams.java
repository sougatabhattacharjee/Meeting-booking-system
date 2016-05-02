package com.mls.booking.fileParser;

/**
 * Created by Sougata on 4/30/2016.
 */
public class FileParserParams {

    /**
     * split a string by whitespace
     */
    public static final String SPLIT_PATTERN = "\\s+";

    /**
     * Represents the company office hours format in 24 hour clock format, an example is "0900 1730"
     */
    public static final String COMPANY_OFFICE_HOURS_FORMAT = "([01][0-9]|2[0-3])[0-5][0-9]\\s([01][0-9]|2[0-3])[0-5][0-9]";

    /** We have used both REGEX and SimpleDateFormat,
     * since REGEX does not validate formats like "2015-15-15" and
     * SimpleDateFormat does not validate format like ("2015-1-1")
     */
    /**
     * Date format in YYYY-MM-DD, an example is "2015-08-17"
     */
    public static final String DATE_FORMAT = "\\d{4}-[01]\\d-[0-3]\\d";

    /**
     * Simple Date formatting in yyyy-MM-dd, an example is "2015-08-17"
     */
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Simple Date formatting in yyyy-MM-dd HH:mm:ss, an example is "2015-08-17 10:17:06"
     */
    public static final String SIMPLE_DATE_FORMAT_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * Simple Date formatting in yyyy-MM-dd HH:mm, an example is "2015-08-21 09:00"
     */
    public static final String SIMPLE_DATE_FORMAT_HHMM = "yyyy-MM-dd HH:mm";

    /**
     * time in the format HH:MM:SS, an example is "10:17:06"
     */
    public static final String TIME_FORMAT_HHMMSS = "^([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$";

    /**
     * time in the format HH:MM, an example is "10:17"
     */
    public static final String TIME_FORMAT_HHMM = "^([0-1]\\d|2[0-3]):([0-5]\\d)";

    /**
     * employee id format, an example is "EMP001"
     */
    public static final String EMPLOYEE_ID_FORMAT = "EMP[0-9]{3}";

    /**
     * meeting duration in hours
     */
    public static final String MEETING_DURATION_FORMAT = "[1-9][0-9]{0,1}";

    /**
     * The first line of the input text represents the company office hours, in 24 hour clock format.
     * It has 2 subsections, an example is "0900 1730"
     */
    public static final int COMPANY_OFFICE_HOURS_FORMAT_NUMBER_OF_PARTS = 2;

    /**
     * Each booking request is in the following format.
     * [request submission time, in the format YYYY-MM-DD HH:MM:SS] [ARCH:employee id]
     * It has 3 subsections, an example is "2015-08-17 10:17:06 EMP001"
     */
    public static final int BOOKING_REQUEST_TIME_FORMAT_NUMBER_OF_PARTS = 3;

    /**
     * Each meeting meetingStartTime time is in the following format
     * [meeting meetingStartTime time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting duration in hours]
     * It has 3 subsections, an example is "2015-08-21 09:00 2"
     */
    public static final int MEETING_SCHEDULE_FORMAT_NUMBER_OF_PARTS = 3;

    /**
     * default file "input" can be retrieved from resource folder
     */
    public static final String DEFAULT_INPUT_FILE = "input.txt";

    /**
     * input file extension should be txt
     */
    public static final String INPUT_FILE_EXTENSION = "txt";
}
