package com.mls.booking.fileParser;

import com.mls.booking.errors.InvalidDateFormatException;
import com.mls.booking.errors.InvalidFormatException;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mls.booking.util.Helpers.trimWhiteSpaceOfString;
import static com.mls.booking.util.Validator.checkNull;

/**
 * Created by Sougata on 4/30/2016.
 */
public class FileParserImpl implements FileParser {

    private final static Logger LOGGER = Logger.getLogger(FileParserImpl.class);

    @Nonnull
    public boolean validateCompanyOfficeHoursFormat(@Nonnull final String time) throws InvalidFormatException {
        checkNull(time, "time");

        if (Boolean.FALSE.equals(validateStringFormatParts(time, FileParserParams.COMPANY_OFFICE_HOURS_FORMAT_NUMBER_OF_PARTS))) {

            throw new InvalidFormatException("Invalid formatting. Line should contain "
                    + FileParserParams.COMPANY_OFFICE_HOURS_FORMAT_NUMBER_OF_PARTS + " parts");
        }

        if (Boolean.FALSE.equals(validateStringPattern(FileParserParams.COMPANY_OFFICE_HOURS_FORMAT, time))) {

            throw new InvalidFormatException("Invalid time formatiing for company office hours. It should be in 24 hour " +
                    "clock format");
        }

        return true;
    }

    @Nonnull
    public boolean validateBookingRequestFormat(@Nonnull final String bookingRequestFormat) throws InvalidFormatException {
        checkNull(bookingRequestFormat, "bookingRequestFormat");

        if (Boolean.FALSE.equals(validateStringFormatParts(bookingRequestFormat,
                FileParserParams.BOOKING_REQUEST_TIME_FORMAT_NUMBER_OF_PARTS))) {
            throw new InvalidFormatException("Invalid formatting. " +
                    "request submission time, in the format [YYYY-MM-DD HH:MM:SS employee id]" +
                    " should contain " + FileParserParams.BOOKING_REQUEST_TIME_FORMAT_NUMBER_OF_PARTS + " parts");
        }

        final String[] tokens = bookingRequestFormat.split(FileParserParams.SPLIT_PATTERN);
        assert tokens.length == FileParserParams.BOOKING_REQUEST_TIME_FORMAT_NUMBER_OF_PARTS;
        final String dateFormat_YYYYMMDD = trimWhiteSpaceOfString(tokens[0]);
        final String timeFormat_HHMMSS = trimWhiteSpaceOfString(tokens[1]);
        final String employeeId = trimWhiteSpaceOfString(tokens[2]);

        if (Boolean.FALSE.equals(validateDateFormat(dateFormat_YYYYMMDD))) {
            throw new InvalidDateFormatException("Invalid date formatting for "
                    + dateFormat_YYYYMMDD + " request submission time, in the format YYYY-MM-DD HH:MM:SS");
        }

        if (Boolean.FALSE.equals(validateTimeFormatHHMMSS(timeFormat_HHMMSS))) {
            throw new InvalidDateFormatException("Invalid date formatting for "
                    + timeFormat_HHMMSS + " request submission time, in the format YYYY-MM-DD HH:MM:SS");

        }

        if (Boolean.FALSE.equals(validateEmployeeId(employeeId))) {
            throw new InvalidFormatException("Invalid employee Id " + employeeId);
        }

        return true;
    }

    @Nonnull
    public boolean validateMeetingScheduleFormat(@Nonnull final String meetingScheduleFormat) throws InvalidFormatException {
        checkNull(meetingScheduleFormat, "meetingScheduleFormat");

        if (Boolean.FALSE.equals(validateStringFormatParts(meetingScheduleFormat,
                FileParserParams.MEETING_SCHEDULE_FORMAT_NUMBER_OF_PARTS))) {

            throw new InvalidFormatException("Invalid formatting. " +
                    "meeting meetingStartTime time, in the format [YYYY-MM-DD HH:MM Duration(hours)]" +
                    " should contain " + FileParserParams.MEETING_SCHEDULE_FORMAT_NUMBER_OF_PARTS + " parts");
        }

        final String[] tokens = meetingScheduleFormat.split(FileParserParams.SPLIT_PATTERN);
        assert tokens.length == FileParserParams.MEETING_SCHEDULE_FORMAT_NUMBER_OF_PARTS;
        final String dateFormat_YYYYMMDD = trimWhiteSpaceOfString(tokens[0]);
        final String timeFormat_HHMM = trimWhiteSpaceOfString(tokens[1]);
        final String duration = trimWhiteSpaceOfString(tokens[2]);

        if (Boolean.FALSE.equals(validateDateFormat(dateFormat_YYYYMMDD))) {
            throw new InvalidDateFormatException("Invalid date formatting for "
                    + dateFormat_YYYYMMDD + " request submission time, in the format YYYY-MM-DD HH:MM");
        }

        if (Boolean.FALSE.equals(validateTimeFormatHHMM(timeFormat_HHMM))) {
            throw new InvalidDateFormatException("Invalid date formatting for "
                    + timeFormat_HHMM + " request submission time, in the format YYYY-MM-DD HH:MM");
        }

        if (Boolean.FALSE.equals(validateMeetingDuration(duration))) {
            throw new InvalidFormatException("Invalid meeting duration " + duration);
        }

        return true;
    }

    /**
     * Helper methods
     */

    /**
     * Validate a given string format according to the defined formatting pattern.
     * Return true if it validates the format otherwise false
     *
     * @param stringFormat defined formatting pattern
     * @param inputString  input string to be validated
     * @return true | false
     */
    @Nonnull
    private boolean validateStringPattern(@Nonnull final String stringFormat, @Nonnull final String inputString) {
        checkNull(stringFormat, "stringFormat");
        checkNull(inputString, "inputString");

        final Pattern pattern = Pattern.compile(stringFormat);
        final Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    /**
     * Validate time format in HH:MM:SS pattern. Return true if it validates the format otherwise false.
     *
     * @param time input time to be validated
     * @return true | false
     */
    @Nonnull
    private boolean validateTimeFormatHHMMSS(@Nonnull final String time) {
        checkNull(time, "time");

        return validateStringPattern(FileParserParams.TIME_FORMAT_HHMMSS, time);
    }

    /**
     * Validate time format in HH:MM pattern. Return true if it validates the format otherwise false.
     *
     * @param time input time to be validated
     * @return true | false
     */
    @Nonnull
    private boolean validateTimeFormatHHMM(@Nonnull final String time) {
        checkNull(time, "time");

        return validateStringPattern(FileParserParams.TIME_FORMAT_HHMM, time);
    }

    /**
     * Validate the format of an employee id. An example of employee id is EMP001.
     * Return true if it validates the format otherwise false.
     *
     * @param employeeId employee id to be validated
     * @return true | false
     */
    @Nonnull
    private boolean validateEmployeeId(@Nonnull final String employeeId) {
        checkNull(employeeId, "employeeId");

        return validateStringPattern(FileParserParams.EMPLOYEE_ID_FORMAT, employeeId);
    }

    /**
     * Validate meeting duration format. Meeting duration should be given in hours.
     * It also checks if the duration is greater than 0.
     * Return true if it validates the format otherwise false.
     *
     * @param meetingDuration meeting duration to be validated
     * @return true | false
     * @throws InvalidFormatException meeting duration should be greater than 0
     */
    @Nonnull
    private boolean validateMeetingDuration(@Nonnull final String meetingDuration) throws InvalidFormatException {
        checkNull(meetingDuration, "meetingDuration");

        if (Boolean.FALSE.equals(validateStringPattern(FileParserParams.MEETING_DURATION_FORMAT, meetingDuration))) {
            if (meetingDuration.matches("[0-9]+")) {
                throw new InvalidFormatException("Invalid meeting duration, should be greater than 0");
            }
            return false;
        }
        return true;
    }

    /**
     * Validate the date format in YYYY-MM-DD format. Return true if it validates the format otherwise false.
     *
     * @param dateFormat date format to be validated
     * @return true | false
     */
    @Nonnull
    private boolean validateDateFormat(@Nonnull final String dateFormat) throws InvalidFormatException {
        checkNull(dateFormat, "dateFormat");

        if (Boolean.FALSE.equals(dateFormat.matches(FileParserParams.DATE_FORMAT)))
            return false;
        try {
            dateFormatThreadLocal.get().parse(dateFormat);
            return true;
        } catch (final ParseException ex) {
            LOGGER.error(ex.getMessage());
        }

        return false;
    }

    /**
     * Since the SimpleDateFormat is not thread safe so ThreadLocal is used to prevent
     * the creation of a new SimpleDateFormat for each instance.
     */
    private final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FileParserParams.SIMPLE_DATE_FORMAT);
            simpleDateFormat.setLenient(false);
            return simpleDateFormat;
        }
    };

    /**
     * Validate each line format. Checks if the line has correct number of subsection.
     * Return true if it validates the format otherwise false.
     *
     * @param stringFormat          input string to be validated
     * @param expectedNumberOfParts expected number of subsection in the string
     * @return true | false
     */
    @Nonnull
    private boolean validateStringFormatParts(@Nonnull final String stringFormat,
                                              @Nonnull final int expectedNumberOfParts) {
        checkNull(stringFormat, "stringFormat");
        checkNull(expectedNumberOfParts, "expectedNumberOfParts");

        final StringTokenizer stringTokenizer = new StringTokenizer(stringFormat);
        final int actualNumberOfParts = stringTokenizer.countTokens();

        return actualNumberOfParts == expectedNumberOfParts;
    }

}
