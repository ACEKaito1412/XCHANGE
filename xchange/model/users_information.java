package com.project.xchange.model;

public class users_information {
    public String Email, FName, LName, UserType;

    public users_information(){

    }

    public users_information(String email, String FName, String LName, String userType) {
        Email = email;
        this.FName = FName;
        this.LName = LName;
        UserType = userType;
    }
}
