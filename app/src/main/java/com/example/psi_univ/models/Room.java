package com.example.psi_univ.models;

public class Room {
    private final String roomName;
    private final String levelName;
    private final String buildingName;

    public Room(String roomName, String buildingName, String levelName) {
        this.roomName = roomName;
        this.buildingName = buildingName;
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return buildingName + ' ' + roomName;
    }
}
