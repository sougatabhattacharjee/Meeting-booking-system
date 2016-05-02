package com.mls.booking;

import com.mls.booking.errors.InvalidFileFormatException;
import com.mls.booking.errors.InvalidFormatException;
import com.mls.booking.fileParser.FileParserParams;
import com.mls.booking.fileParser.FileValidator;
import com.mls.booking.fileParser.FileValidatorImpl;
import com.mls.booking.meetingSchedule.MeetingScheduleInterval;
import com.mls.booking.meetingSchedule.MeetingScheduleIntervalModel;
import com.mls.booking.meetingSchedule.ScheduleIntervalNode;
import com.mls.booking.model.EmployeeMeetingSchedule;
import com.mls.booking.model.Meeting;
import com.mls.booking.model.OfficeHour;
import com.mls.booking.model.Result;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.mls.booking.util.Helpers.*;
import static com.mls.booking.util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */
public class MeetingScheduler {

    private final static Logger LOGGER = Logger.getLogger(MeetingScheduler.class);

    public void process(@Nonnull final String fileName) throws IOException, InvalidFileFormatException {
        checkNull(fileName, "fileName");

        LOGGER.info("Input File Validation started.");
        final Long startTime = DateTimeUtils.currentTimeMillis();

        if (!ifFileIsText(fileName))
            throw new InvalidFileFormatException("Wrong File Format");

        // retrieve the file by its filename and validates the file
        final File inputFile = getFile(fileName);
        final FileValidator fileValidator = new FileValidatorImpl();
        try {
            fileValidator.validateFileFormat(inputFile);
        } catch (final InvalidFormatException | FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }

        final List<String> inputFileContent = getFileAsList(inputFile);
        final List<EmployeeMeetingSchedule> inputFileContentModel = convertListToModel(inputFileContent);

        final Long endTime = DateTimeUtils.currentTimeMillis();
        final Long totalTime = endTime - startTime;
        LOGGER.info("Input File Validation Completed. It takes " + totalTime + "ms.");

        checkOverlapSchedule(inputFileContentModel);
    }

    /**
     * Check if any of the meeting time request overlap with other.
     * To check this, we have to add every record to an interval tree data structure
     *
     * @param employeeMeetingSchedules ordered list to be checked for overlapped meeting time
     */
    public void checkOverlapSchedule(@Nonnull final List<EmployeeMeetingSchedule> employeeMeetingSchedules) {
        checkNull(employeeMeetingSchedules, "employeeMeetingSchedules");

        final MeetingScheduleInterval meetingScheduleIntervalModel = new MeetingScheduleIntervalModel();

        for (final EmployeeMeetingSchedule employeeMeetingSchedule : employeeMeetingSchedules) {
            meetingScheduleIntervalModel.add(employeeMeetingSchedule);
        }
        getActualMeetingSchedule(meetingScheduleIntervalModel.getRoot());
    }

    /**
     * Traverse the interval tree and get the filtered data
     *
     * @param rootNode root node of the tree
     */
    public void getActualMeetingSchedule(@Nullable final ScheduleIntervalNode rootNode) {

        final MeetingScheduleInterval meetingScheduleIntervalModel = new MeetingScheduleIntervalModel();
        final LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules =
                meetingScheduleIntervalModel.traversal(rootNode);

        generateOutput(employeeMeetingSchedules);
    }

    /**
     * convert the data retrieved from the interval tree to defined output model
     *
     * @param employeeMeetingSchedules filtered list from which output data structure to be generated
     */
    public void generateOutput(@Nonnull final LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules) {
        checkNull(employeeMeetingSchedules, "employeeMeetingSchedules");

        final Iterator itr = employeeMeetingSchedules.iterator();
        final Map<String, List<Result>> outputStructure = new LinkedHashMap<>();

        while (itr.hasNext()) {
            final EmployeeMeetingSchedule employeeMeetingSchedule = (EmployeeMeetingSchedule) itr.next();

            if (outputStructure.containsKey(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString())) {
                final List<Result> results = outputStructure.get(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString());

                results.add(new Result(employeeMeetingSchedule.getEmployeeId(),
                        generateMeetingTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingStartTime().getHourOfDay(),
                                employeeMeetingSchedule.getMeeting().getMeetingStartTime().getMinuteOfHour()),
                        generateMeetingTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingEndTime().getHourOfDay(),
                                employeeMeetingSchedule.getMeeting().getMeetingEndTime().getMinuteOfHour())));

                outputStructure.put(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString(), results);

            } else {
                final List<Result> results = new LinkedList<>();

                results.add(new Result(employeeMeetingSchedule.getEmployeeId(),
                        generateMeetingTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingStartTime().getHourOfDay(),
                                employeeMeetingSchedule.getMeeting().getMeetingStartTime().getMinuteOfHour()),
                        generateMeetingTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingEndTime().getHourOfDay(),
                                employeeMeetingSchedule.getMeeting().getMeetingEndTime().getMinuteOfHour())));

                outputStructure.put(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString(), results);
            }
        }

        generateOutput(outputStructure);
    }

    /**
     * Generate the final output to the console.
     */
    public void generateOutput(@Nonnull final Map<String, List<Result>> outputStructure) {
        checkNull(outputStructure, "outputStructure");

        final Iterator entries = outputStructure.entrySet().iterator();
        System.out.println();
        System.out.println("********************* Meeting Schedule ****************************");
        System.out.println();
        while (entries.hasNext()) {
            final Map.Entry thisEntry = (Map.Entry) entries.next();
            String key = (String) thisEntry.getKey();
            System.out.println(key);

            final List<Result> values = (List<Result>) thisEntry.getValue();
            for (final Result result : values) {
                System.out.println(result.toString());
            }
        }
        System.out.println();
        System.out.println("*******************************************************************");
    }

    /**
     * Generate the time format as HH:mm.
     *
     * @param hour   input hour
     * @param minute input minute
     * @return hour:minute
     */
    @Nonnull
    public String generateMeetingTimeFormat(@Nonnull final int hour, @Nonnull final int minute) {
        checkNull(hour, "hour");
        checkNull(minute, "minute");

        final String formatHour = (hour < 10) ? "0" + hour : "" + hour;
        final String formatMinute = (minute < 10) ? "0" + minute : "" + minute;

        return formatHour + ":" + formatMinute;
    }

    /**
     * Convert the list of input contents into our internal model (EmployeeMeetingSchedule) of list.
     * Then sort the list by submission request time.
     *
     * @param inputFileContent input list of contents
     * @return ordered list<EmployeeMeetingSchedule>
     */
    @Nonnull
    private List<EmployeeMeetingSchedule> convertListToModel(@Nonnull final List<String> inputFileContent) {
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
     * Validate the meeting meetingStartTime and meetingEndTime time.
     * Return false if meeting meetingStartTime time is before the office meetingStartTime time
     * and meeting meetingEndTime time is before the office meetingEndTime time.
     *
     * @param employeeMeetingSchedule meeting schedule timings including meetingStartTime and meetingEndTime time
     * @return true | false
     */
    private boolean validateMeetingTimeSchedule(@Nonnull final EmployeeMeetingSchedule employeeMeetingSchedule) {
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

    /**
     * sort the input list by the submission time.
     *
     * @param employeeMeetingSchedules list to be sorted
     * @return sorted list
     */
    @Nonnull
    private List<EmployeeMeetingSchedule> getSortedMeetingSchedulesBySubmissionTime(
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
    private OfficeHour getOfficeHour(@Nonnull final String input) {
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
    private Meeting getMeeting(@Nonnull final String input) {
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
    private void getEmployeeMeetingSchedule(@Nonnull final String input,
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
     * Create a File object from a given file name
     *
     * @param fileName the given filename to be converted
     * @return the File obejct
     * @throws IOException
     */
    @Nonnull
    private File getFile(@Nonnull final String fileName) throws IOException {
        checkNull(fileName, "fileName");

        //Get file from resources folder
        final ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        // read file from resource folder
        if (classLoader.getResource(fileName) != null)
            file = new File(classLoader.getResource(fileName).getFile());
            // read file from any source
        else if (classLoader.getResource(fileName) == null)
            file = new File(fileName);
        else {
            LOGGER.fatal(fileName + " file not found.");
            throw new FileNotFoundException(fileName + " file not found.");
        }

        return file;
    }
}
