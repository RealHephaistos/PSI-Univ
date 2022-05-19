package com.example.psi_univ.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.psi_univ.R;

@SuppressLint("ViewConstructor")
public class LevelScrollView extends ScrollView {
    private final LinearLayout layout;
    private final RelativeLayout.LayoutParams params;
    private GestureDetector gestureDetector;

    public LevelScrollView(Context context, int screenHeight) {
        super(context);

        setVerticalScrollBarEnabled(false);
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        addView(layout);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return super.onDown(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("LevelScrollView", "onScroll");
                scrollTo(0, screenHeight);
                return true;//super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                performClick();
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
        //TODO deactivate scrolling?
    }

    public void addLevel(int level) {
        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.l1_level);
        if(level % 2 == 0) {
            image.setBackgroundColor(Color.GREEN);
        }else{
            image.setBackgroundColor(Color.parseColor("#FF0000"));
        }

        image.setLayoutParams(params);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setAdjustViewBounds(true);
        layout.addView(image);
    }
}

