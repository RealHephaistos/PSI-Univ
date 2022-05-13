package com.example.psi_univ;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalScrollMap extends HorizontalScrollView {

    public HorizontalScrollMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSmoothScrollingEnabled(true);
        setHorizontalScrollBarEnabled(false);
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
