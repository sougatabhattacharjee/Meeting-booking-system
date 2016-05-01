import errors.InvalidFormatException;
import fileParser.FileParserParams;
import fileParser.FileValidator;
import fileParser.FileValidatorImpl;
import meetingSchedule.MeetingScheduleInterval;
import meetingSchedule.MeetingScheduleIntervalModel;
import meetingSchedule.ScheduleIntervalNode;
import model.EmployeeMeetingSchedule;
import model.Meeting;
import model.OfficeHour;
import model.Result;
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

import static errors.ExceptionUtil.logAndThrow;
import static util.Helpers.*;
import static util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */
public class MeetingScheduler {

    private final static Logger LOGGER = Logger.getLogger(MeetingScheduler.class);

    public void process(@Nonnull final String fileName) throws IOException {
        checkNull(fileName, "fileName");
        LOGGER.info("Input File Validation started.");
        final Long startTime = DateTimeUtils.currentTimeMillis();

        final File inputFile = getFile(fileName);
        final FileValidator fileValidator = new FileValidatorImpl();

        try {
            fileValidator.validateFileFormat(inputFile);
        } catch (final InvalidFormatException e) {
            LOGGER.error(e.getMessage());
        }

        final List<String> inputFileContent = getFileAsList(inputFile);

        final List<EmployeeMeetingSchedule> inputFileContentModel = convertListToModel(inputFileContent);

        final Long endTime = DateTimeUtils.currentTimeMillis();
        final Long totalTime = endTime - startTime;

        LOGGER.info("Input File Validation Completed. It takes " + totalTime + "ms.");

        checkOverlapSchedule(inputFileContentModel);
    }

    public void checkOverlapSchedule(@Nonnull final List<EmployeeMeetingSchedule> employeeMeetingSchedules) {
        checkNull(employeeMeetingSchedules, "employeeMeetingSchedules");

        final MeetingScheduleInterval meetingScheduleIntervalModel = new MeetingScheduleIntervalModel();

        for (final EmployeeMeetingSchedule employeeMeetingSchedule : employeeMeetingSchedules) {
            meetingScheduleIntervalModel.add(employeeMeetingSchedule);
        }
        getActualMeetingSchedule(meetingScheduleIntervalModel.getRoot());
    }

    public void getActualMeetingSchedule(@Nullable final ScheduleIntervalNode rootNode) {
        final MeetingScheduleInterval meetingScheduleIntervalModel = new MeetingScheduleIntervalModel();
        LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules = meetingScheduleIntervalModel.traversal(rootNode);

        generateOutput(employeeMeetingSchedules);
    }

    public void generateOutput(@Nonnull final LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules) {
        Iterator itr = employeeMeetingSchedules.iterator();
        Map<String, List<Result>> outputStructure = new LinkedHashMap<>();
        while (itr.hasNext()) {
            EmployeeMeetingSchedule employeeMeetingSchedule = (EmployeeMeetingSchedule) itr.next();

            if (outputStructure.containsKey(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString())) {
                List<Result> results = outputStructure.get(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString());

                results.add(new Result(employeeMeetingSchedule.getEmployeeId(),
                        generateMeetingTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingStartTime().getHourOfDay(),
                                employeeMeetingSchedule.getMeeting().getMeetingStartTime().getMinuteOfHour()),
                        generateMeetingTimeFormat(employeeMeetingSchedule.getMeeting().getMeetingEndTime().getHourOfDay(),
                                employeeMeetingSchedule.getMeeting().getMeetingEndTime().getMinuteOfHour())));

                outputStructure.put(employeeMeetingSchedule.getMeeting().getMeetingStartDate().toString(), results);

            } else {
                List<Result> results = new LinkedList<>();

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

    public void generateOutput(Map<String, List<Result>> outputStructure) {
        Iterator entries = outputStructure.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            String key = (String) thisEntry.getKey();
            System.out.println(key);

            List<Result> values = (List<Result>) thisEntry.getValue();
            for(Result result : values) {
                System.out.println(result.toString());
            }
        }
    }

    public String generateMeetingTimeFormat(int hour, int minute) {
        String formatHour = (hour < 10) ? "0" + hour : "" + hour;
        String formatMinute = (minute < 10) ? "0" + minute : "" + minute;

        return formatHour + ":" + formatMinute;
    }


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

        return (officeStartTime.isEqual(meetingStartTime) || officeStartTime.isBefore(meetingStartTime))
                && (officeEndTime.isAfter(meetingStartTime))
                && (officeEndTime.isEqual(meetingEndTime) || officeEndTime.isAfter(meetingEndTime))
                && (officeStartTime.isBefore(meetingEndTime));
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
     * build the OfficeHour model from the given input.
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
     * build the Meeting model from the given input.
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
     * build the EmployeeMeetingSchedule model from the given input.
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
        } else
            logAndThrow(new NullPointerException("getTimeFormat() is null"), LOGGER);

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
        if (classLoader.getResource(fileName) != null)
            file = new File(classLoader.getResource(fileName).getFile());
        else
            logAndThrow(new FileNotFoundException(fileName + " file not found."), LOGGER);

        return file;
    }
}
