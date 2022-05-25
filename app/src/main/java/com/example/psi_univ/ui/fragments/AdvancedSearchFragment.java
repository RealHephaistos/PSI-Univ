package com.example.psi_univ.ui.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdvancedSearchFragment extends Fragment implements View.OnClickListener {
    TextView timer;
    SwitchCompat availableRoom;
    SwitchCompat unavailableRoom;
    Button dateButton;
    DatePickerDialog datePickerDialog;
    int timerHour, timerMinute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Fragment View
        View myView = inflater.inflate(R.layout.fragment_advanced_search, container, false);

        //Variables initialisations
        timer = myView.findViewById(R.id.time_select);
        availableRoom = myView.findViewById(R.id.switch_available_room);
        unavailableRoom = myView.findViewById(R.id.switch_unavailable_room);
        dateButton = myView.findViewById(R.id.date_button);

        //Calendar for the Date Picker
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        //Current date as Hint in the Button
        dateButton.setHint(
                new StringBuilder().append(mDay).append('/').append(mMonth + 1).append('/').append(mYear));

        //On click listener in the fragment view
        availableRoom.setOnClickListener(this);
        unavailableRoom.setOnClickListener(this);
        timer.setOnClickListener(this);
        dateButton.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_select://Timer Picker
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
            case R.id.switch_available_room://Message shown with the available rooms switch button
                if (availableRoom.isChecked()) {
                    Toast.makeText(getActivity(), R.string.advanced_search_available_rooms_toast_on, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.advanced_search_available_rooms_toast_of, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_unavailable_room://Message shown with the available rooms switch button
                if (unavailableRoom.isChecked()) {
                    Toast.makeText(getActivity(), R.string.advanced_search_unavailable_rooms_toast_on, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.advanced_search_unavailable_rooms_toast_of, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.date_button://Date Picker
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        String date = makeDateString(dayOfMonth,month,year);
                        dateButton.setHint(date);
                    }
                };
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                int style = AlertDialog.THEME_HOLO_LIGHT;

                datePickerDialog = new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);

                datePickerDialog.show();
                break;
        }
    }

    /**
     * Date to String
     * @param dayOfMonth
     * @param month
     * @param year
     *
     * @return A date in a string format
     */
    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    /**
     *
     * @param month
     * @return The month's name in a string format
     */
    private String getMonthFormat(int month) {
        if (month == 1){
            return getString(R.string.January);
        }
        if (month == 2){
            return getString(R.string.February);
        }
        if (month == 3){
            return getString(R.string.March);
        }
        if (month == 4){
            return getString(R.string.April);
        }
        if (month == 5){
            return getString(R.string.May);
        }
        if (month == 6){
            return getString(R.string.June);
        }
        if (month == 7){
            return getString(R.string.July);
        }
        if (month == 8){
            return getString(R.string.August);
        }
        if (month == 9){
            return getString(R.string.September);
        }
        if (month == 10){
            return getString(R.string.October);
        }
        if (month == 11){
            return getString(R.string.November);
        }
        if (month == 12){
            return getString(R.string.December);
        }
        return getString(R.string.January);
    }
}