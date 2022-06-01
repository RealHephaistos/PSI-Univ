package com.example.psi_univ.ui.models;

import com.example.psi_univ.R;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String levelName;
    private final int levelMap;
    private List<Room> rooms;

    public Level(String levelName, List<Room> rooms) {
        this.levelName = levelName;
        this.rooms = rooms;
        levelMap = R.drawable.ic_arrow;
        //this.levelMap = R.drawable.ic_level1;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevelMap() {
        return levelMap;
    }

    public int getRoomCount() {
        return rooms.size();
    }

    public Room getRoomAt(int position) {
        return rooms.get(position);
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
