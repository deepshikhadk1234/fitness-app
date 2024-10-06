package com.fitnessbooking.models;

import java.util.Date;

import com.fitnessbooking.exceptions.CustomException;

public class Admin extends User {

    public Admin(String name, String email, String password) throws CustomException {
        super(name, email, password, "Platinum");
    }

    public FitnessClass createClass(String type, int capacity) {
        FitnessClass fitnessClass = new FitnessClass(type, capacity);
        dataStore.getClasses().put(fitnessClass.getId(), fitnessClass);
        System.out.println("Created class: " + type + " with capacity " + capacity);
        return fitnessClass;
    }

    public void scheduleClass(FitnessClass fitnessClass, Date scheduledTime) {
        fitnessClass.setScheduledTime(scheduledTime);
        System.out.println("Scheduled class " + fitnessClass.getType() + " at " + scheduledTime);
    }

    public void cancelClass(FitnessClass fitnessClass) {
        fitnessClass.cancelClass();
        dataStore.getClasses().remove(fitnessClass.getId());
        System.out.println("Cancelled class: " + fitnessClass.getType() + " at " + fitnessClass.getScheduledTime());
    }
}
