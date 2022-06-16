package com.example.psi_univ.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.backend.DataBaseHelper;
import com.example.psi_univ.models.Event;
import com.example.psi_univ.models.Level;
import com.example.psi_univ.ui.fragments.RoomDialogFragment;
import com.richpath.RichPath;
import com.richpath.RichPathView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LevelMapAdapter extends RecyclerView.Adapter<LevelMapAdapter.LevelViewHolders> {

    private final List<Level> levels;
    private final String buildingName;
    private final FragmentManager fragmentManager;
    private final DataBaseHelper db;
    private final Calendar lookup;
    private final String lookupDate;
    private final String lookupTime;

    private final SimpleDateFormat sdf;


    public LevelMapAdapter(List<Level> levels, Context context, String date) {
        this.levels = levels;
        this.buildingName = levels.get(0).getBuildingName();
        this.fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        this.db = new DataBaseHelper(context);
        this.lookup = Calendar.getInstance();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String dateFormat = prefs.getString("key_date_format", "yyyy-MM-dd HH:mm");
        sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] tmp = new String[2];
        if (date != null) {
            try {
                this.lookup.setTime(databaseFormat.parse(date));
                tmp = date.split(" ");

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
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

        for (int i = 0; i < level.getRoomCount(); i++) {
            String roomName = level.getRoomAt(i).getRoomName();

            RichPath path = holder.richPathViewMap.findRichPathByName(roomName);
            if (path != null) {
                Event event = getEvent(roomName);

                path.setOnPathClickListener(new RichPath.OnPathClickListener() {
                    @Override
                    public void onClick() {
                        openRoomFragment(roomName, event);
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
     * open the room fragment with the given room name
     *
     * @param roomName name of the room
     * @param event    event to be added to the room
     */
    public void openRoomFragment(String roomName, Event event) {
        Bundle bundle = new Bundle();
        bundle.putString("roomName", roomName);
        bundle.putString("lookupDate", lookupDate);
        bundle.putString("lookupTime", lookupTime);

        if (event.isEmpty()) {

            bundle.putBoolean("available", true);
        } else {
            bundle.putBoolean("available", !event.isOverlapping(lookup));
            Event next = event.getNext();
            if (next != null) {
                String[] tmp = sdf.format(next.getStart().getTime()).split(" ");
                bundle.putString("nextDate", tmp[0]);
                bundle.putString("nextTime", tmp[1]);
            }
        }

        RoomDialogFragment roomDialogFragment = new RoomDialogFragment();
        roomDialogFragment.setArguments(bundle);
        roomDialogFragment.show(fragmentManager, "roomDialog_" + roomName);
    }

    /**
     * @param roomName name of the room
     * @return the event of the room at the lookup date and time
     */
    public Event getEvent(String roomName) {
        return db.getEventAt(buildingName, roomName, lookup);
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