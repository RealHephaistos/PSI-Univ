package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.util.Objects;

public class BuildingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent intent = getIntent();
        String buildingName = intent.getStringExtra("building");

        TextView buildingNameTextView = findViewById(R.id.buildingName);
        TextView levelNameTextView = findViewById(R.id.levelName);
        buildingNameTextView.setText(buildingName);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int levelsNbr = 4; //TODO: get levels from database
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i < levelsNbr; i++) {
            levels.add(new Level("Level " + (i + 1), buildingName));
        }

        levelNameTextView.setText(levels.get(0).getLevelName());

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new LevelRecyclerAdapter(levels));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);//TODO: Possibly not the best way to do this
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                    levelNameTextView.setText(levels.get(position).getLevelName());
                }
            }
        });

    }
}
