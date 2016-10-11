package com.example.letstalk.users;

import java.util.Calendar;
public class User {
    private int birthDate;
    private String gender;
    private String username;
    private String password;

    public User(int birthDate, String gender, String username, String password) {
        this.birthDate = birthDate;
        this.gender = gender;
        this.username = username;
        this.password = password;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);

        if (birthDate > currentYear - 18){
            //throw
        }
        else if (birthDate<1900){
            //throw
        }

        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == " "){
            //throw
        }
        if (gender !="m" || gender != "f"){
            //throw
        }

        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password ==null){
            //throw
        }
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null){
            //throw
        }

        this.username = username;
    }
}
