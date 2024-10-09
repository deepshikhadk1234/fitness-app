package com.fitnessbooking.models;


import com.fitnessbooking.exceptions.CustomException;

public class Admin extends User {

    public Admin(String name, String email, String password, String tier) throws CustomException {
        super(name, email, password, tier);
    }
}
