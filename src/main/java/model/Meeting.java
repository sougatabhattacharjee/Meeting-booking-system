package model;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;

import static util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */
public class Meeting {

    protected LocalDate meetingStartDate;

    protected LocalDateTime meetingStartTime;

    protected LocalDateTime meetingEndTime;

    protected int meetingDuration;

    public Meeting(final String meetingStartDate, final String meetingStartTime, final String meetingDuration) {
        this.setMeetingStartDate(convertStringToLocalDate(meetingStartDate));
        this.setMeetingStartTime(convertStringToLocalDateTimeFormat(meetingStartTime));
        this.setMeetingEndTime(convertStringToLocalDateTimeFormat(meetingStartTime).plusHours(Integer.parseInt(meetingDuration)));
        this.setMeetingDuration(Integer.parseInt(meetingDuration));
    }

    @Nonnull
    public LocalDate getMeetingStartDate() {
        return meetingStartDate;
    }

    public void setMeetingStartDate(@Nonnull final LocalDate meetingStartDate) {
        checkNull(meetingStartDate, "meetingStartDate");
        this.meetingStartDate = meetingStartDate;
    }

    @Nonnull
    public LocalDateTime getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(@Nonnull final LocalDateTime meetingStartTime) {
        checkNull(meetingStartTime, "meetingStartTime");
        this.meetingStartTime = meetingStartTime;
    }

    @Nonnull
    public LocalDateTime getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(@Nonnull final LocalDateTime meetingEndTime) {
        checkNull(meetingEndTime, "meetingEndTime");
        this.meetingEndTime = meetingEndTime;
    }

    @Nonnull
    public int getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(@Nonnull final int meetingDuration) {
        checkNull(meetingDuration, "meetingDuration");
        this.meetingDuration = meetingDuration;
    }

    @Nonnull
    private LocalDateTime convertStringToLocalDateTimeFormat(@Nonnull final String time) {
        checkNull(time, "time");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        final LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);

        return localDateTime;
    }

    @Nonnull
    private LocalDate convertStringToLocalDate(@Nonnull final String time) {
        checkNull(time, "time");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        final LocalDate localDate = LocalDate.parse(time, dateTimeFormatter);

        return localDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meeting)) return false;

        Meeting meeting = (Meeting) o;

        if (meetingDuration != meeting.meetingDuration) return false;
        if (!meetingStartDate.equals(meeting.meetingStartDate)) return false;
        if (!meetingStartTime.equals(meeting.meetingStartTime)) return false;
        return meetingEndTime.equals(meeting.meetingEndTime);

    }

    @Override
    public int hashCode() {
        int result = meetingStartDate.hashCode();
        result = 31 * result + meetingStartTime.hashCode();
        result = 31 * result + meetingEndTime.hashCode();
        result = 31 * result + meetingDuration;
        return result;
    }
}
