package edu.usc.eventme;

import android.net.Uri;

public class User {
    private String userName;
    private String userEmail;
    private String userBirthday;
    private String userPhoto;
    private EventList userEventList;

    public User() {}

    public User(String userName, String userEmail, String userBirthday, String userPhoto) {
        this.userBirthday = userBirthday;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhoto = userPhoto;
        userEventList = null;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public EventList getUserEventList() {
        return userEventList;
    }
}
