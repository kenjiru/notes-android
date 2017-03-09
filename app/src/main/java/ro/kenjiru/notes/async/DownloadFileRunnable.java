package ro.kenjiru.notes.async;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ro.kenjiru.notes.dropbox.DropboxClientFactory;
import ro.kenjiru.notes.xml.ConversionUtils;
import ro.kenjiru.notes.xml.Manifest;
import ro.kenjiru.notes.xml.Note;

import static ro.kenjiru.notes.async.DownloadManager.DOWNLOAD_FAILED;
import static ro.kenjiru.notes.async.DownloadManager.DOWNLOAD_MANIFEST;
import static ro.kenjiru.notes.async.DownloadManager.DOWNLOAD_NOTE;

public class DownloadFileRunnable implements Runnable {
    private static final String TAG = "Notes";
    private final String mFile;
    private final DownloadManager mDownloadManager;

    public DownloadFileRunnable(DownloadManager downloadManager, String file) {
        mDownloadManager = downloadManager;
        mFile = file;
    }

    @Override
    public void run() {
        DbxClientV2 mDbxClient = DropboxClientFactory.getClient();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            mDbxClient.files().downloadBuilder(mFile).download(outputStream);

            ByteArrayInputStream input = new ByteArrayInputStream(outputStream.toByteArray());

            if (mFile.equalsIgnoreCase("/manifest.xml")) {
                Manifest manifest = ConversionUtils.toManifest(input);
                mDownloadManager.sendMessage(DOWNLOAD_MANIFEST, manifest);
            } else {
                Note note = ConversionUtils.toNote(input);
                mDownloadManager.sendMessage(DOWNLOAD_NOTE, note);
            }
        } catch (DbxException | IOException e) {
            mDownloadManager.sendMessage(DOWNLOAD_FAILED, mFile);

            e.printStackTrace();
        }
    }
}
