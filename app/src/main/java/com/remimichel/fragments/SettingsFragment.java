package com.remimichel.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.remimichel.activities.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
