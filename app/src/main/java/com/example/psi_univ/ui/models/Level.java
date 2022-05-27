package com.example.psi_univ.ui.models;

import android.graphics.drawable.VectorDrawable;

import androidx.core.graphics.PathParser;

import com.example.psi_univ.R;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String levelName;
    private List<Room> rooms = new ArrayList<>();

    public Level(String levelName, List<Room> rooms) {
        this.levelName = levelName;

        //TODO: get rooms from database
        rooms = new ArrayList<>();
        for(int i = 50; i <= 60; i++){
            rooms.add(new Room ("i-" + i, Room.dummyEvents()));
        }
        rooms.add(new Room ("amphi-p", Room.dummyEvents()));

        this.rooms = rooms;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevelMap() {
        return R.drawable.ic_level1;
        //TODO: get level map from database
    }

    public int getRoomCount(){
        return rooms.size();
    }

    public Room getRoomAt(int position){
        return rooms.get(position);
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
