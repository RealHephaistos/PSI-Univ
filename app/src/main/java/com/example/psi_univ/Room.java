package com.example.psi_univ;

import java.util.List;

public class Room {

    private String name;
    private List<EventPSI> events;

    public Room(){}

    public Room(String name, List<EventPSI> events){
        this.name = name;
        this.events = events;
    }

    public List<EventPSI> getEvents() {
        return events;
    }

    public void setEvents(List<EventPSI> events) {
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n" + name +
                "\n" + events.size() +
                "\n" + events;
    }
}
