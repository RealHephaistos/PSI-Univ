package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.DataBaseHelper;
import com.example.psi_univ.R;
import com.example.psi_univ.ui.adapters.LevelMapRecycler;
import com.example.psi_univ.ui.adapters.LevelMapRecyclerAdapter;
import com.example.psi_univ.ui.adapters.LevelNameAdapter;
import com.example.psi_univ.ui.adapters.LevelNameRecycler;
import com.example.psi_univ.ui.models.Building;
import com.example.psi_univ.ui.models.Level;

import java.util.List;

public class BuildingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseHelper db = new DataBaseHelper(BuildingActivity.this);
        setContentView(R.layout.activity_building);

        //Get and set the building's name
        Intent intent = getIntent();
        String buildingName = intent.getStringExtra("building");
        TextView buildingNameTextView = findViewById(R.id.buildingName);
        buildingNameTextView.setText(buildingName);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        Building building = dataBaseHelper.getBuilding(buildingName);
        List<Level> levels = building.getLevelList();

        //Set the map recycler view
        LevelMapRecycler mapRecycler = findViewById(R.id.mapRecycler);
        mapRecycler.setAdapter(new LevelMapRecyclerAdapter(levels));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mapRecycler);

        //Set the name recycler view
        LevelNameRecycler nameRecycler = findViewById(R.id.nameRecycler);
        nameRecycler.setAdapter(new LevelNameAdapter(levels));
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(nameRecycler);

        //Make the name recycler view scroll to the selected level
        mapRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    assert nameRecycler.getLayoutManager() != null;
                    nameRecycler.getLayoutManager().smoothScrollToPosition(nameRecycler, null, mapRecycler.getChildAdapterPosition(mapRecycler.getChildAt(0)));
                }
            }
        });
    }
}