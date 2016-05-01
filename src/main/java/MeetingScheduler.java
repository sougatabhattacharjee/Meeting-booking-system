import errors.InvalidFormatException;
import fileParser.FileParserParams;
import fileParser.FileValidator;
import fileParser.FileValidatorImpl;
import model.EmployeeMeetingSchedule;
import model.Meeting;
import model.OfficeHour;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static errors.ExceptionUtil.logAndThrow;
import static util.Helpers.getFileAsList;
import static util.Helpers.getTimeFormat;
import static util.Helpers.trimWhiteSpaceOfString;
import static util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */
public class MeetingScheduler {

    final static Logger LOGGER = Logger.getLogger(MeetingScheduler.class);

    public void process(@Nonnull final String fileName) throws IOException {
        checkNull(fileName, "fileName");

        final File inputFile = getFile(fileName);
        final FileValidator fileValidator = new FileValidatorImpl();

        try {
            fileValidator.validateFileFormat(inputFile);
        } catch (final InvalidFormatException e) {
            LOGGER.error(e.getMessage());
        }

        final List<String> inputFileContent = getFileAsList(inputFile);

        final List<EmployeeMeetingSchedule> inputFileContentModel = convertListToModel(inputFileContent);

        System.out.println("inputFileContentModel.size() = " + inputFileContentModel.size());
        System.out.println("inputFileContentModel.size() = " + inputFileContentModel.get(0).getEmployeeId());
    }

    @Nonnull
    private List<EmployeeMeetingSchedule> convertListToModel(@Nonnull final List<String> inputFileContent) {
        checkNull(inputFileContent, "inputFileContent");

        final List<EmployeeMeetingSchedule> employeeMeetingSchedules = new ArrayList<EmployeeMeetingSchedule>();

        EmployeeMeetingSchedule employeeMeetingSchedule;

        // convert the 1st line into our defined OfficeHour Model
        final OfficeHour officeHour = getOfficeHour(inputFileContent.get(0));

        // start from the 2nd line
        for (int index = 1; index < inputFileContent.size(); ) {
            employeeMeetingSchedule = new EmployeeMeetingSchedule();
            employeeMeetingSchedule.setOfficeHour(officeHour);
            int count = 0;
            while (count++ < 2) {

                // validate every 2nd line which contains individual booking requests
                if (index % 2 != 0) {
                    getEmployeeMeetingSchedule(inputFileContent.get(index), employeeMeetingSchedule);
                }

                // validate every 3rd line which contains meeting start time
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
                && (officeEndTime.isEqual(meetingEndTime) || officeEndTime.isAfter(meetingEndTime));
    }

    @Nonnull
    private List<EmployeeMeetingSchedule> getSortedMeetingSchedulesBySubmissionTime(
            @Nonnull final List<EmployeeMeetingSchedule> employeeMeetingSchedules) {
        checkNull(employeeMeetingSchedules, "employeeMeetingSchedules");

        Collections.sort(employeeMeetingSchedules, (submissionTime1, submissionTime2)
                -> submissionTime1.getSubmissionTime().compareTo(submissionTime2.getSubmissionTime()));
        return employeeMeetingSchedules;
    }

    @Nonnull
    private OfficeHour getOfficeHour(@Nonnull final String input) {
        checkNull(input, "input");

        final String[] officeHourString = input.split(FileParserParams.SPLIT_PATTERN);
        final String officeStartTime = trimWhiteSpaceOfString(officeHourString[0]);
        final String officeEndTime = trimWhiteSpaceOfString(officeHourString[1]);

        return new OfficeHour(officeStartTime, officeEndTime);
    }

    @Nonnull
    private Meeting getMeeting(@Nonnull final String input) {
        checkNull(input, "input");

        final String[] meetingString = input.split(FileParserParams.SPLIT_PATTERN);
        final String meetingStartDate = trimWhiteSpaceOfString(meetingString[0]);
        final String meetingStartTime = trimWhiteSpaceOfString(meetingString[1]);
        final String meetingDuration = trimWhiteSpaceOfString(meetingString[2]);

        return new Meeting(meetingStartDate, getTimeFormat(meetingStartDate, meetingStartTime), meetingDuration);
    }

    private void getEmployeeMeetingSchedule(@Nonnull final String input,
                                            @Nonnull final EmployeeMeetingSchedule employeeMeetingSchedule) {
        checkNull(input, "input");
        checkNull(employeeMeetingSchedule, "employeeMeetingSchedule");

        final String[] meetingString = input.split(FileParserParams.SPLIT_PATTERN);

        final String submissionDate = trimWhiteSpaceOfString(meetingString[0]);
        final String submissionTime = trimWhiteSpaceOfString(meetingString[1]);
        final String employeeId = trimWhiteSpaceOfString(meetingString[2]);

        employeeMeetingSchedule.setSubmissionTime(getTimeFormat(submissionDate, submissionTime));
        employeeMeetingSchedule.setEmployeeId(employeeId);
    }

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
