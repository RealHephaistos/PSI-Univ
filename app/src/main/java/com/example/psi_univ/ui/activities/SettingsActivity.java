package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;


public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView imageView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("key_language", "").compareTo("FRE") == 0) {
            //Toast.makeText(this, sharedPreferences.getString("key_language", " "), Toast.LENGTH_SHORT).show();
            setLocal("fr");
        }
        else {
            setLocal("eng");
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        //Drawer
        imageView = findViewById(R.id.imageViewHome);
        drawerLayout = findViewById(R.id.settingsDrawer);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.settings);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Toolbar
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (findViewById(R.id.settings_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, new SettingsFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homepage) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (item.getItemId() == R.id.advancedSearch) {
            startActivity(new Intent(this, AdvancedSearchActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setLocal(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);

        Configuration configuration = this.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        this.getResources().updateConfiguration(configuration,this.getResources().getDisplayMetrics());

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }



}