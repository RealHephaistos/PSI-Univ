package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.adapters.LevelRecyclerAdapter;
import com.example.psi_univ.ui.models.Level;

import java.util.ArrayList;
import java.util.List;

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



        int levelsNbr = 4; //TODO: get levels from database
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i < levelsNbr; i++) {
            levels.add(new Level("Level " + (i + 1), buildingName));
        }

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new LevelRecyclerAdapter(levels));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler);
    }
}
