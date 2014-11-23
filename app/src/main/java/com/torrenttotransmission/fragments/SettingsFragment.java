package com.torrenttotransmission.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.torrenttotransmission.activities.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuVisibility(true);
        addPreferencesFromResource(R.xml.settings);
    }

}
