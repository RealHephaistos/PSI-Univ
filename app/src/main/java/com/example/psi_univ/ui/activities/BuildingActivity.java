package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.adapters.LevelNameAdapter;
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
        buildingNameTextView.setText(buildingName);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int levelsNbr = 4; //TODO: get levels from database
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i < levelsNbr; i++) {
            levels.add(new Level("Level " + (i + 1), buildingName));
        }
        levels.add(new Level("Sous sol", buildingName));

        RecyclerView mapRecycler = findViewById(R.id.mapRecycler);
        mapRecycler.setHasFixedSize(true);
        mapRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapRecycler.setAdapter(new LevelRecyclerAdapter(levels));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mapRecycler);

        RecyclerView levelNameRecycler = findViewById(R.id.levelRecycler);
        levelNameRecycler.setLayoutManager(new LinearLayoutManager(this));
        levelNameRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        levelNameRecycler.setAdapter(new LevelNameAdapter(levels));
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(levelNameRecycler);

        mapRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    Objects.requireNonNull(levelNameRecycler.getLayoutManager()).smoothScrollToPosition(levelNameRecycler, null, mapRecycler.getChildAdapterPosition(mapRecycler.getChildAt(0)));
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
