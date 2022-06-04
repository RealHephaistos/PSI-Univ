package com.example.psi_univ.ui.models;

import android.content.Context;

import com.example.psi_univ.R;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String levelName;
    private final int levelMap;
    private List<Room> rooms;

    public Level(String buildingName, String levelName, List<Room> rooms, Context context) {
        int levelnbr = Integer.parseInt(levelName); //TODO: levelName en int dans la db
        this.levelName = levelName;
        this.rooms = rooms;
        String levelMapName = "ic_"+buildingName+"_";
        if(levelnbr< 0){
            levelMapName +="s"+levelnbr;
        }
        else{
            levelMapName +=levelnbr;
        }
        int id = context.getResources().getIdentifier(levelMapName, "drawable", context.getPackageName());
        if(id == 0){
            this.levelMap = R.drawable.ic_b12d_s1; //TODO: truc par défaut
        }
        else {
            this.levelMap = id;
        }
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
