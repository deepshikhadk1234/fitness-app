package com.fitnessbooking.utils;

import java.util.regex.Pattern;

public class Validators {

    public static boolean isValidEmail(String email) {
        // Simple regex for email validation
        String regex = "^(.+)@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        // Password must be at least 6 characters
        return password != null && password.length() >= 6;
    }
}
