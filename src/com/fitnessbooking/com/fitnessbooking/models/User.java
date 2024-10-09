package com.fitnessbooking.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
//import java.util.Map;

//import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;
import com.fitnessbooking.utils.Validators;
import com.fitnessbooking.utils.HashingPassword;

public class User {
    protected String id;
    protected String name;
    protected String email;
    protected String passwordHash;
    protected String tier;
    protected int bookingLimit;
    public Set<String> currentBookings;

   // private static DataStore dataStore = DataStore.getInstance();

    // protected static DataStore dataStore;
    //protected Map<String, User> users = dataStore.getUsers();
    //protected Map<String, FitnessClass> classes = dataStore.getClasses();
    private Object salt;

    public User(String name, String email, String password, String tier) throws CustomException {
        this.id = "user-" + UUID.randomUUID().toString();
        this.name = name;
        this.setEmail(email);
        this.setPassword(password);
        this.setTier(tier);
        this.currentBookings = new HashSet<>();
    }

    private void setEmail(String email) throws CustomException {
        if (Validators.isValidEmail(email)) {
            this.email = email;
        } else {
            throw new CustomException("Invalid email format.");
        }
    }

    private void setPassword(String password) throws CustomException {
        if (Validators.isValidPassword(password)) {
            // Generate a random salt
            this.salt = HashingPassword.generateSalt();
            // Hash the password with the generated salt
            this.passwordHash = HashingPassword.hashPassword(password, this.salt);
        } else {
            throw new CustomException("Password must be at least 6 characters long.");
        }
    }
    

    private void setTier(String tier) {
        this.tier = tier;
        this.bookingLimit = getBookingLimit(tier);
    }

    private int getBookingLimit(String tier) {
        return switch (tier) {
            case "Platinum" -> 10;
            case "Gold" -> 5;
            //case "Basic" -> 3;
            default -> 3;
        };
    }

    public void selectPackage(String tier) {
        this.setTier(tier);
        if (this.currentBookings.size() > this.bookingLimit) {
            System.out.println("You have more bookings than your new limit. Please cancel some bookings.");
        } else {
            System.out.println("Package updated to " + tier);
        }
    }

    // Getters and setters (if needed)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {

        return email;
    }
    public String getPassword() {
        return passwordHash;
    }
    public Object getSalt() {
        return salt;
    }
    public String getTier() {
        return tier;
    }
    public int getBookingLimit() {
        return bookingLimit;
    }
    public Set<String> getCurrentBookings() {
        return currentBookings;
    }

}
