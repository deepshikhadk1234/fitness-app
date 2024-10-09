package com.fitnessbooking.models;

import java.util.Date;
import java.util.Set;
import java.util.List;

public class WorkoutSession extends FitnessClass {
    public WorkoutSession(String id, ClassType type, int capacity, Date scheduledTime) {
        super(id, type, capacity);
    }

    private Date scheduledTime;
    private Set<String> attendees;  // Set of userIds
    private List<User> waitlist;
    
    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime=scheduledTime;
    }

    public Set<String> getAttendees() {
        return attendees;
    }

    public List<User> getWaitlist() {
        return waitlist;
    }

}
