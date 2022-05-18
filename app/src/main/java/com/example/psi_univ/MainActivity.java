package com.example.psi_univ;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private DrawerLayout drawer;
    private List<Building> buildingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        mToolBar = findViewById(R.id.ToolBar);
        //getSupportActionBar();
        setSupportActionBar(mToolBar);

        //Drawer
        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,mToolBar,R.string.drawer_open,R.string.drawer_close);




        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.addDrawerListener(toggle);
                toggle.syncState();
                return false;
            }
        });

        //Create Map
        PhotoView map = findViewById(R.id.map);
        map.setImageResource(R.drawable.map);
        map.setScaleType(ImageView.ScaleType.CENTER_CROP);

        map.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                Log.d("PhotoView", "X: " + x + " Y: " + y);
                for (Building building : buildingList) {
                    if(building.isInBuilding(x,y)){
                        Toast.makeText(MainActivity.this, building.getName(), Toast.LENGTH_SHORT).show();
                        Log.d("PhotoView", "In building " + building.getName());
                    }
                }
            }
        });

        buildingList = new ArrayList<>();
        JSONArray buildingJSON = getJSONObject("building_list.json");
        assert buildingJSON != null;
        for(int i = 0; i < buildingJSON.length(); i++){
            try {
                buildingList.add(new Building((JSONObject) buildingJSON.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "Error");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_app_bar,menu);
        MenuItem menuItem = menu.findItem(R.id.top_app_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Recherche");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    /**
     * Get JSONObject from file
     * @param fileName
     * @return
     */
    private JSONArray getJSONObject(String fileName){
        try(InputStream is = getAssets().open(fileName)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, StandardCharsets.UTF_8));
        }catch (FileNotFoundException e){
            e.printStackTrace();//TODO: Handle this
            Log.e("FileNotFoundException", "File Not Found : " + fileName);
        }catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IO Exception : " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", "JSON Exception : " + e.toString());
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}