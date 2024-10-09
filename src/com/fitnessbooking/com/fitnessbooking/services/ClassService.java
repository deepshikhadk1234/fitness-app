package com.fitnessbooking.services;

import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;
import com.fitnessbooking.models.FitnessClass;
import com.fitnessbooking.models.User;
import com.fitnessbooking.models.WorkoutSession;

public class ClassService {
    private static DataStore dataStore = DataStore.getInstance();

    public static boolean isFull(FitnessClass fitnessClass) {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        return workoutSession.getAttendees().size() >= workoutSession.getCapacity();
    }

    public static void addAttendee(User user, FitnessClass fitnessClass) throws CustomException {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        if (workoutSession.getAttendees().contains(user.getId())) {
            throw new CustomException("User already booked in this class.");
        }
        if (isFull(fitnessClass)) {
            addToWaitlist(user, fitnessClass);
            System.out.println("Class is full. Added to waitlist.");
        } else {
            workoutSession.getAttendees().add(user.getId());
            System.out.println("User " + user.getName() + " added to class " + fitnessClass.getType());
        }
    }

    public static boolean removeAttendee(User user, FitnessClass fitnessClass) {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        if (workoutSession.getAttendees().remove(user.getId())) {
            promoteFromWaitlist(workoutSession);
            return true;
        } else if (removeFromWaitlist(user, fitnessClass)) {
            return true;
        }
        return false;
    }

    private static void addToWaitlist(User user, FitnessClass fitnessClass) {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        if (!workoutSession.getWaitlist().contains(user)) {
            workoutSession.getWaitlist().add(user);
        } else {
            System.out.println("User already in waitlist.");
        }
    }

    private static boolean removeFromWaitlist(User user, FitnessClass fitnessClass) {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        return workoutSession.getWaitlist().remove(user);
    }

    private static void promoteFromWaitlist(FitnessClass fitnessClass) {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        if (!workoutSession.getWaitlist().isEmpty()) {
            User nextUser = workoutSession.getWaitlist().remove(0);
            workoutSession.getAttendees().add(nextUser.getId());
            nextUser.currentBookings.add(workoutSession.getId());
            System.out.println("User " + nextUser.getName() + " promoted from waitlist to class " + workoutSession.getType());
        }
    }

    public static void cancelClass(FitnessClass fitnessClass) {
        // Notify attendees and waitlisted users
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        for (String userId : workoutSession.getAttendees()) {
            User user = dataStore.getUsers().get(userId);
            if (user != null) {
                user.currentBookings.remove(fitnessClass.getId());
                System.out.println("Notified " + user.getName() + " about class cancellation.");
            }
        }
        for (User user : workoutSession.getWaitlist()) {
            System.out.println("Notified " + user.getName() + " about class cancellation.");
        }
        workoutSession.getAttendees().clear();
        workoutSession.getWaitlist().clear();
    }
}
