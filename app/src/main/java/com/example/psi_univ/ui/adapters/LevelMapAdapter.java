package com.example.psi_univ.ui.adapters;

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
import com.example.psi_univ.ui.fragments.RoomDialogFramework;
import com.example.psi_univ.models.Level;
import com.example.psi_univ.models.Room;
import com.richpath.RichPath;
import com.richpath.RichPathView;

import java.util.Calendar;
import java.util.List;

public class LevelMapAdapter extends RecyclerView.Adapter<LevelMapAdapter.LevelViewHolders> {

    private final List<Level> levels;

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

        for (int i = 0; i < level.getRoomCount(); i++) {
            Room room = level.getRoomAt(i);
            RichPath path = holder.richPathViewMap.findRichPathByName(room.getRoomName());
            //assert path != null; //TODO: handle this case
            if(path != null) {
                path.setOnPathClickListener(new RichPath.OnPathClickListener() {
                    @Override
                    public void onClick() {
                        Bundle bundle = new Bundle();
                        bundle.putString("roomName", room.getRoomName());
                        Calendar currentTime = Calendar.getInstance();//TODO: change to lookup date
                        bundle.putInt("day", currentTime.get(Calendar.DATE));
                        bundle.putInt("month", currentTime.get(Calendar.MONTH));
                        bundle.putInt("year", currentTime.get(Calendar.YEAR));
                        bundle.putInt("hour", currentTime.get(Calendar.HOUR_OF_DAY));
                        bundle.putInt("minute", currentTime.get(Calendar.MINUTE));
                        bundle.putBoolean("available", room.isAvailableAt(Calendar.getInstance()));//TODO: only do that once
                        Calendar next = room.getNextEvent();
                        bundle.putInt("dayNext", next.get(Calendar.DATE));
                        bundle.putInt("monthNext", next.get(Calendar.MONTH));
                        bundle.putInt("yearNext", next.get(Calendar.YEAR));
                        bundle.putInt("hourNext", next.get(Calendar.HOUR_OF_DAY));
                        bundle.putInt("minuteNext", next.get(Calendar.MINUTE));

                        RoomDialogFramework roomDialogFramework = new RoomDialogFramework();

                        roomDialogFramework.setArguments(bundle);
                        FragmentManager fragmentManager = ((AppCompatActivity) holder.richPathViewMap.getContext()).getSupportFragmentManager();
                        roomDialogFramework.show(fragmentManager, "roomDialog_" + room.getRoomName());
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