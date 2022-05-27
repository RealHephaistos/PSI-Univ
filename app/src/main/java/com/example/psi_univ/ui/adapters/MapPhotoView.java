package com.example.psi_univ.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.activities.BuildingActivity;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapPhotoView extends PhotoView {
    private final List<Polygon> polygons;
    private final float width;
    private final float height;

    public MapPhotoView(Context context, AttributeSet attr) {
        //Create the PhotoView
        super(context, attr);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageResource(R.drawable.ic_map);
        width = getWidth();
        height = getHeight();

        //Get the polygons from the XML file
        polygons = new ArrayList<>();
        XmlResourceParser xml = context.getResources().getXml(R.xml.maps);
        try {
            xml.next();
            int eventType = xml.getEventType();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    if(xml.getName().equals("area")){
                        polygons.add(new Polygon(xml.getAttributeValue(null,"coords"), xml.getAttributeValue(null,"title")));
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
            for(Polygon poly : polygons){
                if(poly.isInsidePolygon(x,y,width,height)){
                    Intent intent = new Intent(context, BuildingActivity.class);
                    intent.putExtra("building", poly.getName());
                    context.startActivity(intent);
                }
            }
        });
    }

    private static class Polygon{
        private final List<Vertex> vertices;
        private final String name;

        public Polygon(String verticesString, String name) {
            this.vertices = new ArrayList<>();
            this.name = name;

           String[] verticesArray = verticesString.split(",");
            for(int i = 0; i < verticesArray.length; i+=2){
                vertices.add(new Vertex(Integer.parseInt(verticesArray[i]), Integer.parseInt(verticesArray[i + 1])));
            }
        }

        public String getName() {
            return name;
        }

        public boolean isInsidePolygon(float x, float y, float width, float height){
            float trueX = x * width;
            float trueY = y * height;
            float slope;
            Vertex lastVertex = vertices.get(vertices.size() - 1);

            for (Vertex vertex: vertices) {
                if(vertex.x == trueX && vertex.y == trueY){
                    return true;
                }

                if((vertex.y > trueY) != (lastVertex.y > trueY)){
                    slope = (x - vertex.x) * (lastVertex.y - vertex.y) - (y - vertex.y) * (lastVertex.x - vertex.x);
                    if(slope == 0){
                        return true;
                    }
                    if(slope > 0){
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static class Vertex{
        private final int x;
        private final int y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
