package edu.usc.eventme;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EventList implements Serializable {
    private ArrayList<Event> eventList;
    private String sorting;
    private String keyWord;

    private double currentlat;
    private double currentlon;
    public EventList() {
        eventList=new ArrayList<Event>();
    };

    public ArrayList<Event> getEventList() {
        return eventList;
    }
    public void formList(String sort, String key){

    }
    public void sort(String sort){
        switch (sort) {
            case "cost":
                Collections.sort(eventList, new sortByCost());
            case "distance":
                //Collections.sort(eventList, new sortByDistance());
        }
    }

    public void setCurrentlat(double lat){currentlat=lat;}
    public void setCurrentlon(double lon){currentlon=lon;}

    public void sortbydistoloc(double lat, double lon){
        currentlon=lon;
        currentlat=lat;
        Collections.sort(eventList, new sortByDistance());
    }

    public void sortbydis(){
        Collections.sort(eventList, new sortByDistance());
    }

    public double getCurrentlat() {
        return currentlat;
    }

    public double getCurrentlon() {
        return currentlon;
    }

    public void addEvent(Event e){
        eventList.add(e);
    }

    public void removeEvent(Event e){
        eventList.remove(e);
    }

    public Event getEvent(String n){
        for(Event e:eventList){
            if(e.getID().equals(n)){
                return e;
            }
        }

        return null;
    }

    public ArrayList<Event> getList(){
        return eventList;
    }


    class sortByCost implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.getCost().length()>b.getCost().length()){
                return 1;
            }
            else if(a.getCost().length()<b.getCost().length()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    //need to calculate distance first
    class sortByDistance implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.findDis(currentlat, currentlon)>b.findDis(currentlat, currentlon)){
                return 1;
            }
        else if(a.findDis(currentlat, currentlon)<b.findDis(currentlat, currentlon)){
                return -1;
            }
            else{
                return 0;
            }
        }
    }
}

