package ro.kenjiru.notes.dropbox;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.v2.users.FullAccount;

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

        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                String displayName = result.getName().getDisplayName();

                Toast.makeText(DropboxSyncService.this, "Dropbox user: " + displayName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();

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
