package com.example.psi_univ;

import android.graphics.Point;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Building {
    private final List<Vertex> vertices;
    private final List<Level> levels;//TODO list to array
    private final List<Segment> segments;
    private final String name;

    public Building(JSONObject building) {

        name = building.optString("name");
        Log.d("Building", "Building name: " + name);
        vertices = new ArrayList<>();
        JSONArray verticesArray = building.optJSONArray("vertices");
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
        float maxX = getRightMostPoint();
        if (x > maxX) {
            //if the x coordinate is greater than the right most point, we know it's right of the building
            return false;
        }

        Segment s = new Segment(new Vertex(x, y), new Vertex(maxX, y));
        int intersectCount = 0;
        for (Segment segment : segments) {
            if (isIntersecting(s, segment)) {
                intersectCount++;
            }
        }

        return intersectCount % 2 == 1;
    }

    /**
     * @return the x coordinate of the right most point of the building
     */
    private float getRightMostPoint() {
        float max = vertices.get(0).x;
        for (Vertex p : vertices) {
            if (p.x > max) {
                max = p.x;
            }
        }
        return max;
    }

    /**
     * @param s1 the first segment
     * @param s2 the second segment
     * @return true if the segments intersect, false otherwise
     */
    private boolean isIntersecting(Segment s1, Segment s2) {
        if(ccw(s1.p, s1.q, s2.p) * ccw(s1.p, s1.q, s2.p) > 0){
            return false;
        }

        return ccw(s2.p, s2.q, s1.p) * ccw(s2.p, s2.q, s1.p) <= 0;
    }

    /**
     *
     * @param p the first point
     * @param q the second point
     * @param r the third point
     * @return 1 if the points are in counter clockwise order, -1 if they are in clockwise order, 0 if they are collinear
     */
    private int ccw(Vertex p, Vertex q, Vertex r) {
        return (int)((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y));
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


