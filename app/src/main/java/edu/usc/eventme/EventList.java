package edu.usc.eventme;
import java.io.Serializable;
import java.util.Comparator;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EventList implements Serializable {
    private ArrayList<Event> eventList;
    private String sorting;
    private String keyWord;

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
        }
    }

    public void addEvent(Event e){
        eventList.add(e);
    }

    public void removeEvent(Event e){
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getId().equals(e.getId())) {
                eventList.remove(eventList.get(i));
                return;
            }
        }

    }

    public Event getEvent(String n){
        for(Event e:eventList){
            if(e.getId().equals(n)){
                return e;
            }
        }

        return null;
    }

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
/*class sortByDistance implements Comparator<Event>{
    public int compare(Event a, Event b){
        if(a.getCost()>b.getCost()){
            return 1;
        }
        else if(a.getCost()<b.getCost()){
            return -1;
        }
        else{
            return 0;
        }
    }
}*/
