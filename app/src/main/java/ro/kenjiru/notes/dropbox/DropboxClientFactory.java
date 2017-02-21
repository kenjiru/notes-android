package ro.kenjiru.notes.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.AuthActivity;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Singleton instance of {@link DbxClientV2} and friends
 */
public class DropboxClientFactory {

    private static DbxClientV2 sDbxClient;

    public static void init(String accessToken) {
        if (sDbxClient == null) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("notes").build();

            sDbxClient = new DbxClientV2(requestConfig, accessToken);
        }
    }

    public static DbxClientV2 getClient() {
        if (sDbxClient == null) {
            throw new IllegalStateException("Client not initialized.");
        }

        return sDbxClient;
    }

    public static void invalidateClient() {
        sDbxClient = null;

        // We need to reset the result of the last authentication using the Dropbox AuthActivity
        // Otherwise, Auth.getOAuth2Token() will return the last authenticated token
        AuthActivity.result = null;
    }
}
