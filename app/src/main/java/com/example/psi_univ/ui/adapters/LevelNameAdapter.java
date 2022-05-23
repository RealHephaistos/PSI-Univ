package com.example.psi_univ.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.models.Level;

import java.util.List;

public class LevelNameAdapter extends RecyclerView.Adapter<LevelNameAdapter.LevelNameViewHolders> {

    private final List<Level> levels;

    public LevelNameAdapter(List<Level> levels) {
        this.levels = levels;
    }

    @NonNull
    @Override
    public LevelNameViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        Log.d("LevelNameAdapter", "onCreateViewHolder: "+ v.getHeight());
        return new LevelNameViewHolders(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelNameViewHolders holder, int position) {
        Level level = levels.get(position);
        holder.levelNameTextView.setText(level.getLevelName());
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    public static class LevelNameViewHolders extends RecyclerView.ViewHolder{
        final TextView levelNameTextView;

        public LevelNameViewHolders(@NonNull View itemView) {
            super(itemView);
            levelNameTextView = itemView.findViewById(R.id.levelName);
        }
    }
}
