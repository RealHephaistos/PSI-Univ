package com.example.psi_univ.ui.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.backend.DataBaseHelper;
import com.example.psi_univ.models.Event;
import com.example.psi_univ.models.Level;
import com.example.psi_univ.models.Room;
import com.example.psi_univ.ui.fragments.RoomDialogFragment;
import com.richpath.RichPath;
import com.richpath.RichPathView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class LevelMapAdapter extends RecyclerView.Adapter<LevelMapAdapter.LevelViewHolders> {

    private final List<Level> levels;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    private DataBaseHelper db;

    public LevelMapAdapter(List<Level> levels) {
        this.levels = levels;
    }

    @NonNull
    @Override
    public LevelMapAdapter.LevelViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_plan, parent, false);
        return new LevelViewHolders(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolders holder, int position) {
        Level level = levels.get(position);
        holder.richPathViewMap.setVectorDrawable(level.getLevelMap());

        db = new DataBaseHelper(holder.itemView.getContext());
        FragmentManager fm = ((AppCompatActivity) holder.richPathViewMap.getContext()).getSupportFragmentManager();

        Calendar currentTime = Calendar.getInstance();//TODO: change to lookup date
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);

        for (int i = 0; i < level.getRoomCount(); i++) {
            Room room = level.getRoomAt(i);

            RichPath path = holder.richPathViewMap.findRichPathByName(room.getRoomName());
            if (path != null) {
                path.setOnPathClickListener(new RichPath.OnPathClickListener() {
                    @Override
                    public void onClick() {
                        openRoomFragment(room.getRoomName(), currentTime, endTime, fm);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * @param levelName name of the level
     * @return the position of the corresponding level in the list -1 if not found
     */
    public int getPosition(String levelName) {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getLevelName().equals(levelName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Opens a dialog fragment with the room's events
     *
     * @param roomName    name of the room
     * @param currentTime current time
     * @param endTime     end time
     * @param fm          fragment manager
     */
    public void openRoomFragment(String roomName, Calendar currentTime, Calendar endTime, FragmentManager fm) {
        Bundle bundle = new Bundle();
        bundle.putString("roomName", roomName);
        bundle.putString("currentDate", date.format(currentTime.getTime()));
        bundle.putString("currentTime", time.format(currentTime.getTime()));

        //Event event = db.getEventsAt(level.getBuildingName(), room.getRoomName(), currentTime);
        //Event event = new Event(currentTime, endTime, "test");
        Event event = null;
        if (event == null) {
            bundle.putBoolean("available", true);
        } else {
            bundle.putBoolean("available", event.isOverlapping(currentTime));
            if (event.getNext() != null) {
                Calendar next = event.getNext().getStart();
                bundle.putString("nextDate", date.format(next.getTime()));
                bundle.putString("nextTime", time.format(next.getTime()));
            }
        }

        RoomDialogFragment roomDialogFragment = new RoomDialogFragment();
        roomDialogFragment.setArguments(bundle);
        roomDialogFragment.show(fm, "roomDialog_" + roomName);
    }

    public static class LevelViewHolders extends RecyclerView.ViewHolder {
        final RichPathView richPathViewMap;
        final FragmentContainerView fragmentContainer;

        public LevelViewHolders(@NonNull View itemView) {
            super(itemView);
            richPathViewMap = itemView.findViewById(R.id.map);
            fragmentContainer = itemView.findViewById(R.id.roomFragmentContainerView);
        }
    }
}