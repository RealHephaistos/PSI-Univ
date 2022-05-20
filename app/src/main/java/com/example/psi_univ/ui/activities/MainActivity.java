package com.example.psi_univ.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.psi_univ.ui.fragments.AdvancedSearchFragment;
import com.example.psi_univ.ui.fragments.SettingsFragment;
import com.example.psi_univ.ui.models.Building;
import com.example.psi_univ.R;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Fragment
        TextView timer;
        timer = findViewById(R.id.time_select);

        /*
        timer.setOnClickListener(new View.OnClickListener() {
            int Minute,Hour;
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Hour = hourOfDay;
                                Minute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,Hour,Minute);
                                timer.setText(DateFormat.format("hh:mm:aa", calendar));
                            }
                        },12,0,true);
                timePickerDialog.updateTime(Hour,Minute);
                timePickerDialog.show();
            }
        });

         */


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
                        Log.d("PhotoView", "In building " + building.getName());
                        Intent intent = new Intent(MainActivity.this, BuildingActivity.class);
                        intent.putExtra("building", building.getName());
                        MainActivity.this.startActivity(intent);
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
            Log.e("IOException", "IO Exception : " + e);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", "JSON Exception : " + e);
        }
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawer_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                break;
            case R.id.homepage:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.advanced_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AdvancedSearchFragment()).commit();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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