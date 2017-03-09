package ro.kenjiru.notes.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ro.kenjiru.notes.xml.ConversionUtils;
import ro.kenjiru.notes.xml.Manifest;
import ro.kenjiru.notes.xml.Note;
import ro.kenjiru.notes.xml.NoteEntry;

public class DownloadManager {
    private static final String TAG = "Notes";

    public static final int DOWNLOAD_MANIFEST = 1;
    public static final int DOWNLOAD_NOTE = 2;
    public static final int DOWNLOAD_FAILED = 3;

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = NUMBER_OF_CORES * 2;
    private static final int MAXIMUM_POOL_SIZE = NUMBER_OF_CORES * 3;

    // Used for the singleton
    private static DownloadManager sInstance;

    private final ThreadPoolExecutor mBackgroundTasks;
    private final Handler mHandler;
    private final LinkedBlockingQueue<Runnable> mDownloadWorkQueue;

    public static DownloadManager getInstance() {
        if (sInstance == null) {
            synchronized (DownloadManager.class) {
                sInstance = new DownloadManager();
            }
        }

        return sInstance;
    }

    private DownloadManager() {
        ThreadFactory backgroundPriorityThreadFactory = new PriorityThreadFactory(
                Process.THREAD_PRIORITY_BACKGROUND);

        mDownloadWorkQueue = new LinkedBlockingQueue<Runnable>();

        mBackgroundTasks = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDownloadWorkQueue,
                backgroundPriorityThreadFactory);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case DOWNLOAD_MANIFEST:
                        Manifest manifest = (Manifest) inputMessage.obj;

                        Log.i(TAG, "DOWNLOAD_MANIFEST, number of notes: " + manifest.getNotes().size());

                        for (NoteEntry noteEntry : manifest.getNotes()) {
                            downloadFile(ConversionUtils.getNotePath(noteEntry));
                        }
                        break;

                    case DOWNLOAD_NOTE:
                        Note note = (Note) inputMessage.obj;

                        Log.i(TAG, "DOWNLOAD_NOTE, note title: " + note.getTitle());
                        break;

                    case DOWNLOAD_FAILED:
                        String fileName = (String) inputMessage.obj;

                        Log.i(TAG, "DOWNLOAD_FAILED, note file name: " + fileName);
                        break;
                }
            }
        };
    }

    public void startSynchronization() {
        downloadFile("/manifest.xml");
    }

    private void downloadFile(String file) {
        mBackgroundTasks.execute(new DownloadFileRunnable(this, file));
    }

    public void sendMessage(int what, Object obj) {
        Message msg = Message.obtain(mHandler, what, obj);
        mHandler.sendMessage(msg);
    }
}
