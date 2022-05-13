package com.example.psi_univ;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VerticalScrollMap extends ScrollView {

    public VerticalScrollMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSmoothScrollingEnabled(true);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        performClick();
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
