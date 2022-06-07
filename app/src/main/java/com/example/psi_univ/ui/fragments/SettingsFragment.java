package com.example.psi_univ.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.psi_univ.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        int index;
        switch (key){
            case "key_language":
                androidx.preference.ListPreference listPreferenceLanguage = (androidx.preference.ListPreference) preference;
                assert listPreferenceLanguage != null;
                index = listPreferenceLanguage.findIndexOfValue(sharedPreferences.getString(key," "));
                preference.setSummary(listPreferenceLanguage.getEntries()[index]);
                Toast.makeText(getActivity(), "Changement d'heure", Toast.LENGTH_SHORT).show();
                break;
            case "key_date_format":
                androidx.preference.ListPreference listPreferenceDate = (androidx.preference.ListPreference) preference;
                assert listPreferenceDate != null;
                index = listPreferenceDate.findIndexOfValue(sharedPreferences.getString(key," "));
                preference.setSummary(listPreferenceDate.getEntries()[index]);
                break;
        }

    }
}