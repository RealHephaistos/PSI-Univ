package com.example.psi_univ.ui.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.psi_univ.R;

import java.util.Locale;
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
                if (sharedPreferences.getString("key_language", "").compareTo("FRE") == 0) {
                    //Toast.makeText(getContext(), sharedPreferences.getString("key_language", " "), Toast.LENGTH_SHORT).show();
                    setLocal("fr");
                }
                else {
                    setLocal("eng");
                }

                break;
            case "key_date_format":
                androidx.preference.ListPreference listPreferenceDate = (androidx.preference.ListPreference) preference;
                assert listPreferenceDate != null;
                index = listPreferenceDate.findIndexOfValue(sharedPreferences.getString(key," "));
                preference.setSummary(listPreferenceDate.getEntries()[index]);
                break;
            case "keyAvailableRoom":
                if (sharedPreferences.getBoolean("keyAvailableRoom", false)) {
                    Toast.makeText(getActivity(), R.string.available_rooms_toast_on, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), R.string.available_rooms_toast_of, Toast.LENGTH_SHORT).show();
                }
                break;
            case "keyUnavailableRoom":
                if (sharedPreferences.getBoolean("keyUnavailableRoom", false)) {
                    Toast.makeText(getActivity(), R.string.unavailable_rooms_toast_on, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), R.string.unavailable_rooms_toast_of, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void setLocal(String code) {
        if (isAdded()) {
            Locale locale = new Locale(code);
            Resources resources = getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();

            configuration.setLocale(locale);

            resources.updateConfiguration(configuration, metrics);
        }
    }


}

