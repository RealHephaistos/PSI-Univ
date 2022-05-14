package com.example.psi_univ;

import android.graphics.Point;

import java.util.List;
import java.util.logging.Level;

public class Building {
    private List<Point> vertices;
    private List<Level> levels;
    private String name;

    public Building(String name){
        this.name = name;
    }
}
