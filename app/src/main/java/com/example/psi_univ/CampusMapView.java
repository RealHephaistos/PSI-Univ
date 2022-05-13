package com.example.psi_univ;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CampusMapView extends androidx.appcompat.widget.AppCompatImageView {

    public CampusMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.univ_rennes_1_plan_beaulieu);
        setScaleType(ScaleType.CENTER_CROP);

        setOnDragListener(new OnDragListener() {
            @Override public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            setScrollX((int)event.getX());
                            Log.println(Log.INFO, "Drag", "Drag started");
                            break;

                }
                return true;
            }
        });
    }
}
