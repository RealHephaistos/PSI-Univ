package com.example.psi_univ.models;

import java.util.List;

public class Building {
    private final String buildingName;
    private final List<Level> levelList;

    public Building(String buildingName, List<Level> levelList) {
        this.buildingName = buildingName;
        this.levelList = levelList;
    }

    /**
     * @return the name of the building
     */
    public String getName() {
        return buildingName;
    }

    /**
     * @return the list of levels in the building
     */
    public List<Level> getLevelList() {
        return levelList;
    }
}