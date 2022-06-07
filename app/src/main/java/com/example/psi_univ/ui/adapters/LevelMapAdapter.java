package com.example.psi_univ.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LevelMapAdapter extends RecyclerView.Adapter<LevelMapAdapter.LevelViewHolders> {

    private final List<Level> levels;
    private final String buildingName;
    private final FragmentManager fragmentManager;
    private final DataBaseHelper db;
    private final Calendar lookup;
    private final String lookupDate;
    private final String lookupTime;


    public LevelMapAdapter(List<Level> levels, Context context, String date) {
        this.levels = levels;
        this.buildingName = levels.get(0).getBuildingName();
        this.fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        this.db = new DataBaseHelper(context);
        this.lookup = Calendar.getInstance();
        String[] tmp = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        if(date != null) {
            try {
                this.lookup.setTime(Objects.requireNonNull(sdf.parse(date)));
                tmp = date.split(" ");
                
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            tmp = sdf.format(Calendar.getInstance().getTime()).split(" ");
        }
        
        this.lookupDate = tmp[0];
        this.lookupTime = tmp[1];
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
                        openRoomFragment(room.getRoomName());
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
     */
    public void openRoomFragment(String roomName) {
        Bundle bundle = new Bundle();
        bundle.putString("roomName", roomName);
        bundle.putString("lookupDate", lookupDate);
        bundle.putString("lookupTime", lookupTime);

        Event event = db.getEventAt(buildingName, roomName, lookupDate + " " + lookupTime);
        if (event == null) {

            bundle.putBoolean("available", true);
        } else {
            bundle.putBoolean("available", !event.isOverlapping(lookup));
            String test = event.getStart();
            Event next =event.getNext();
            if (next != null) {
                String[] tmp = next.getStart().split(" ");
                bundle.putString("nextDate", tmp[0]);
                bundle.putString("nextTime", tmp[1]);
            }
        }

        RoomDialogFragment roomDialogFragment = new RoomDialogFragment();
        roomDialogFragment.setArguments(bundle);
        roomDialogFragment.show(fragmentManager, "roomDialog_" + roomName);
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