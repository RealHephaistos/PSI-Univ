package com.example.psi_univ;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalScrollMap extends HorizontalScrollView {
    public HorizontalScrollMap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev); //TODO check if this is needed
        return false;
    }
}
