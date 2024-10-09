package com.fitnessbooking.services;

import com.fitnessbooking.models.FitnessClass;
import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.models.ClassType;
import com.fitnessbooking.models.WorkoutSession;

import java.util.Date;
//import java.util.Map;

public class AdminService {
    
    // protected static DataStore dataStore;
    // protected Map<String, User> users = dataStore.getUsers();
    // protected Map<String, FitnessClass> classes = dataStore.getClasses();
    private static DataStore dataStore = DataStore.getInstance();
    // AdminService(){
    //     this.dataStore = DataStore.getInstance();
    // }

    public static FitnessClass createClass(String id, ClassType type, int capacity) {
        FitnessClass fitnessClass = new FitnessClass(id, type, capacity);
        dataStore.getClasses().put(fitnessClass.getId(), fitnessClass);
        System.out.println("Created class: " + type + " with capacity " + capacity);
        return fitnessClass;
    }
    
    public static void scheduleClass(FitnessClass fitnessClass, Date scheduledTime) {
        WorkoutSession workoutSession = new WorkoutSession(fitnessClass.getId(), fitnessClass.getType(), fitnessClass.getCapacity(), scheduledTime);
        dataStore.getClasses().put(workoutSession.getId(), workoutSession);
        System.out.println("Scheduled class " + workoutSession.getType() + " at " + scheduledTime);
    }
    
    public static void cancelClass(FitnessClass fitnessClass) {
        WorkoutSession workoutSession = (WorkoutSession) fitnessClass;
        ClassService.cancelClass(workoutSession);
        dataStore.getClasses().remove(workoutSession.getId());
        System.out.println("Cancelled class: " + workoutSession.getType() + " at " + workoutSession.getScheduledTime());
    }

}
