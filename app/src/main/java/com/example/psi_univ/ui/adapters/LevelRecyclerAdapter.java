package com.example.psi_univ.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.models.Level;
import com.richpath.RichPath;
import com.richpath.RichPathView;

import java.util.ArrayList;
import java.util.List;

public class LevelRecyclerAdapter extends RecyclerView.Adapter<LevelRecyclerAdapter.LevelViewHolders> {

    private final List<Level> levels;

    public LevelRecyclerAdapter(List<Level> levels) {
        this.levels = levels;
    }

    @NonNull
    @Override
    public LevelRecyclerAdapter.LevelViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_levelplan, parent, false);
        return new LevelViewHolders(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolders holder, int position) {
        //holder.levelMap.setVectorDrawable(levels.get(position).getLevelMap());
        List<RichPath> richPathList = new ArrayList<>();

        holder.levelMap.findRichPathByIndex(0).setFillColor(Color.parseColor("#000000"));
        holder.levelMap.findRichPathByIndex(1).setFillColor(Color.parseColor("#000000"));
        holder.levelMap.findRichPathByIndex(2).setFillColor(Color.parseColor("#000000"));
        holder.levelMap.findRichPathByIndex(3).setFillColor(Color.parseColor("#000000"));
        holder.levelMap.findRichPathByIndex(4).setFillColor(Color.parseColor("#000000"));

        if(position % 2 == 0){
            holder.levelMap.setBackgroundColor(Color.GREEN);
        }
        else{
            holder.levelMap.setBackgroundColor(Color.RED);
        }

        holder.levelName.setText(levels.get(position).getLevelName());
        holder.buildingName.setText(levels.get(position).getBuildingName());

        //TODO make it pretty
        //TODO add level logo
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
        CardView levelCard;
        TextView levelName;
        TextView buildingName;
        RichPathView levelMap;
        public LevelViewHolders(@NonNull View itemView) {
            super(itemView);
            levelCard = itemView.findViewById(R.id.cv);
            levelMap = itemView.findViewById(R.id.map);
            levelName = itemView.findViewById(R.id.levelName);
            buildingName = itemView.findViewById(R.id.buildingName);
        }
    }
}
