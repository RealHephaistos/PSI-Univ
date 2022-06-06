package com.example.psi_univ.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.psi_univ.R;

public class RoomDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.dialog_room, container, false);

        Dialog dialog = getDialog();
        assert dialog != null;
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.dialog_anim);

        Bundle bundle = getArguments();
        assert bundle != null;

        TextView roomNameTextView = fragment.findViewById(R.id.roomNameTextView);
        roomNameTextView.setText(bundle.getString("roomName"));

        TextView dateTextView = fragment.findViewById(R.id.dateTextView);
        TextView timeTextView = fragment.findViewById(R.id.timeTextView);
        dateTextView.setText(bundle.getString("currentDate"));
        timeTextView.setText(bundle.getString("currentTime"));

        TextView availableTextView = fragment.findViewById(R.id.availableTextView);
        if (bundle.getBoolean("available")) {
            availableTextView.setText(R.string.available);
        } else {
            availableTextView.setText(R.string.unavailable);
        }

        String nextDate = bundle.getString("nextDate");
        String nextTime = bundle.getString("nextTime");
        TextView dateNextClassTextView = fragment.findViewById(R.id.dateNextClassTextView);
        TextView timeNextClassTextView = fragment.findViewById(R.id.timeNextClassTextView);
        if (nextDate != null && nextTime != null) {
            dateNextClassTextView.setText(nextDate);
            timeNextClassTextView.setText(nextTime);
        } else {
            dateNextClassTextView.setText(R.string.no_next_class);
            timeNextClassTextView.setText("");
        }

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}