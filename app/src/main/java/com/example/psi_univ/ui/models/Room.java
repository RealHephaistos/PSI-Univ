package com.example.psi_univ.ui.models;

import com.example.psi_univ.EventPSI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Room {
    private final String roomName;
    private List<Event> events;

    public Room(String roomName, List<Event> events) {
        this.roomName = roomName;
        this.events = events;
        events.sort(Comparator.comparing(Event::getStart)); //TODO: try to implement binary search
    }

    public Room (String roomName, List<EventPSI> events, boolean dummy) {
        this.roomName = roomName;
        this.events = new ArrayList<>();
        for (EventPSI event : events){
            Calendar start = Calendar.getInstance();
            start.setTime(event.getStartTime());
            Calendar end = Calendar.getInstance();
            end.setTime(event.getEndTime());
            this.events.add(new Event(start,end, event.getSubject()));
        }
    }

    public boolean isAvailableAt(Calendar d) {
        for (Event e : events) {
            if (e.isOverlapping(d)) {
                return false;
            }
            if(e.getStart().after(d)){
                return true;
            }
        }
        return true;
    }

    public String getRoomName() {
        return roomName;
    }

    public Calendar getNextEvent() {
        Calendar d = Calendar.getInstance();
        for(Event e : events){
            if(e.getStart().after(d)){
                return e.getStart();
            }
        }
        return null;
    }

    public static List<Event> dummyEvents() {//TODO remove this
        List<Event> dummy = new ArrayList<>();
        for(int i = 0; i < 24; i+=2) {
            Calendar start = Calendar.getInstance();
            start.set(2022, 5, 24, i, 0);
            Calendar end = Calendar.getInstance();
            end.set(2022, 5, 24, i+2, 0);
            dummy.add(new Event(start, end, "dummy"));
        }
        return dummy;
    }

    public List<EventPSI> getEvents(){
        List<EventPSI> events = new ArrayList<>();
        for (Event e : this.events){
            Date start = e.getStart().getTime();
            Date end = e.getEnd().getTime();
            EventPSI event = new EventPSI();
            event.setStartTime(start);
            event.setEndTime(end);
            event.setSubject(e.getSubject());
            events.add(event);
        }
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
