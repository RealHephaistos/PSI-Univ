package com.example.psi_univ.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.psi_univ.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Locale;


public class AdvancedSearchActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TextView timer;
    SwitchCompat availableRoom;
    SwitchCompat unavailableRoom;
    Button dateButton;
    DatePickerDialog datePickerDialog;
    int timerHour, timerMinute;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        Toolbar toolbar = findViewById(R.id.ToolBarAdvancedSearch);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        //Variables initialisations
        timer = findViewById(R.id.time_select);
        availableRoom = findViewById(R.id.switch_available_room);
        unavailableRoom = findViewById(R.id.switch_unavailable_room);
        dateButton = findViewById(R.id.date_button);


        //Drawer
        ImageView imageView = findViewById(R.id.imageViewHome);
        drawerLayout = findViewById(R.id.advancedSearchContainer);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.advancedSearch);


        //Calendar for the Date Picker
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        //Current date as Hint in the Button
        dateButton.setHint(
                makeDateString(mDay, mMonth, mYear));

        //On click listener in the fragment view
        availableRoom.setOnClickListener(this);
        unavailableRoom.setOnClickListener(this);
        timer.setOnClickListener(this);
        dateButton.setOnClickListener(this);

        imageView.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

    }


    public void onClick(View v) {

        if (v.getId() == R.id.time_select) {
            //Timer Picker
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
                timerHour = hourOfDay;
                timerMinute = minute;
                timer.setText(String.format(Locale.getDefault(), "%02d:%02d", timerHour, timerMinute));
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, timerHour, timerMinute, true);
            timePickerDialog.setTitle(R.string.drawer_close);
            timePickerDialog.show();
        }
        if (v.getId() == R.id.switch_available_room) {
            //Message shown with the available rooms switch button
            if (availableRoom.isChecked()) {
                Toast.makeText(this, R.string.advanced_search_unavailable_rooms_toast_on, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.advanced_search_available_rooms_toast_of, Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.switch_unavailable_room) {
            //Message shown with the available rooms switch button
            if (unavailableRoom.isChecked()) {
                Toast.makeText(this, R.string.advanced_search_unavailable_rooms_toast_on, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.advanced_search_unavailable_rooms_toast_of, Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.date_button) {
            //Date Picker
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setHint(date);
            };
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);


            int style = 0;

            datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

            datePickerDialog.show();
        }
    }

    /**
     * Date to String
     *
     * @param dayOfMonth the day of the month
     * @param month      the month
     * @param year       the year
     * @return A date in a string format
     */
    private String makeDateString(int dayOfMonth, int month, int year) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("key_date_format", " ").equals("dd/mm/yyyy")) {
            return dayOfMonth + " " + getMonthFormat(month) + " " + year;
        }
        if (sharedPreferences.getString("key_date_format", " ").equals("yyyy/mm/dd")) {
            return year + " " + getMonthFormat(month) + " " + dayOfMonth;
        }
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    /**
     * @param month the month
     * @return The month's name in a string format
     */
    private String getMonthFormat(int month) {
        if (month == 1) {
            return getString(R.string.January);
        }
        if (month == 2) {
            return getString(R.string.February);
        }
        if (month == 3) {
            return getString(R.string.March);
        }
        if (month == 4) {
            return getString(R.string.April);
        }
        if (month == 5) {
            return getString(R.string.May);
        }
        if (month == 6) {
            return getString(R.string.June);
        }
        if (month == 7) {
            return getString(R.string.July);
        }
        if (month == 8) {
            return getString(R.string.August);
        }
        if (month == 9) {
            return getString(R.string.September);
        }
        if (month == 10) {
            return getString(R.string.October);
        }
        if (month == 11) {
            return getString(R.string.November);
        }
        if (month == 12) {
            return getString(R.string.December);
        }
        return getString(R.string.January);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homepage) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}