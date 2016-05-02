package com.mls.booking.util;

import com.mls.booking.errors.InvalidFileFormatException;
import com.mls.booking.fileParser.FileParserParams;
import com.mls.booking.model.EmployeeMeetingSchedule;
import com.mls.booking.model.Meeting;
import com.mls.booking.model.OfficeHour;
import org.apache.commons.io.FilenameUtils;
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

import static com.mls.booking.fileParser.FileParserParams.INPUT_FILE_EXTENSION;
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
        while (Boolean.FALSE.equals(isStringEmpty(line = reader.readLine()))) {
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
            throw new NullPointerException();
    }


    /** LocalDateTime utis */
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

    public static boolean isGreaterThanOrEqual(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");
        return startTime.isAfter(endTime) || startTime.isEqual(endTime);
    }

    public static boolean isLessThanOrEqual(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isBefore(endTime) || startTime.isEqual(endTime);
    }

    public static boolean isEqual(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isEqual(endTime);
    }

    public static boolean isLessThan(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isBefore(endTime);
    }

    public static boolean isGreaterThan(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isAfter(endTime);
    }

    public static boolean ifFileIsText(@Nonnull final String fileName) throws InvalidFileFormatException {
        checkNull(fileName, "fileName");

        if (FilenameUtils.isExtension(fileName.toLowerCase(), INPUT_FILE_EXTENSION.toLowerCase()))
            return true;
        else
            throw new InvalidFileFormatException("Input file should be a text file");
    }


    /** Meeting Schedule Interval Tree utils*/

    /**
     * Convert the list of input contents into our internal model (EmployeeMeetingSchedule) of list.
     * Then sort the list by submission request time.
     *
     * @param inputFileContent input list of contents
     * @return ordered list<EmployeeMeetingSchedule>
     */
    @Nonnull
    public static List<EmployeeMeetingSchedule> convertListToModel(@Nonnull final List<String> inputFileContent) {
        checkNull(inputFileContent, "inputFileContent");

        final List<EmployeeMeetingSchedule> employeeMeetingSchedules = new ArrayList<EmployeeMeetingSchedule>();

        EmployeeMeetingSchedule employeeMeetingSchedule;

        // convert the 1st line into our defined OfficeHour Model
        final OfficeHour officeHour = getOfficeHour(inputFileContent.get(0));

        // meetingStartTime from the 2nd line
        for (int index = 1; index < inputFileContent.size(); ) {
            employeeMeetingSchedule = new EmployeeMeetingSchedule();
            employeeMeetingSchedule.setOfficeHour(officeHour);
            int count = 0;
            while (count++ < 2) {

                // validate every 2nd line which contains individual booking requests
                if (index % 2 != 0) {
                    getEmployeeMeetingSchedule(inputFileContent.get(index), employeeMeetingSchedule);
                }

                // validate every 3rd line which contains meeting meetingStartTime time
                if (index % 2 == 0) {
                    final Meeting meeting = getMeeting(inputFileContent.get(index));
                    employeeMeetingSchedule.setMeeting(meeting);
                }
                index++;
            }

            if (validateMeetingTimeSchedule(employeeMeetingSchedule))
                employeeMeetingSchedules.add(employeeMeetingSchedule);
        }

        getSortedMeetingSchedulesBySubmissionTime(employeeMeetingSchedules);

        return employeeMeetingSchedules;
    }

    /**
     * sort the input list by the submission time.
     *
     * @param employeeMeetingSchedules list to be sorted
     * @return sorted list
     */
    @Nonnull
    public static List<EmployeeMeetingSchedule> getSortedMeetingSchedulesBySubmissionTime(
            @Nonnull final List<EmployeeMeetingSchedule> employeeMeetingSchedules) {
        checkNull(employeeMeetingSchedules, "employeeMeetingSchedules");

        Collections.sort(employeeMeetingSchedules, (submissionTime1, submissionTime2)
                -> submissionTime1.getSubmissionTime().compareTo(submissionTime2.getSubmissionTime()));
        return employeeMeetingSchedules;
    }

    /**
     * build the OfficeHour com.mls.booking.model from the given input.
     *
     * @param input input string to be converted to the OfficeHour object
     * @return OfficeHour
     */
    @Nonnull
    public static OfficeHour getOfficeHour(@Nonnull final String input) {
        checkNull(input, "input");

        final String[] officeHourString = input.split(FileParserParams.SPLIT_PATTERN);
        final String officeStartTime = trimWhiteSpaceOfString(officeHourString[0]);
        final String officeEndTime = trimWhiteSpaceOfString(officeHourString[1]);

        return new OfficeHour(officeStartTime, officeEndTime);
    }

    /**
     * build the Meeting com.mls.booking.model from the given input.
     *
     * @param input input string to be converted to the Meeting object
     * @return Meeting
     */
    @Nonnull
    public static Meeting getMeeting(@Nonnull final String input) {
        checkNull(input, "input");

        final String[] meetingString = input.split(FileParserParams.SPLIT_PATTERN);
        final String meetingStartDate = trimWhiteSpaceOfString(meetingString[0]);
        final String meetingStartTime = trimWhiteSpaceOfString(meetingString[1]);
        final String meetingDuration = trimWhiteSpaceOfString(meetingString[2]);

        return new Meeting(meetingStartDate, getTimeFormat(meetingStartDate, meetingStartTime), meetingDuration);
    }

    /**
     * build the EmployeeMeetingSchedule com.mls.booking.model from the given input.
     *
     * @param input input string to be converted to the EmployeeMeetingSchedule object
     */
    public static void getEmployeeMeetingSchedule(@Nonnull final String input,
                                                  @Nonnull final EmployeeMeetingSchedule employeeMeetingSchedule) {
        checkNull(input, "input");
        checkNull(employeeMeetingSchedule, "employeeMeetingSchedule");

        final String[] meetingString = input.split(FileParserParams.SPLIT_PATTERN);

        final String submissionDate = trimWhiteSpaceOfString(meetingString[0]);
        final String submissionTime = trimWhiteSpaceOfString(meetingString[1]);
        final String employeeId = trimWhiteSpaceOfString(meetingString[2]);

        if (getTimeFormat(submissionDate, submissionTime) != null) {
            employeeMeetingSchedule.setSubmissionTime(getTimeFormat(submissionDate, submissionTime));
        } else {
            LOGGER.fatal("getTimeFormat() is null");
            throw new NullPointerException("getTimeFormat() is null");
        }

        employeeMeetingSchedule.setEmployeeId(employeeId);
    }

    /**
     * Validate the meeting meetingStartTime and meetingEndTime time.
     * Return false if meeting meetingStartTime time is before the office meetingStartTime time
     * and meeting meetingEndTime time is before the office meetingEndTime time.
     *
     * @param employeeMeetingSchedule meeting schedule timings including meetingStartTime and meetingEndTime time
     * @return true | false
     */
    public static boolean validateMeetingTimeSchedule(@Nonnull final EmployeeMeetingSchedule employeeMeetingSchedule) {
        checkNull(employeeMeetingSchedule, "employeeMeetingSchedule");

        final LocalDateTime meetingStartTime = employeeMeetingSchedule.getMeeting().getMeetingStartTime();
        final LocalDateTime meetingEndTime = employeeMeetingSchedule.getMeeting().getMeetingEndTime();

        final String officeStart = getTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString(),
                employeeMeetingSchedule.getOfficeHour().getOfficeStartTime());
        final String officeEnd = getTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString(),
                employeeMeetingSchedule.getOfficeHour().getOfficeEndTime());

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HHmm");
        final LocalDateTime officeStartTime = LocalDateTime.parse(officeStart, dateTimeFormatter);
        final LocalDateTime officeEndTime = LocalDateTime.parse(officeEnd, dateTimeFormatter);

        return isLessThanOrEqual(officeStartTime, meetingStartTime)
                && isGreaterThan(officeEndTime, meetingStartTime)
                && isGreaterThanOrEqual(officeEndTime, meetingEndTime)
                && isLessThan(officeStartTime, meetingEndTime);
    }
}
