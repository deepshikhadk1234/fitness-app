package com.fitnessbooking.services;

import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;
import com.fitnessbooking.models.FitnessClass;
import com.fitnessbooking.models.User;
import com.fitnessbooking.models.WorkoutSession;

import java.util.Date;
//import java.util.Set;
//import java.util.Map;

public class BookingService {
    protected static DataStore dataStore = DataStore.getInstance();
    // protected static DataStore dataStore;
    //protected Map<String, FitnessClass> classes = dataStore.getClasses();
    //protected Map<String, User> users = dataStore.getUsers();
    

    public static void bookClass(FitnessClass fitnessClass,User user) throws CustomException {
        if (user.currentBookings.size() >= user.getBookingLimit()) {
            throw new CustomException("Booking limit reached.");
        }
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        ClassService.addAttendee(user, fitnessClass);
        user.currentBookings.add(fitnessClass.getId());
        System.out.println("Booked class: " + fitnessClass.getType() + " at " + workoutSession.getScheduledTime());
    }


    public static void cancelBooking(FitnessClass fitnessClass, User user) throws CustomException {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        Date scheduledTime = workoutSession.getScheduledTime();
        long timeDifference = (scheduledTime.getTime() - System.currentTimeMillis()) / (1000 * 60);
        if (timeDifference < 30) {
            throw new CustomException("Cannot cancel less than 30 minutes before the class starts.");
        }
        if (ClassService.removeAttendee(user, fitnessClass)) {
            user.currentBookings.remove(fitnessClass.getId());
            System.out.println("Cancelled booking for class: " + fitnessClass.getType() + " at " + workoutSession.getScheduledTime());
        } else {
            throw new CustomException("You are not booked for this class.");
        }
    }
}
