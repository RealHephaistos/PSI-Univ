package com.example.psi_univ.ui.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.psi_univ.R;

import java.util.Locale;

public class AdvancedSearchFragment extends  Fragment implements View.OnClickListener {
    TextView timer;
    int timerHour,timerMinute;

    //Fragment

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_advanced_search,container,false);
        timer = myView.findViewById(R.id.time_select);
        timer.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_select :
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    timerHour = hourOfDay;
                    timerMinute = minute;
                    timer.setText(String.format(Locale.getDefault(), "%02d:%02d", timerHour, timerMinute));
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, timerHour, timerMinute, true);
            timePickerDialog.setTitle(R.string.drawer_close);
            timePickerDialog.show();
        }
    }
}