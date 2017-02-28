package ro.kenjiru.notes.dropbox;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class DropboxSyncService extends IntentService {
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
