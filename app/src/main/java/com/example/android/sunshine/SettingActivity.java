package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import java.util.List;


public class SettingActivity extends AppCompatPreferenceActivity {

    public static final String PREF_FONT_SIZE = "font_size";
    public static final String DEFAULT_FONT_SIZE = "12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /*    Must overide this method, using to check
        if fragment is reference of preference fragment  */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingFragment.class.getName().equalsIgnoreCase(fragmentName);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_font_screen);

            Preference fontsizePreference = findPreference("font_size");
            fontsizePreference.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String stringValue = value.toString();

                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    // Set the summary to reflect the new value.
                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .edit().putString(PREF_FONT_SIZE, stringValue).apply();

                    return true;
                }
            });
        }
    }
}
