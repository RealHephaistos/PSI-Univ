package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.psi_univ.R;
import com.example.psi_univ.backend.DataBaseHelper;
import com.example.psi_univ.models.Room;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayAdapter<Room> arrayAdapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("key_language", "").compareTo("FRE") == 0) {
            //Toast.makeText(this, sharedPreferences.getString("key_language", " "), Toast.LENGTH_SHORT).show();
            setLocal("fr");
        } else {
            setLocal("eng");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageViewHome);
        drawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.homepage);


        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

        SearchView searchView = findViewById(R.id.searchView);
        List<Room> rooms = dataBaseHelper.getAllRooms();
        ListView room = findViewById(R.id.listRoom);
        Intent resultIntent = new Intent(this, BuildingActivity.class);
        room.setOnItemClickListener((parent, view, position, id) -> {
            Object selectedRoom = parent.getAdapter().getItem(position);
            position = rooms.indexOf(selectedRoom);
            //Toast.makeText(MainActivity.this, selectedRoom.toString(), Toast.LENGTH_SHORT).show();
            resultIntent.putExtra("building", rooms.get(position).getBuildingName());
            resultIntent.putExtra("level", rooms.get(position).getLevelName());
            resultIntent.putExtra("room", rooms.get(position).getRoomName());
            startActivity(resultIntent);
        });

        room.setEmptyView(findViewById(R.id.empty));


        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rooms);
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
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            View listLayout = findViewById(R.id.list_layout);
            if (hasFocus) listLayout.setVisibility(View.VISIBLE);
            else listLayout.setVisibility(View.GONE);
        });

        imageView.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.advancedSearch) {
            startActivity(new Intent(this, AdvancedSearchActivity.class));
        }
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    private void setLocal(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);

        Configuration configuration = this.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        this.getResources().updateConfiguration(configuration, this.getResources().getDisplayMetrics());

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }
}