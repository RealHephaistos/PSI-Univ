package com.example.psi_univ.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.fragments.SettingsFragment;


public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.ToolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (findViewById(R.id.settings_container) != null) {
            if (savedInstanceState != null){
                return;
            }

            getFragmentManager().beginTransaction().add(R.id.settings_container, new SettingsFragment()).commit();
        }
    }
}