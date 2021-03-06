package com.example.psi_univ.ui.adapters;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LevelMapRecycler extends RecyclerView {
    public LevelMapRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);
    }

    /**
     * Scroll to the level with the given name, open the corresponding room if roomName is not null.
     *
     * @param levelName the level name to be displayed
     * @param roomName  the room name to be displayed can be null
     * @return the position of the level name in the recycler view
     */
    public int scrollToLevel(String levelName, String roomName) {
        LevelMapAdapter adapter = (LevelMapAdapter) getAdapter();
        assert adapter != null;
        int position = adapter.getPosition(levelName);
        scrollToPosition(position);
        if (roomName != null) {
            adapter.openRoomFragment(roomName, adapter.getEvent(roomName));
        }

        return position;
    }
}
