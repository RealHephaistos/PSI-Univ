package com.example.psi_univ.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.fragments.RoomFragment;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_level_plan, parent, false);
        return new LevelViewHolders(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolders holder, int position) {
        Level level = levels.get(position);
        holder.richPathViewMap.setVectorDrawable(level.getLevelMap());
        List<RichPath> richPathList = new ArrayList<>();

        for(int i = 0; i<level.getRoomCount(); i++){
            RichPath richPath = holder.richPathViewMap.findRichPathByName(level.getRoom(i));
            //TODO: error map not found
            assert richPath != null;

            richPath.setOnPathClickListener(() -> {
                Fragment fragment = new RoomFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) holder.richPathViewMap.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.roomFragmentContainerView, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            /*richPath.setFillAlpha(1);
            richPath.setFillColor(Color.parseColor("#FF0000"));*/

            //richPath.setScaleX(0.5f);
            //richPath.setScaleY(0.5f);
            //TODO: zoom maybe?

            richPathList.add(richPath);
        }

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
        final RichPathView richPathViewMap;
        final FragmentContainerView fragmentContainer;
        public LevelViewHolders(@NonNull View itemView) {
            super(itemView);
            richPathViewMap = itemView.findViewById(R.id.map);
            fragmentContainer = itemView.findViewById(R.id.roomFragmentContainerView);
        }
    }
}
