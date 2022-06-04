package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.psi_univ.R;
import com.example.psi_univ.models.Event;
import com.example.psi_univ.models.Room;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    ArrayAdapter<Room> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageViewHome);
        drawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.homepage);


        SearchView searchView = findViewById(R.id.searchView);
        ListView room = findViewById(R.id.listRoom);
        Intent resultIntent = new Intent(this,BuildingActivity.class);
        room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultIntent.putExtra("building", parent.getItemAtPosition(position).toString().split(" ")[0]);
                startActivity(resultIntent);
            }
        });

        room.setEmptyView(findViewById(R.id.empty));

        List<Room> rooms = new ArrayList<Room>();
        rooms.add(new Room("i- 50", "B12D","0",new ArrayList<Event>()));

        arrayAdapter = new ArrayAdapter<Room>(this, android.R.layout.simple_list_item_1,rooms);
        room.setAdapter(arrayAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View listLayout = findViewById(R.id.list_layout);
                if(hasFocus) listLayout.setVisibility(View.VISIBLE);
                else listLayout.setVisibility(View.GONE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.advancedSearch:
                startActivity(new Intent(this,AdvancedSearchActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}