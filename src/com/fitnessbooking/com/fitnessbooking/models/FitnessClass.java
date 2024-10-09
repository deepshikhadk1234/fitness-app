package com.fitnessbooking.models;


//import com.fitnessbooking.data.DataStore;

public class FitnessClass {
    private String id;
    private ClassType type;
    private int capacity;
       // List of User instances

    // private DataStore dataStore = DataStore.getInstance();
    // protected DataStore dataStore;
    // protected Map<String, User> users = dataStore.getUsers();
    // protected Map<String, FitnessClass> classes = dataStore.getClasses();
    //private static int idCounter = 1;

    public FitnessClass(String id, ClassType type, int capacity) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        // this.scheduledTime = scheduledTime;
        // this.attendees = new HashSet<>();
        // this.waitlist = new ArrayList<>();
    }


    // Getters and setters
    public String getId() {
        return id;
    }

    public ClassType getType() {
        return type;
    }
    public int getCapacity() {
        return capacity;
    }


}


