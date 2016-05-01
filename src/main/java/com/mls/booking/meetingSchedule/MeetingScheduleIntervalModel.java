package com.mls.booking.meetingSchedule;

import com.mls.booking.model.EmployeeMeetingSchedule;
import org.joda.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.LinkedHashSet;
import java.util.Stack;

import static com.mls.booking.util.Validator.checkNull;

/**
 * Created by Sougata on 4/30/2016.
 */
public class MeetingScheduleIntervalModel implements MeetingScheduleInterval {

    private ScheduleIntervalNode rootNode;

    public void add(@Nonnull final EmployeeMeetingSchedule employeeMeetingSchedule) {
        checkNull(employeeMeetingSchedule, "employeeMeetingSchedule");
        checkNull(employeeMeetingSchedule.getMeeting().getMeetingStartTime(), "startTime");
        checkNull(employeeMeetingSchedule.getMeeting().getMeetingEndTime(), "endTime");

        final LocalDateTime startTime = employeeMeetingSchedule.getMeeting().getMeetingStartTime();
        final LocalDateTime endTime = employeeMeetingSchedule.getMeeting().getMeetingEndTime();

        if (isGreaterThanOrEqual(startTime, endTime))
            throw new IllegalArgumentException("The meetingEndTime " + endTime +
                    " should be greater than meetingStartTime " + startTime);

        ScheduleIntervalNode scheduleIntervalNode = rootNode;

        while (scheduleIntervalNode != null) {
            // Check the overlap and ignore it
            if (overlap(startTime, endTime))
                return;

            scheduleIntervalNode.maxMeetingEndTime = (isGreaterThan(endTime, scheduleIntervalNode.maxMeetingEndTime)) ? endTime : scheduleIntervalNode.maxMeetingEndTime;

            if (isLessThan(startTime, scheduleIntervalNode.meetingStartTime)) {
                if (scheduleIntervalNode.leftNode == null) {
                    scheduleIntervalNode.leftNode = new ScheduleIntervalNode(null, startTime, endTime, endTime, null, employeeMeetingSchedule);
                    return;
                }
                scheduleIntervalNode = scheduleIntervalNode.leftNode;
            } else {
                if (scheduleIntervalNode.rightNode == null) {
                    scheduleIntervalNode.rightNode = new ScheduleIntervalNode(null, startTime, endTime, endTime, null, employeeMeetingSchedule);
                    return;
                }
                scheduleIntervalNode = scheduleIntervalNode.rightNode;
            }
        }
        rootNode = new ScheduleIntervalNode(null, startTime, endTime, endTime, null, employeeMeetingSchedule);
    }

    @Override
    public LinkedHashSet<EmployeeMeetingSchedule> traversal(@Nullable ScheduleIntervalNode rootNode) {
        LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules = new LinkedHashSet<>();

        if (rootNode == null) {
            return employeeMeetingSchedules;
        }

        Stack<ScheduleIntervalNode> stack = new Stack<>();
        stack.push(rootNode);

        while (!stack.isEmpty()) {
            ScheduleIntervalNode top = stack.peek();
            if (top.leftNode != null) {
                stack.push(top.leftNode);
                top.leftNode = null;
            } else {
                employeeMeetingSchedules.add(top.employeeMeetingSchedule);
                stack.pop();
                if (top.rightNode != null) {
                    stack.push(top.rightNode);
                }
            }
        }
        return employeeMeetingSchedules;
    }

    @Nullable
    @Override
    public ScheduleIntervalNode getRoot() {
        return this.rootNode;
    }


    /**
     * Check if the input meeting timing intervals are overlapping with the existing intervals.
     *
     * @param startTime the meetingStartTime of the interval
     * @param endTime   the meetingEndTime of the interval
     *                  return           true if overlap, else false.
     */
    public boolean overlap(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        if (isGreaterThanOrEqual(startTime, endTime))
            throw new IllegalArgumentException("The meetingEndTime " + endTime
                    + " should be greater than meetingStartTime " + startTime);

        ScheduleIntervalNode scheduleIntervalNode = rootNode;

        while (scheduleIntervalNode != null) {
            if (validateIntersection(startTime, endTime, scheduleIntervalNode.meetingStartTime,
                    scheduleIntervalNode.meetingEndTime))
                return true;

            if (goToLeftNode(startTime, endTime, scheduleIntervalNode.leftNode)) {
                scheduleIntervalNode = scheduleIntervalNode.leftNode;
            } else {
                scheduleIntervalNode = scheduleIntervalNode.rightNode;
            }
        }
        return false;
    }

    /**
     * Returns true if there is any validateIntersection in the two time intervals
     * Two intervals such that one of the points coincide:
     * eg: [2015-08-22 15:00, 2015-08-22 16:00] and [2015-08-22 16:00, 2015-08-22 17:00] are NOT considered as intersecting.
     * eg: [2015-08-22 15:00, 2015-08-22 16:00] and [2015-08-22 15:30, 2015-08-22 17:00] are considered as intersecting.
     */
    private boolean validateIntersection(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime,
                                         @Nonnull final LocalDateTime intervalStartTime,
                                         @Nonnull final LocalDateTime intervalEndTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");
        checkNull(intervalStartTime, "intervalStartTime");
        checkNull(intervalEndTime, "intervalEndTime");

        return isLessThan(startTime, intervalEndTime) && isGreaterThan(endTime, intervalStartTime);
    }

    private boolean goToLeftNode(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime,
                                 @Nullable final ScheduleIntervalNode intervalLeftSubtree) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return intervalLeftSubtree != null && isGreaterThan(intervalLeftSubtree.maxMeetingEndTime, startTime);
    }

    private boolean isGreaterThanOrEqual(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");
        return startTime.isAfter(endTime) || startTime.isEqual(endTime);
    }

    private boolean isLessThanOrEqual(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isBefore(endTime) || startTime.isEqual(endTime);
    }

    private boolean isEqual(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isEqual(endTime);
    }

    private boolean isLessThan(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isBefore(endTime);
    }

    private boolean isGreaterThan(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return startTime.isAfter(endTime);
    }
}