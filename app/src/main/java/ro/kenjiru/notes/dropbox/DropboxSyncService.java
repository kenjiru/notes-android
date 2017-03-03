package ro.kenjiru.notes.dropbox;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import ro.kenjiru.notes.xml.ConversionUtils;
import ro.kenjiru.notes.xml.Manifest;

public class DropboxSyncService extends IntentService {
    private static final String TAG = "Notes";
    private final IBinder mBinder = new LocalBinder();

    public DropboxSyncService() {
        super("DropboxSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "DropboxSyncService starting", Toast.LENGTH_SHORT).show();

        new DownloadFileTask(DropboxClientFactory.getClient(), new DownloadFileTask.Callback() {
            @Override
            public void onDownloadComplete(ByteArrayOutputStream result) {
                ByteArrayInputStream input = new ByteArrayInputStream(result.toByteArray());

                Manifest manifest = ConversionUtils.toManifest(input);
                Log.i(TAG, "Number of notes: " + manifest.getNotes().size());
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Failed to download file.", e);
                Toast.makeText(DropboxSyncService.this, "An error has occurred", Toast.LENGTH_SHORT).show();
            }
        }).execute("/manifest.xml");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public DropboxSyncService getService() {
            return DropboxSyncService.this;
        }
    }
}
