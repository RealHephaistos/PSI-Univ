package com.example.psi_univ.ui.models;

import java.util.Calendar;
import java.util.List;

public class Room {
    private final String roomName;
    private List<Event> events;

    public Room(String roomName, List<Event> events) {
        this.roomName = roomName;
        this.events = events;
        //events.sort(Comparator.comparing(Event::getStart)); //TODO: try to implement binary search
    }

    public boolean isAvailableAt(Calendar d) {
        if (events == null) return true;
        for (Event e : events) {
            if (e.isOverlapping(d)) {
                return false;
            }
            if (e.getStart().after(d)) {
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
        for (Event e : events) {
            if (e.getStart().after(d)) {
                return e.getStart();
            }
        }
        return Calendar.getInstance();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
