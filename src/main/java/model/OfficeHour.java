package model;

import javax.annotation.Nonnull;

import static util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */

/**
 * Class contains all the details of the office hours meetingStartTime and meetingEndTime time.
 */
public class OfficeHour {

    private String officeStartTime;

    private String officeEndTime;

    public OfficeHour(final String officeStartTime, final String officeEndTime) {
        this.setOfficeStartTime(officeStartTime);
        this.setOfficeEndTime(officeEndTime);
    }

    @Nonnull
    public String getOfficeStartTime() {
        return this.officeStartTime;
    }

    private void setOfficeStartTime(@Nonnull final String officeStartTime) {
        checkNull(officeStartTime, "officeStartTime");

        this.officeStartTime = officeStartTime;
    }

    @Nonnull
    public String getOfficeEndTime() {
        return this.officeEndTime;
    }

    private void setOfficeEndTime(@Nonnull final String officeEndTime) {
        checkNull(officeEndTime, "officeEndTime");

        this.officeEndTime = officeEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfficeHour)) return false;

        OfficeHour that = (OfficeHour) o;

        if (!officeStartTime.equals(that.officeStartTime)) return false;
        return officeEndTime.equals(that.officeEndTime);

    }

    @Override
    public int hashCode() {
        int result = officeStartTime.hashCode();
        result = 31 * result + officeEndTime.hashCode();
        return result;
    }
}
