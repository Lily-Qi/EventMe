package edu.usc.eventme;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EventList {
    private ArrayList<Event> eventList;
    private String sorting;
    private String keyWord;

    public EventList() {
        eventList=new ArrayList<Event>();
    };

    public List<Event> getEventList() {
        return eventList;
    }
    public void formList(String sort, String key){

    }
    public void sort(String sort){
        switch (sort) {
            case "cost":
                Collections.sort(eventList, new sortByCost());
        }
    }

    public void addEvent(Event e){
        eventList.add(e);
    }

    public void removeEvent(Event e){
        eventList.remove(e);
    }

    public Event getEvent(int n){
        for(Event e:eventList){
            if(e.getId()==n){
                return e;
            }
        }

        return null;
    }

}

class sortByCost implements Comparator<Event>{
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
}