package com.fitnessbooking.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;
import com.fitnessbooking.utils.Validators;

public class User {
    protected String id;
    protected String name;
    protected String email;
    protected String passwordHash;
    protected String tier;
    protected int bookingLimit;
    public Set<String> currentBookings;

    protected DataStore dataStore = DataStore.getInstance();

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
            // For simplicity, we're storing the plain password.
            // In production, always hash and salt passwords.
            this.passwordHash = password; // Replace with proper hashing
        } else {
            throw new CustomException("Password must be at least 6 characters long.");
        }
    }

    private void setTier(String tier) {
        this.tier = tier;
        this.bookingLimit = getBookingLimit(tier);
    }

    private int getBookingLimit(String tier) {
        switch (tier) {
            case "Platinum":
                return 10;
            case "Gold":
                return 5;
            case "Silver":
            default:
                return 3;
        }
    }

    public void register() throws CustomException {
        if (dataStore.getUsers().containsKey(this.email)) {
            throw new CustomException("User already exists with this email.");
        }
        dataStore.getUsers().put(this.email, this);
        System.out.println("User registered: " + this.name);
    }

    public boolean login(String password) throws CustomException {
        User user = dataStore.getUsers().get(this.email);
        if (user != null && user.passwordHash.equals(password)) {
            System.out.println("User logged in: " + this.name);
            return true;
        } else {
            throw new CustomException("Invalid email or password.");
        }
    }

    public void selectPackage(String tier) {
        this.setTier(tier);
        if (this.currentBookings.size() > this.bookingLimit) {
            System.out.println("You have more bookings than your new limit. Please cancel some bookings.");
        } else {
            System.out.println("Package updated to " + tier);
        }
    }

    public void bookClass(FitnessClass fitnessClass) throws CustomException {
        if (this.currentBookings.size() >= this.bookingLimit) {
            throw new CustomException("Booking limit reached.");
        }
        fitnessClass.addAttendee(this);
        this.currentBookings.add(fitnessClass.getId());
        System.out.println("Booked class: " + fitnessClass.getType() + " at " + fitnessClass.getScheduledTime());
    }

    public void cancelBooking(FitnessClass fitnessClass) throws CustomException {
        long timeDifference = (fitnessClass.getScheduledTime().getTime() - System.currentTimeMillis()) / (1000 * 60);
        if (timeDifference < 30) {
            throw new CustomException("Cannot cancel less than 30 minutes before the class starts.");
        }
        if (fitnessClass.removeAttendee(this)) {
            this.currentBookings.remove(fitnessClass.getId());
            System.out.println("Cancelled booking for class: " + fitnessClass.getType() + " at " + fitnessClass.getScheduledTime());
        } else {
            throw new CustomException("You are not booked for this class.");
        }
    }

    // Getters and setters (if needed)
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
