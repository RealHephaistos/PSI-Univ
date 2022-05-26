package com.example.psi_univ.ui.models;

import com.example.psi_univ.R;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String levelName;
    private final String buildingName;
    private List<Room> rooms = new ArrayList<>(); //TODO: change to List<Room>
    private String levelMap;

    public Level(String levelName, String buildingName) {
        this.levelName = levelName;
        this.buildingName = buildingName;

        //TODO: get rooms from database
        for(int i = 50; i <= 60; i++){
            rooms.add(new Room ("i-" + i, Room.dummyEvents()));
        }

        rooms.add(new Room ("amphi-p", Room.dummyEvents()));
    }

    public String getLevelName() {
        return levelName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public int getLevelMap() {
        return R.drawable.ic_level1;
        //TODO: get level map from database
    }

    public int getRoomCount(){
        return rooms.size();
    }

    public Room getRoomName(int position){
        return rooms.get(position);
    }

    public void setLevelMap(String levelMap) {
        this.levelMap = levelMap;
    }

    public void addRoom(Room room){
        if (this.rooms == null) {
            this.rooms = new ArrayList<>();
        }
        this.rooms.add(room);
    }
}
