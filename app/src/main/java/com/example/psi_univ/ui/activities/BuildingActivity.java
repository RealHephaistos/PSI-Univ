package com.example.psi_univ.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.psi_univ.R;

public class BuildingActivity extends AppCompatActivity {
    private int screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent intent = getIntent();
        String buildingName = intent.getStringExtra("building");
        Log.d("BuildingActivity", buildingName);

        LinearLayout linearLayout = findViewById(R.id.buildingLayout);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight);

        int levels = 4; //TODO: get levels from database

        for (int i = 0; i < levels; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.l1_level);
            if(i % 2 == 0) {
                imageView.setBackgroundColor(Color.GREEN);
            }else{
                imageView.setBackgroundColor(Color.parseColor("#FF0000"));
            }

            imageView.setLayoutParams(params);
            Log.d("BuildingActivity", "height: " + screenHeight);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);
            linearLayout.addView(imageView);
        }

        ScrollView scrollView = findViewById(R.id.buildingScrollView);
    }
}
