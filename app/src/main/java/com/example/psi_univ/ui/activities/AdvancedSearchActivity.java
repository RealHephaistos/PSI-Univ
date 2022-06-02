package com.example.psi_univ.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.fragments.AdvancedSearchFragment;


public class AdvancedSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (findViewById(R.id.advancedSearchContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.advancedSearchContainer, new AdvancedSearchFragment()).commit();
        }
    }
}