package com.example.psi_univ;

import android.graphics.Point;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Building {
    private final List<Vertex> vertices;
    private final List<Level> levels;//TODO list to array
    private final List<Segment> segments;
    private final String name;

    public Building(JSONObject building) throws JSONException {

        JSONObject buildingObject = building.getJSONObject("building");

        name = buildingObject.getString("name");
        Log.d("Building", "Building name: " + name);

        vertices = new ArrayList<>();
        JSONArray verticesArray = buildingObject.getJSONArray("vertices");
        for(int i = 0; i < verticesArray.length(); i++){
            JSONObject vertex = verticesArray.optJSONObject(i);
            vertices.add(new Vertex((float)vertex.optDouble("x"), (float)vertex.optDouble("y")));
        }
        Log.d("Building", "Building vertices: " + vertices.toString());

        levels = new ArrayList<>();
        segments = new ArrayList<>();
        for(int i = 0; i < vertices.size()-1; i++){
            segments.add(new Segment(vertices.get(i), vertices.get(i + 1)));
        }
        segments.add(new Segment(vertices.get(vertices.size() - 1), vertices.get(0)));
    }

    /**
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * @return the number of levels
     */
    public int getNumberOfLevels(){
        return levels.size();
    }

    /**
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return true if the point is inside the building, false otherwise
     */
    public boolean isInBuilding(float x, float y) {

        Segment s = new Segment(new Vertex(x, y), new Vertex(1, y));
        int intersectCount = 0;
        for (Segment segment : segments) {
            if (isIntersecting(s, segment)) {
                intersectCount++;
            }
        }

        return intersectCount % 2 == 1;
    }

    /**
     * @param s1 the first segment
     * @param s2 the second segment
     * @return true if the segments intersect, false otherwise
     */
    private boolean isIntersecting(Segment s1, Segment s2) {

        float p1x, p1y, p2x, p2y;
        p1x = s1.q.x - s1.p.x;
        p1y = s1.q.y - s1.p.y;
        p2x = s2.q.x - s2.p.x;
        p2y = s2.q.y - s2.p.y;

        float s, t;
        s = (-p1y * (s1.p.x - s2.p.x) + p1x * (s1.p.y - s2.p.y)) / (-p2x * p1y + p1x * p2y);
        t = (p2x * (s1.p.y - s2.p.y) - p2y * (s1.p.x - s2.p.x)) / (-p2x * p1y + p1x * p2y);

        return s >= 0 && s <= 1 && t >= 0 && t <= 1;
    }

    public static class Vertex {
        private final float x;
        private final float y;

        public Vertex(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Segment {
        private final Vertex p;
        private final Vertex q;

        public Segment(Vertex p, Vertex q) {
            this.p = p;
            this.q = q;
        }
    }
}


