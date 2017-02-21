package ro.kenjiru.notes.dropbox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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

    public boolean hasToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = prefs.getString("access-token", null);

        return accessToken != null;
    }

    public void revokeToken() {
        executeRevokeToken();
        removeToken();
    }

    private void removeToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("access-token", null)
                .putString(getString(R.string.dropbox_user), null)
                .apply();
    }

    private void executeRevokeToken() {
        new TokenRevokeTask(DropboxClientFactory.getClient(), new TokenRevokeTask.Callback() {
            @Override
            public void onComplete() {
                Toast.makeText(getBaseContext(), "Successfully revoked the auth token", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to revoke token.", e);
            }
        }).execute();

        DropboxClientFactory.invalidateClient();
    }
}
