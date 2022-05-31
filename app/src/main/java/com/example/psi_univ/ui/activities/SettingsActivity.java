package com.example.psi_univ.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.fragments.SettingsFragment;

import java.util.Locale;


public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.ToolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if (sharedPreferences.getString("key_language", "").compareTo("FRE") == 0) {
                    Toast.makeText(getApplicationContext(), sharedPreferences.getString("key_language", ""), Toast.LENGTH_SHORT).show();
                    setLocal("fr");
                }
                if (sharedPreferences.getString("key_language", "").compareTo("ENG") == 0) {
                    Toast.makeText(getApplicationContext(), sharedPreferences.getString("key_language", ""), Toast.LENGTH_SHORT).show();
                    setLocal("en");
                }
                onBackPressed();
            }
        });

        if (findViewById(R.id.settings_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getFragmentManager().beginTransaction().add(R.id.settings_container, new SettingsFragment()).commit();
        }


    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

}