package ro.kenjiru.notes.ui.activities;

import android.app.Activity;
import android.os.Bundle;

import ro.kenjiru.notes.ui.fragments.settings.SettingsFragment;

public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
