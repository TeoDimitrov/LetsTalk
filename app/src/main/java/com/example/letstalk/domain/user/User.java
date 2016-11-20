package com.example.letstalk.domain.user;

import com.example.letstalk.domain.roles.Customer;
import com.example.letstalk.domain.roles.Role;

import java.util.Calendar;
public class User {

    private static final int START_CHATS = 0;

    private static final int START_TALKS = 0;

    private static final Role DEFAULT_ROLE = new Customer();

    private int birthDate;

    private String gender;

    private String username;

    private String password;

    private int chats;

    private int talks;

    private Role role;

    @SuppressWarnings("unused")
    public User(){
        super();
    }

    public User(int birthDate, String gender, String username, String password) {
        this.setBirthDate(birthDate);
        this.setGender(gender);
        this.setUsername(username);
        this.setPassword(password);
        this.setChats(START_CHATS);
        this.setTalks(START_TALKS);
        this.setRole(DEFAULT_ROLE);
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

    public int getChats() {
        return this.chats;
    }

    public void setChats(int chats) {
        this.chats = chats;
    }

    public int getTalks() {
        return this.talks;
    }

    public void setTalks(int talks) {
        this.talks = talks;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
