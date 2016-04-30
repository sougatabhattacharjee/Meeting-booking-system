package fileParser;

/**
 * Created by Sougata on 4/30/2016.
 */
public class FileParserParams {

    public static final String SPLIT_PATTERN = "\\s+";

    public static final String COMPANY_OFFICE_HOURS_FORMAT = "([01][0-9]|2[0-3])[0-5][0-9]\\s([01][0-9]|2[0-3])[0-5][0-9]";

    public static final String DATE_FORMAT = "\\d{4}-[01]\\d-[0-3]\\d";

    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT_HHMMSS = "^([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$";

    public static final String TIME_FORMAT_HHMM = "^([0-1]\\d|2[0-3]):([0-5]\\d)";

    public static final String EMPLOYEE_ID_FORMAT = "EMP[0-9]{3}";

    public static final String MEETING_DURATION_FORMAT = "[1-9][0-9]{0,1}";

    public static final int COMPANY_OFFICE_HOURS_FORMAT_NUMBER_OF_PARTS = 2;

    public static final int BOOKING_REQUEST_TIME_FORMAT_NUMBER_OF_PARTS = 3;

    public static final int MEETING_SCHEDULE_FORMAT_NUMBER_OF_PARTS = 3;

    public static final String bookingRequestTimeFormat = "";

    public static final String meetingStartTimeFormat = "";
}
