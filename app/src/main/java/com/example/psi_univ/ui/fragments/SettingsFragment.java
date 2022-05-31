package com.example.psi_univ.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.psi_univ.R;
import com.example.psi_univ.ui.activities.SettingsActivity;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragment {

    private Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(value);
                preference.setSummary(listPreference.getEntries()[index]);
            }


            return true;
        }
    };

    private void bind(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    private void setLocal(String code){
        Resources resources = Resources.getSystem();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(code));
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        bind(findPreference("key_language"));
        bind(findPreference("key_date_format"));

    }
}