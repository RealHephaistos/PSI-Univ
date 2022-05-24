package com.example.psi_univ.ui.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.psi_univ.R;

import java.util.Locale;

public class AdvancedSearchFragment extends  Fragment implements View.OnClickListener {
    TextView timer;
    SwitchCompat availableRoom;
    SwitchCompat unavailableRoom;
    int timerHour,timerMinute;

    //Fragment

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_advanced_search,container,false);

        timer = myView.findViewById(R.id.time_select);
        availableRoom = myView.findViewById(R.id.switch_available_room);
        unavailableRoom = myView.findViewById(R.id.switch_unavailable_room);
        availableRoom.setOnClickListener(this);
        unavailableRoom.setOnClickListener(this);
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
            break;
            case R.id.switch_available_room:
                if (availableRoom.isChecked()){
                    Toast.makeText(getActivity(), R.string.advanced_search_available_rooms_toast_on, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), R.string.advanced_search_available_rooms_toast_of, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_unavailable_room:
                if (unavailableRoom.isChecked()){
                    Toast.makeText(getActivity(), R.string.advanced_search_unavailable_rooms_toast_on, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), R.string.advanced_search_unavailable_rooms_toast_of, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}