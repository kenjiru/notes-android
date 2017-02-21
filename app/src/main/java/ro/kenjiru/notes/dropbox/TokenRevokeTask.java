package ro.kenjiru.notes.dropbox;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

public class TokenRevokeTask extends AsyncTask<Void, Void, Boolean> {

    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;

    public interface Callback {
        void onComplete();
        void onError(Exception e);
    }

    public TokenRevokeTask(DbxClientV2 dbxClient, Callback callback) {
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            mDbxClient.auth().tokenRevoke();

        } catch (DbxException e) {
            mException = e;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onComplete();
        }
    }
}
