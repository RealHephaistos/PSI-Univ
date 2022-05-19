package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.adapters.LevelScrollView;

public class BuildingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent intent = getIntent();
        String buildingName = intent.getStringExtra("building");
        Log.d("BuildingActivity", buildingName);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        LevelScrollView levelScrollView = new LevelScrollView(this, size.y);
        setContentView(levelScrollView);

        int levels = 4; //TODO: get levels from database

        for (int i = 0; i < levels; i++) {
            levelScrollView.addLevel(i);
        }


    }
}
