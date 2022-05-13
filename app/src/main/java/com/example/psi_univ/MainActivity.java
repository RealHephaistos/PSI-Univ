package com.example.psi_univ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar;

    private GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        mToolBar = findViewById(R.id.ToolBar);
        getSupportActionBar();
        setSupportActionBar(mToolBar);

        //Navigation Icon when clicked
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Menu", Toast.LENGTH_SHORT).show();
            }
        });


        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
                return false;
                   }
        });

        //Map view
        VerticalScrollMap verticalScroll = findViewById(R.id.vericalScroll);
        HorizontalScrollMap horizontalScroll = findViewById(R.id.horizontalScroll);
        CampusMapView campusMap = findViewById(R.id.campusMap);

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                horizontalScroll.smoothScrollBy((int) distanceX, 0);
                verticalScroll.smoothScrollBy(0, (int) distanceY);
                return true;
            }
        });

        horizontalScroll.setOnTouchListener((v, event) -> gd.onTouchEvent(event));
        verticalScroll.setOnTouchListener((v, event) -> gd.onTouchEvent(event));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_app_bar,menu);
        return true;
    }


}