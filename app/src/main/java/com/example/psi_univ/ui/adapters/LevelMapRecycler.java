package com.example.psi_univ.ui.adapters;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class LevelMapRecycler extends RecyclerView {
    public LevelMapRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);
    }

    public int scrollToLevel(String levelName, String roomName) {
        Calendar currentTime = Calendar.getInstance();//TODO: change to lookup date
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        LevelMapAdapter adapter = (LevelMapAdapter) getAdapter();
        assert adapter != null;
        int position = adapter.getPosition(levelName);
        scrollToPosition(position);
        if(roomName != null){
            adapter.openRoomFragment(roomName, currentTime, endTime, ((AppCompatActivity) getContext()).getSupportFragmentManager());
        }

        return position;
    }
}
