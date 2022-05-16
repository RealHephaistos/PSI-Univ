package com.example.psi_univ;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.logging.Level;

public class Building {
    private final Level[] levels;
    private final Segment[] segments;
    private final String name;

    public Building(JSONObject building) throws JSONException {

        //get building object
        building = building.getJSONObject("building");

        //get building name
        name = building.getString("name");
        Log.d("Building", "Building name: " + name);

        //get the vertices and use them to create segments
        JSONArray verticesArray = building.getJSONArray("vertices");
        assert verticesArray.length() >= 3; //A building's hitbox must be a polygon
        //TODO check if we need to throw a JSONParseException if the JSONObject is not correct
        segments = new Segment[verticesArray.length()];
        for(int i = 0; i < verticesArray.length()-1; i++){
            segments[i] = new Segment(verticesArray.optJSONObject(i), verticesArray.optJSONObject(i + 1));
        }
        segments[verticesArray.length()-1] = new Segment(verticesArray.optJSONObject(verticesArray.length()-1), verticesArray.optJSONObject(0));
        Log.d("Building", "Building vertices: " + Arrays.toString(segments));

        //get the levels
        levels = new Level[building.getInt("levels")]; //TODO add levels
        assert levels.length > 0; //A building must have at least one level (the ground)
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
        return levels.length;
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

        float s, t, v;
        v = -p2x * p1y + p1x * p2y;
        assert v != 0;
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