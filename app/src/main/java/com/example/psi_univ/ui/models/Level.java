package com.example.psi_univ.ui.models;

import com.example.psi_univ.R;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String levelName;
    private final String buildingName;
    private final List<String> rooms = new ArrayList<>(); //TODO: change to List<Room>
    private String levelMap;

    public Level(String levelName, String buildingName) {
        this.levelName = levelName;
        this.buildingName = buildingName;

        //TODO: get rooms from database
        for(int i = 50; i <= 60; i++){
            rooms.add("i-" + i);
        }

        rooms.add("amphi-p");
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

    public String getRoom(int index){
        return rooms.get(index);
    }

    public int getRoomCount(){
        return rooms.size();
    }

    public void setLevelMap(String levelMap) {
        this.levelMap = levelMap;
    }
}
