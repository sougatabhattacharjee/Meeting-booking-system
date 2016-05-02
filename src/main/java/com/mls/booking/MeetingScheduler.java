package com.mls.booking;

import com.mls.booking.errors.InvalidFileFormatException;
import com.mls.booking.errors.InvalidFormatException;
import com.mls.booking.fileParser.FileValidator;
import com.mls.booking.fileParser.FileValidatorImpl;
import com.mls.booking.meetingSchedule.MeetingScheduleInterval;
import com.mls.booking.meetingSchedule.MeetingScheduleIntervalModel;
import com.mls.booking.meetingSchedule.ScheduleIntervalNode;
import com.mls.booking.model.EmployeeMeetingSchedule;
import com.mls.booking.model.Result;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;

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
