package com.example.psi_univ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private List<Building> buildingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        mToolBar = findViewById(R.id.ToolBar);
        getSupportActionBar();
        setSupportActionBar(mToolBar);

        //Navigation Icon when clicked
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                }
            }
        });


        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //Create Map

        PhotoView map = findViewById(R.id.map);
        map.setImageResource(R.drawable.ic_map_test);
        map.setScaleType(ImageView.ScaleType.CENTER_CROP);

        map.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                Log.d("PhotoView", "X: " + x + " Y: " + y);
            }
        });

        buildingList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try(FileReader reader = new FileReader("./resources/json/building_list.json")){
            JSONArray buildingJSON = (JSONArray) parser.parse(reader);
            Log.d("JSON", buildingJSON.toString());
            for(int i = 0; i < buildingJSON.length(); i++){
                buildingList.add(new Building(buildingJSON.getJSONObject(i)));
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
            Log.e("FileNotFoundException", "File Not Found");
        }catch (IOException e){
            e.printStackTrace();
            Log.e("IOException", "IO Exception");
        }catch (ParseException e){
            e.printStackTrace();
            Log.e("ParseException", "Parse Exception");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_app_bar,menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        };


        menu.findItem(R.id.top_app_search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.top_app_search).getActionView();
        searchView.setQueryHint("Recherche");
        return true;
    }


}