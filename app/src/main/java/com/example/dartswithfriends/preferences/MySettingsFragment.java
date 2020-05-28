package com.example.dartswithfriends.preferences;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import com.example.dartswithfriends.R;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
