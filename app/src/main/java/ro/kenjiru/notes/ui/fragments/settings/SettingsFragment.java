package ro.kenjiru.notes.ui.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.Map;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.ui.activities.SettingsActivity;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String NOTES_FOLDER = "pref_notesFolder";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        setSummariesForPreferences();
        handleDropboxLogin();
    }

    private void handleDropboxLogin() {
        Preference button = findPreference(getString(R.string.dropbox_user));

        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsActivity settingsActivity = (SettingsActivity) getActivity();

                if (settingsActivity.hasToken()) {
                    // TODO Add a confirmation dialog here
                    settingsActivity.deleteToken();
                } else {
                    settingsActivity.acquireToken();
                }

                return true;
            }
        });
    }

    private void setSummariesForPreferences() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Map<String, ?> allPreferences = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allPreferences.entrySet()) {
            Preference pref = findPreference(entry.getKey());

            if (pref != null) {
                pref.setSummary((String) entry.getValue());
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (key.equals(NOTES_FOLDER)) {
            pref.setSummary(sharedPreferences.getString(key, ""));
        } else if (key.equals(getString(R.string.dropbox_user))) {
            pref.setSummary(sharedPreferences.getString(key, getString(R.string.not_logged_in)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
