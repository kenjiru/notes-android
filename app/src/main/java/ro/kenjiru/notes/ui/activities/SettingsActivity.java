package ro.kenjiru.notes.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dropbox.core.v2.users.FullAccount;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.dropbox.DropboxActivity;
import ro.kenjiru.notes.dropbox.DropboxClientFactory;
import ro.kenjiru.notes.dropbox.GetCurrentAccountTask;
import ro.kenjiru.notes.ui.fragments.settings.SettingsFragment;

public class SettingsActivity extends DropboxActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void loadDropboxData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                String displayName = result.getName().getDisplayName();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

                prefs.edit().putString(getString(R.string.dropbox_user), displayName).apply();
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }
}
