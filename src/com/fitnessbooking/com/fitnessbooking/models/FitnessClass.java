package com.fitnessbooking.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;

public class FitnessClass {
    private String id;
    private String type;
    private int capacity;
    private Date scheduledTime;
    private Set<String> attendees;  // Set of userIds
    private List<User> waitlist;    // List of User instances

    private DataStore dataStore = DataStore.getInstance();

    public FitnessClass(String type, int capacity) {
        this.id = "class-" + UUID.randomUUID().toString();
        this.type = type;
        this.capacity = capacity;
        this.attendees = new HashSet<>();
        this.waitlist = new ArrayList<>();
    }

    public boolean isFull() {
        return attendees.size() >= capacity;
    }

    public void addAttendee(User user) throws CustomException {
        if (attendees.contains(user.id)) {
            throw new CustomException("User already booked in this class.");
        }
        if (isFull()) {
            addToWaitlist(user);
            System.out.println("Class is full. Added to waitlist.");
        } else {
            attendees.add(user.id);
            System.out.println("User " + user.getName() + " added to class " + this.type);
        }
    }

    public boolean removeAttendee(User user) {
        if (attendees.remove(user.id)) {
            promoteFromWaitlist();
            return true;
        } else if (removeFromWaitlist(user)) {
            return true;
        }
        return false;
    }

    private void addToWaitlist(User user) {
        if (!waitlist.contains(user)) {
            waitlist.add(user);
        } else {
            System.out.println("User already in waitlist.");
        }
    }

    private boolean removeFromWaitlist(User user) {
        return waitlist.remove(user);
    }

    private void promoteFromWaitlist() {
        if (!waitlist.isEmpty()) {
            User nextUser = waitlist.remove(0);
            attendees.add(nextUser.id);
            nextUser.currentBookings.add(this.id);
            System.out.println("User " + nextUser.getName() + " promoted from waitlist to class " + this.type);
        }
    }

    public void cancelClass() {
        // Notify attendees and waitlisted users
        for (String userId : attendees) {
            User user = dataStore.getUsers().get(userId);
            if (user != null) {
                user.currentBookings.remove(this.id);
                System.out.println("Notified " + user.getName() + " about class cancellation.");
            }
        }
        for (User user : waitlist) {
            System.out.println("Notified " + user.getName() + " about class cancellation.");
        }
        attendees.clear();
        waitlist.clear();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Set<String> getAttendees() {
        return attendees;
    }

    public List<User> getWaitlist() {
        return waitlist;
    }
}
