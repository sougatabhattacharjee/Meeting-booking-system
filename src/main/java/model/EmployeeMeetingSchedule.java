package model;


import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;

import static util.Validator.checkNull;

/**
 * Created by Sougata on 4/30/2016.
 */
public class EmployeeMeetingSchedule {

    protected OfficeHour officeHour;

    protected String employeeId;

    protected LocalDateTime submissionTime;

    protected Meeting meeting;

    public EmployeeMeetingSchedule() {
    }

    public EmployeeMeetingSchedule(String s, String employeeId) {
//        this.setSubmissionTime(convertStringToLocalDateTimeFormat(s));
        this.setEmployeeId(employeeId);
    }

    @Nonnull
    public OfficeHour getOfficeHour() {
        return officeHour;
    }

    public void setOfficeHour(@Nonnull final OfficeHour officeHour) {
        checkNull(officeHour, "officeHour");
        this.officeHour = officeHour;
    }

    @Nonnull
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@Nonnull final String employeeId) {
        checkNull(employeeId, "employeeId");
        this.employeeId = employeeId;
    }

    @Nonnull
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(@Nonnull final String submissionTime) {
        checkNull(submissionTime, "submissionTime");
        this.submissionTime = convertStringToLocalDateTimeFormat(submissionTime);
    }

    @Nonnull
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(@Nonnull final Meeting meeting) {
        checkNull(meeting, "meeting");
        this.meeting = meeting;
    }

    @Nonnull
    private LocalDateTime convertStringToLocalDateTimeFormat(@Nonnull final String time) {
        checkNull(time, "time");

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);

        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeMeetingSchedule)) return false;

        EmployeeMeetingSchedule that = (EmployeeMeetingSchedule) o;

        if (!officeHour.equals(that.officeHour)) return false;
        if (!employeeId.equals(that.employeeId)) return false;
        if (!submissionTime.equals(that.submissionTime)) return false;
        return meeting.equals(that.meeting);
    }

    @Override
    public int hashCode() {
        int result = officeHour.hashCode();
        result = 31 * result + employeeId.hashCode();
        result = 31 * result + submissionTime.hashCode();
        result = 31 * result + meeting.hashCode();
        return result;
    }
}
