package com.example.psi_univ.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.activities.BuildingActivity;
import com.github.chrisbanes.photoview.PhotoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapPhotoView extends PhotoView {
    private final List<Polygon> polygons;
    private final float width = 2374;
    private final float height = 2815;

    public MapPhotoView(Context context, AttributeSet attr) {
        //Create the PhotoView
        super(context, attr);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageResource(R.drawable.ic_map);

        //Get the polygons from the XML file
        polygons = new ArrayList<>();
        XmlResourceParser xml = context.getResources().getXml(R.xml.maps);
        try {
            xml.next();
            int eventType = xml.getEventType();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    if (xml.getName().equals("area")) {
                        polygons.add(new Polygon(xml.getAttributeValue(null, "coords"), xml.getAttributeValue(null, "title")));
                    }
                }
                eventType = xml.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MapPhotoView", "Error reading XML file");
            System.exit(1);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("MapPhotoView", "Error parsing XML file");
            System.exit(1);
        }

        //Set the onTouchListener
        setOnPhotoTapListener((view, x, y) -> {
            for (Polygon poly : polygons) {
                if (poly.isInsidePolygon(x, y, width, height)) {
                    Intent intent = new Intent(context, BuildingActivity.class);
                    intent.putExtra("building", poly.getName());
                    context.startActivity(intent);
                }
            }
        });
    }

    private static class Polygon {
        private final List<Vertex> vertices;
        private final String name;

        public Polygon(String verticesString, String name) {
            this.vertices = new ArrayList<>();
            this.name = name;

            String[] verticesArray = verticesString.split(",");
            for (int i = 0; i < verticesArray.length; i += 2) {
                vertices.add(new Vertex(Integer.parseInt(verticesArray[i]), Integer.parseInt(verticesArray[i + 1])));
            }
        }

        public String getName() {
            return name;
        }

        /**
         * @param x      the x coordinate of the point the user touched
         * @param y      the y coordinate of the point the user touched
         * @param width  the width of the image
         * @param height the height of the image
         * @return true if the point is inside the polygon, false otherwise
         */
        public boolean isInsidePolygon(float x, float y, float width, float height) {
            //Translate the point to the image's coordinate system
            float trueX = x * width;
            float trueY = y * height;
            boolean inside = false;
            int j = vertices.size() - 1;

            for (int i = 0; i < vertices.size(); j = i++) {
                if ((vertices.get(i).y < trueY && vertices.get(j).y >= trueY) || (vertices.get(j).y < trueY && vertices.get(i).y >= trueY)) {
                    if (vertices.get(i).x + (trueY - vertices.get(i).y) / (vertices.get(j).y - vertices.get(i).y) * (vertices.get(j).x - vertices.get(i).x) < trueX) {
                        inside = !inside;
                    }
                }
            }

            return inside;
        }

        private static class Vertex {
            private final int x;
            private final int y;

            public Vertex(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
    }
}
