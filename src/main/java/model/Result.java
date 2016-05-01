package model;

/**
 * Created by Sougata on 5/1/2016.
 */
public class Result {

    private String employeeId;

    private String meetingStartTime;

    private String meetingEndTime;

    public Result(String employeeId, String meetingStartTime, String meetingEndTime) {
        this.setEmployeeId(employeeId);
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMeetingStartTime() {
        return this.meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getMeetingEndTime() {
        return this.meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;

        Result result = (Result) o;

        if (!employeeId.equals(result.employeeId)) return false;
        if (!meetingStartTime.equals(result.meetingStartTime)) return false;
        return meetingEndTime.equals(result.meetingEndTime);

    }

    @Override
    public int hashCode() {
        int result = employeeId.hashCode();
        result = 31 * result + meetingStartTime.hashCode();
        result = 31 * result + meetingEndTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getMeetingStartTime() + " " + getMeetingEndTime() + " " + getEmployeeId();
    }
}
