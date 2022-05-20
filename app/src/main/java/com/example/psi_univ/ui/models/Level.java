package com.example.psi_univ.ui.models;

import com.example.psi_univ.R;

public class Level {
    private final String levelName;
    private final String buildingName;

    public Level(String levelName, String buildingName) {
        this.levelName = levelName;
        this.buildingName = buildingName;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public int getLevelMap() {
        return R.drawable.ic_level1; //TODO: get level map from database
    }
}
