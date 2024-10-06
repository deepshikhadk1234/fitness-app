package com.fitnessbooking.data;

import java.util.HashMap;
import java.util.Map;

import com.fitnessbooking.models.FitnessClass;
import com.fitnessbooking.models.User;

public class DataStore {
    private static DataStore instance = null;

    private Map<String, User> users;
    private Map<String, FitnessClass> classes;

    private DataStore() {
        users = new HashMap<>();
        classes = new HashMap<>();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            synchronized (DataStore.class) {
                if (instance == null) {
                    instance = new DataStore();
                }
            }
        }
        return instance;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, FitnessClass> getClasses() {
        return classes;
    }
}
