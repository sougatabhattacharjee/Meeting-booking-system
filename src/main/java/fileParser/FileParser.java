package fileParser;

import errors.InvalidFormatException;

import javax.annotation.Nonnull;

/**
 * Created by Sougata on 4/30/2016.
 */
public interface FileParser {

    /**
     * Check the first line of the input text represents the company office hours, in 24 hour clock format
     * An example format is 0900 1730. Return true if it validates the format otherwise false.
     *
     * @param time time format to be validated
     * @return boolean true | false
     */
    @Nonnull
    boolean validateCompanyOfficeHoursFormat(@Nonnull String time) throws InvalidFormatException;

    /**
     * Check the booking request is in the following format, [request submission time, in the format YYYY-MM-DD HH:MM:SS]
     * [ARCH:employee id]
     * An example format is 2015-08-17 10:17:06 EMP001. Return true if it validates the format otherwise false.
     *
     * @param bookingRequestFormat booking request to be validated
     * @return boolean true | false
     */
    @Nonnull
    boolean validateBookingRequestFormat(@Nonnull String bookingRequestFormat) throws InvalidFormatException;

    /**
     * Check the booking request is in the following format, [meeting start time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting
     * duration in hours]
     * An example format is 2015-08-21 09:00 2. Return true if it validates the format otherwise false.
     *
     * @param meetingScheduleFormat meeting start time to be validated
     * @return boolean true | false
     */
    @Nonnull
    boolean validateMeetingScheduleFormat(@Nonnull String meetingScheduleFormat) throws InvalidFormatException;


}
