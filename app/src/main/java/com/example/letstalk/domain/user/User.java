package com.example.letstalk.domain.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.letstalk.domain.roles.CustomerRole;
import com.example.letstalk.domain.roles.Role;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Parcelable {

    @Exclude
    private static final int PAID_CHATS = 3;

    @Exclude
    private static final int START_CHATS = 0;

    @Exclude
    private static final int PAID_TALKS = 3;

    @Exclude
    private static final transient int START_TALKS = 0;

    @Exclude
    private static final transient Role DEFAULT_ROLE = new CustomerRole();

    private int birthDate;

    private String gender;

    private String email;

    private String password;

    private int paidChats;

    private int chats;

    private int paidTalks;

    private int talks;

    private Role role;

    private String notes;

    @SuppressWarnings("unused")
    public User() {
        super();
        this.setPaidChats(PAID_CHATS);
        this.setChats(START_CHATS);
        this.setPaidTalks(PAID_TALKS);
        this.setTalks(START_TALKS);
        this.setRole(DEFAULT_ROLE);
    }

    public User(int birthDate, String gender, String username, String password) {
        this();
        this.setBirthDate(birthDate);
        this.setGender(gender);
        this.setEmail(username);
        this.setPassword(password);
    }

    protected User(Parcel in) {
        birthDate = in.readInt();
        gender = in.readString();
        email = in.readString();
        password = in.readString();
        paidChats = in.readInt();
        chats = in.readInt();
        paidTalks = in.readInt();
        talks = in.readInt();
        role = (Role) in.readValue(Role.class.getClassLoader());
        notes = in.readString();
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPaidChats() {
        return this.paidChats;
    }

    public void setPaidChats(int paidChats) {
        this.paidChats = paidChats;
    }

    public int getPaidTalks() {
        return this.paidTalks;
    }

    public void setPaidTalks(int paidTalks) {
        this.paidTalks = paidTalks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(birthDate);
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(paidChats);
        dest.writeInt(chats);
        dest.writeInt(paidTalks);
        dest.writeInt(talks);
        dest.writeValue(role);
        dest.writeString(notes);
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
