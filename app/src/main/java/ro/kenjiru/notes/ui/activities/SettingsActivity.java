package ro.kenjiru.notes.ui.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.util.Map;

import ro.kenjiru.notes.R;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String NOTES_FOLDER = "pref_notesFolder";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        setSummariesForPreferences();
    }

    private void setSummariesForPreferences() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Map<String, ?> allPreferences = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allPreferences.entrySet()) {
            Preference pref = findPreference(entry.getKey());

            pref.setSummary((String) entry.getValue());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(NOTES_FOLDER)) {
            Preference folderPref = findPreference(key);
            folderPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
