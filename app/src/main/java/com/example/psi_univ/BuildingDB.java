package com.example.psi_univ;

public class BuildingDB {
    private String building;
    private int floor;
    private String room;
    private int type;
    private int capacity;
    private String info;

    public BuildingDB(String building, int floor, String room, int type, int capacity, String info) {
        this.building = building;
        this.room = room;
        this.floor = floor;
        this.type = type;
        this.capacity = capacity;
        this.info = info;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }

    public int getFloor() {
        return floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getInfo() {
        return info;
    }

    public int getType() {
        return type;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setType(int type) {
        this.type = type;
    }
}
