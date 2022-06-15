package com.example.psi_univ.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.psi_univ.R;
import com.example.psi_univ.backend.DataBaseHelper;
import com.example.psi_univ.models.Room;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AdvancedSearchActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TextView timer;
    Button dateButton;
    Button searchButton;
    DatePickerDialog datePickerDialog;
    int timerHour, timerMinute;
    ArrayAdapter<Room> arrayAdapter;
    private DrawerLayout drawerLayout;
    int mapPosition;

    int formatYear ;
    int formatMonth ;
    int formatDay ;

boolean def;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);


        //Toolbar Initialisation
        Toolbar toolbar = findViewById(R.id.ToolBarAdvancedSearch);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        //Variables initialisations
        timer = findViewById(R.id.time_select);
        dateButton = findViewById(R.id.date_button);


        //Drawer
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

        formatYear =cal.get(Calendar.YEAR);
        formatMonth =cal.get(Calendar.MONTH+1) ;
        formatDay =cal.get(Calendar.DAY_OF_MONTH);
        timerHour =cal.get(Calendar.HOUR);
        timerMinute =cal.get(Calendar.MINUTE);

        //On click listener in the fragment view
        timer.setOnClickListener(this);
        dateButton.setOnClickListener(this);

        //Home button
        ImageView imageView = findViewById(R.id.imageViewHome);
        imageView.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        //Search view

        SearchView searchView = findViewById(R.id.advanced_search_bar);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        List<Room> rooms = dataBaseHelper.getAllRooms();
        ListView room = findViewById(R.id.listRoom);
        def= true;
        Intent resultIntent = new Intent(this, BuildingActivity.class);
        room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                def = false;
                searchView.setQuery(rooms.get(position).getBuildingName() + " " + rooms.get(position).getRoomName(),true);
                searchView.clearFocus();
                mapPosition = position;

            }
        });

        room.setEmptyView(findViewById(R.id.empty));


        arrayAdapter = new ArrayAdapter<Room>(this, android.R.layout.simple_list_item_1, rooms);
        room.setAdapter(arrayAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View listLayout = findViewById(R.id.list_layout);
                if (hasFocus) listLayout.setVisibility(View.VISIBLE);
                else listLayout.setVisibility(View.GONE);
            }
        });


        //Search Button
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateTimePicker = makeTimeFormat(formatYear,formatMonth,formatDay,timerHour,timerMinute);

                if(!def) {
                    resultIntent.putExtra("building", rooms.get(mapPosition).getBuildingName());
                    resultIntent.putExtra("level", rooms.get(mapPosition).getLevelName());
                    resultIntent.putExtra("room", rooms.get(mapPosition).getRoomName());
                }
                else {
                    resultIntent.putExtra("building", "B12D");
                    resultIntent.putExtra("level", "0");
                }
                resultIntent.putExtra("time",dateTimePicker);
               // Toast.makeText(AdvancedSearchActivity.this, dateTimePicker, Toast.LENGTH_SHORT).show();
                startActivity(resultIntent);
            }
        });
    }

    private String makeTimeFormat(int formatYear, int formatMonth, int formatDay, int timerHour, int timerMinute) {
        String year = formatYear+"";
        String month= formatMonth +"";
        String day = formatDay+"";
        String hour= timerHour +"";
        String minute = timerMinute + "";

        if (formatMonth < 10){
            month = "0"+month;
        }
        if (formatDay < 10){
            day = "0"+day;
        }
        if (timerHour < 10){
            hour = "0"+hour;
        }
        if (timerMinute < 10){
            minute = "0"+minute;
        }

        return year+"-"+month+"-"+day+" "+hour+":"+minute;
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
        if (v.getId() == R.id.date_button) {
            //Date Picker
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
                month = month + 1;
                formatYear =year;
                formatMonth =month;
                formatDay =dayOfMonth;
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
        if (sharedPreferences.getString("key_date_format", " ").equals("dd-MM-yyyy HH:mm")) {
            return dayOfMonth + " " + getMonthFormat(month) + " " + year;
        }
        if (sharedPreferences.getString("key_date_format", " ").equals("yyyy-MM-dd HH:mm")) {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }
}