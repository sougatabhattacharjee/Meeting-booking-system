package meetingSchedule;

import model.EmployeeMeetingSchedule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashSet;

/**
 * Created by Sougata on 5/1/2016.
 */
public interface MeetingScheduleInterval {

    /**
     * Adds an interval of meeting schedule to the model. It also checks for the overlap of interval.
     * If there is any overlap then it ignores it
     *
     * @param employeeMeetingSchedule contains all the details of meeting schedule
     */
    void add(@Nonnull EmployeeMeetingSchedule employeeMeetingSchedule);

    /**
     * Traverse over all the intervals and return the lists of intervals as inorder pattern.
     *
     * @param rootNode the root node of the model
     */
    LinkedHashSet<EmployeeMeetingSchedule> traversal(@Nullable ScheduleIntervalNode rootNode);

    @Nullable
    ScheduleIntervalNode getRoot();
}
