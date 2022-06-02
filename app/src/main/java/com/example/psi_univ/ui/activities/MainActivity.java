package com.example.psi_univ.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.psi_univ.R;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageViewHome);
        drawerLayout = findViewById(R.id.drawerLayout);


        SearchView searchView = findViewById(R.id.searchView);
        ListView room = findViewById(R.id.listRoom);
        room.setEmptyView(findViewById(R.id.empty));
        List<String> rooms = new ArrayList<String>();

        rooms.add("B12D i-50");rooms.add("B12D i-51");rooms.add("B12D i-52");rooms.add("B12D i-53");rooms.add("B12D i-54");rooms.add("B12D i-55");
        rooms.add("B12D Salle Guernesey");rooms.add("B12D Salle Jersey");rooms.add("B12D Amphi P");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,rooms);
        room.setAdapter(arrayAdapter);

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

}