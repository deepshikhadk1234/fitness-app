package com.fitnessbooking.services;

import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;
import com.fitnessbooking.utils.HashingPassword;
import com.fitnessbooking.models.User;
//import java.util.Map;

public class AuthService {
    protected static DataStore dataStore = DataStore.getInstance();
    //protected static DataStore dataStore;
    //protected Map<String, User> users = dataStore.getUsers();
    //protected Map<String, FitnessClass> classes = dataStore.getClasses();
    
    public static void register(User user) throws CustomException {
        if (dataStore.getUsers().containsKey(user.getEmail())) {
            throw new CustomException("User already exists with this email.");
        }
        dataStore.getUsers().put(user.getEmail(), user);
        System.out.println("User registered: " + user.getName());
    }

    public static User login(String email, String password) throws CustomException {
        User user = dataStore.getUsers().get(email);
        if (user != null && user.getPassword().equals(HashingPassword.hashPassword(password, user.getSalt()))) {
            System.out.println("User logged in: " + user.getName());
            return user;
        } else {
            throw new CustomException("Invalid email or password.");
        }
    }

}
