package model;


import fileParser.FileParserParams;
import org.joda.time.LocalDateTime;

import javax.annotation.Nonnull;

import static util.Helpers.convertStringToLocalDateTimeFormat;
import static util.Validator.checkNull;

/**
 * Created by Sougata on 4/30/2016.
 */

/**
 * Class contains all the details of meeting scheduling system. It contains the office hours, employee id,
 * submission time details and meeting meetingStartTime time details.
 */
public class EmployeeMeetingSchedule {

    private OfficeHour officeHour;

    private String employeeId;

    private LocalDateTime submissionTime;

    private Meeting meeting;

    public EmployeeMeetingSchedule() {
    }

    @Nonnull
    public OfficeHour getOfficeHour() {
        return this.officeHour;
    }

    public void setOfficeHour(@Nonnull final OfficeHour officeHour) {
        checkNull(officeHour, "officeHour");

        this.officeHour = officeHour;
    }

    @Nonnull
    public String getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(@Nonnull final String employeeId) {
        checkNull(employeeId, "employeeId");

        this.employeeId = employeeId;
    }

    @Nonnull
    public LocalDateTime getSubmissionTime() {
        return this.submissionTime;
    }

    public void setSubmissionTime(@Nonnull final String submissionTime) {
        checkNull(submissionTime, "submissionTime");

        this.submissionTime = convertStringToLocalDateTimeFormat
                (submissionTime, FileParserParams.SIMPLE_DATE_FORMAT_HHMMSS);
    }

    @Nonnull
    public Meeting getMeeting() {
        return this.meeting;
    }

    public void setMeeting(@Nonnull final Meeting meeting) {
        checkNull(meeting, "meeting");

        this.meeting = meeting;
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
