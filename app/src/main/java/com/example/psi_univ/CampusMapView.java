package com.example.psi_univ;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class CampusMapView extends AppCompatImageView {

    private ScaleGestureDetector sgd;

    public CampusMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setContentDescription("Campus Map");
        setImageResource(R.drawable.univ_rennes_1_plan_beaulieu);
        setScaleType(ScaleType.CENTER_CROP);
        sgd = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("Scale", "Scale detected");
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
