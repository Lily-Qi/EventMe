package edu.usc.eventme;
import  java.util.ArrayList;
public class Event {
    private String eventTitle;
    private int id;
    private String eventType;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private int registered;
    private double cost;
    private String content;
    private String location;
    private String photo;
    private boolean parking;
    private ArrayList<String> times;

    public Event() {
        times=new ArrayList<String>();
    }

    public String getEventTitle() {
        return eventTitle;
    }
    public int getId(){return id;}
    public String getEventType(){return eventType;}
    public ArrayList<String> getTimes(){
        return times;
    }
    public int getRegistered(){ return registered;}
    public double getCost(){return cost;}
    public String getContent(){return content;}
    public String getLocation(){return location;}
    public  boolean getParking(){return parking;}
}
