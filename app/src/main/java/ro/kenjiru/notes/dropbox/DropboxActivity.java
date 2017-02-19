package ro.kenjiru.notes.dropbox;

import android.app.Activity;
import android.content.SharedPreferences;

import com.dropbox.core.android.Auth;


/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("notes", MODE_PRIVATE);
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

    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences("notes", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);

        return accessToken != null;
    }
}
