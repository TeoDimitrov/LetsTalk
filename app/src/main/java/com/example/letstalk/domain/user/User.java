package com.example.letstalk.domain.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.letstalk.domain.roles.CustomerRole;
import com.example.letstalk.domain.roles.Role;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class User implements Parcelable {

    @Exclude
    private static final int START_CHATS = 0;

    @Exclude
    private static final transient int START_TALKS = 0;

    @Exclude
    private static final transient Role DEFAULT_ROLE = new CustomerRole();

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
        this.setChats(START_CHATS);
        this.setTalks(START_TALKS);
        this.setRole(DEFAULT_ROLE);
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

    protected User(Parcel in) {
        birthDate = in.readInt();
        gender = in.readString();
        username = in.readString();
        password = in.readString();
        chats = in.readInt();
        talks = in.readInt();
        role = (Role) in.readValue(Role.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(birthDate);
        dest.writeString(gender);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeInt(chats);
        dest.writeInt(talks);
        dest.writeValue(role);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
