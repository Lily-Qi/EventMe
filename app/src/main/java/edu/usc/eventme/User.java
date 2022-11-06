package edu.usc.eventme;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

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
        userEventList = new EventList();
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

    //TODO check conflict time
    public boolean checkConflict(Event event) {
        String date = event.getEndDate();
        String startTime = event.getStartTime();
        String endTime = event.getEndTime();

        return false;
    }

    public boolean eventExist(String eventID) {
        ArrayList<Event> currList = userEventList.getEventList();
        for (int i = 0; i < currList.size(); i++) {
            if (eventID.equals(currList.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean registerEvent(Event event) {
        userEventList.addEvent(event);
        return true;
    }

    public boolean removeEvent(Event event) {
        userEventList.removeEvent(event);
        return true;
    }
}
