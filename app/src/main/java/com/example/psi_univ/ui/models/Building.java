package com.example.psi_univ.ui.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Building {
    private final Segment[] segments;
    private String buildingName;
    private List<Level> levelList;

    public Building(JSONObject building){

        //get building object
        try {
            building = building.getJSONObject("building");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get building name
        try {
            buildingName = building.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get the vertices and use them to create segments
        JSONArray verticesArray = null;
        try {
            verticesArray = building.getJSONArray("vertices");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert verticesArray != null && verticesArray.length() >= 3; //A building's hitbox must be a polygon
        //TODO check if we need to throw a JSONParseException if the JSONObject is not correct
        segments = new Segment[verticesArray.length()];
        for(int i = 0; i < verticesArray.length()-1; i++){
            segments[i] = new Segment(verticesArray.optJSONObject(i), verticesArray.optJSONObject(i + 1));
        }
        segments[verticesArray.length()-1] = new Segment(verticesArray.optJSONObject(verticesArray.length()-1), verticesArray.optJSONObject(0));

    }

    public Building(String buildingName, List<Level> levelList){
        this.buildingName = buildingName;
        this.levelList = levelList;
        //TODO: Fetch the building from the database
        segments = new Segment[0];
    }

    /**
     * @return the name of the building
     */
    public String getName() {
        return buildingName;
    }

    /**
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return true if the point is inside the building, false otherwise
     */
    public boolean isInBuilding(float x, float y) {
        x = Math.round(x * 1000) / 1000.0f;
        y = Math.round(y * 1000) / 1000.0f;
        Log.d("Building", "round x: " + x + " y: " + y);

        //Cast a ray from the point to the right border of the map
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

        float s, t, v;
        v = -p2x * p1y + p1x * p2y;
        Log.d("Building", "v: " + v);
        //assert v != 0;
        if (v == 0) {
            return false;
        }

        s = (-p1y * (s1.p.x - s2.p.x) + p1x * (s1.p.y - s2.p.y)) / v;
        t = (p2x * (s1.p.y - s2.p.y) - p2y * (s1.p.x - s2.p.x)) / v;

        return s >= 0 && s <= 1 && t >= 0 && t <= 1;
    }

    private static class Vertex {
        private final float x;
        private final float y;

        public Vertex(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vertex(JSONObject vertex) {
            x = (float)vertex.optDouble("x");
            y = (float)vertex.optDouble("y"); //TODO probably need to throw a JSONException if the JSONObject is not correct
        }
    }



    private static class Segment {
        private final Vertex p;
        private final Vertex q;

        public Segment(Vertex p, Vertex q) {
            this.p = p;
            this.q = q;
        }

        public Segment(JSONObject v1, JSONObject v2) {
            p = new Vertex(v1);
            q = new Vertex(v2);
        }
    }
}