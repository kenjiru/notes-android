package ro.kenjiru.notes.dropbox;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class DownloadFileTask extends AsyncTask<String, Void, OutputStream> {

    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;

    public interface Callback {
        void onDownloadComplete(OutputStream result);

        void onError(Exception e);
    }

    DownloadFileTask(DbxClientV2 dbxClient, Callback callback) {
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(OutputStream result) {
        super.onPostExecute(result);

        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onDownloadComplete(result);
        }
    }

    @Override
    protected OutputStream doInBackground(String... params) {
        String fileName = params[0];
        OutputStream outputStream = null;

        try {
            outputStream = new ByteArrayOutputStream();

            try {
                FileMetadata metadata = mDbxClient.files().downloadBuilder(fileName).download(outputStream);
            } finally {
                outputStream.close();
            }

            return outputStream;
        } catch (DbxException | IOException e) {
            mException = e;
        }

        return outputStream;
    }
}
