package ro.kenjiru.notes.dropbox;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import ro.kenjiru.notes.async.DownloadManager;

public class DropboxSyncService extends IntentService {
    private static final String TAG = "Notes";
    private final IBinder mBinder = new LocalBinder();
    private Handler mHandler;

    public DropboxSyncService() {
        super("DropboxSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "DropboxSyncService starting", Toast.LENGTH_SHORT).show();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                String msg = (String) inputMessage.obj;

                Log.i(TAG, "Message outside handler: " + msg);
            }
        };

        DownloadManager.getInstance().startSynchronization();

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
