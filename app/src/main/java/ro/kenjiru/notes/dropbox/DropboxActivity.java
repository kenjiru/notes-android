package ro.kenjiru.notes.dropbox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dropbox.core.android.Auth;

import ro.kenjiru.notes.R;

/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = prefs.getString("access-token", null);

        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();

            if (accessToken != null) {
                prefs.edit().putString("access-token", accessToken).apply();
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        loadDropboxData();
    }

    protected abstract void loadDropboxData();

    public void acquireToken() {
        Auth.startOAuth2Authentication(this, getString(R.string.dropbox_app_key));
    }

    public void deleteToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("access-token", null)
                .putString(getString(R.string.dropbox_user), null)
                .apply();
    }

    public boolean hasToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = prefs.getString("access-token", null);

        return accessToken != null;
    }
}
