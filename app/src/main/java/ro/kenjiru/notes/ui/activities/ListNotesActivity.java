package ro.kenjiru.notes.ui.activities;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.dropbox.core.v2.users.FullAccount;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.dropbox.DropboxActivity;
import ro.kenjiru.notes.dropbox.DropboxClientFactory;
import ro.kenjiru.notes.dropbox.DropboxSyncService;
import ro.kenjiru.notes.dropbox.GetCurrentAccountTask;
import ro.kenjiru.notes.intent.Action;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.SpecialFolder;
import ro.kenjiru.notes.ui.fragments.folders.ListFoldersFragment;
import ro.kenjiru.notes.ui.fragments.notes.ListNotesFragment;

import static ro.kenjiru.notes.ui.fragments.settings.SettingsFragment.NOTES_FOLDER;

public class ListNotesActivity extends DropboxActivity implements ListFoldersFragment.OnFolderSelectedListener {
    private static final int RESULT_SETTINGS = 1;

    private Menu activityMenu;

    private DropboxSyncService dropboxSyncService;
    private boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            DropboxSyncService.LocalBinder binder = (DropboxSyncService.LocalBinder) service;
            dropboxSyncService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateAuthMenuItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        long folderId = SpecialFolder.ALL_FOLDERS;

        if (Action.FILTER_NOTES.equals(intent.getAction())) {
            long newFolderId = intent.getLongExtra(Extra.FOLDER_ID, SpecialFolder.ALL_FOLDERS);
            long previousFolderId = getPreviousFolderId();

            if (newFolderId != previousFolderId) {
                folderId = newFolderId;
            }
        }

        createListNotesFragment(folderId);
    }

    private long getPreviousFolderId() {
        ListNotesFragment listNotesFragment = (ListNotesFragment) getFragmentManager().findFragmentById(R.id.list_notes_fragment);

        if (listNotesFragment != null) {
            return listNotesFragment.getFolderId();
        }

        return SpecialFolder.ALL_FOLDERS;
    }

    private void createListNotesFragment(long folderId) {
        ListNotesFragment listNotesFragment = new ListNotesFragment();
        listNotesFragment.setFolderId(folderId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_notes_container, listNotesFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        activityMenu = menu;

        getMenuInflater().inflate(R.menu.list_notes, menu);
        configureSearchWidget(menu);
        updateAuthMenuItem();

        return super.onCreateOptionsMenu(menu);
    }

    private void configureSearchWidget(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;

            case R.id.action_dropbox_login:
                acquireToken();
                break;

            case R.id.action_dropbox_logout:
                revokeToken();
                updateAuthMenuItem();
                break;

            case R.id.action_dropbox_sync:
                startSync();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSync() {
        Intent intent = new Intent(this, DropboxSyncService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        startService(intent);
    }

    @Override
    protected void loadDropboxData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                String displayName = result.getName().getDisplayName();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListNotesActivity.this);

                prefs.edit().putString(getString(R.string.dropbox_user), displayName).apply();
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showAppSettings();
                break;

        }
    }

    private void showAppSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();
        builder.append("Notes folder: ").append(sharedPrefs.getString(NOTES_FOLDER, "NULL"));
    }

    @Override
    public void onFolderSelected(long folderId) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View drawer = findViewById(R.id.navigation_drawer);

        mDrawerLayout.closeDrawer(drawer);
    }

    private void updateAuthMenuItem() {
        if (activityMenu == null) {
            return;
        }

        MenuItem dropboxLogin = activityMenu.findItem(R.id.action_dropbox_login);
        MenuItem dropboxLogout = activityMenu.findItem(R.id.action_dropbox_logout);

        if (hasToken()) {
            dropboxLogin.setVisible(false);
            dropboxLogout.setVisible(true);
        } else {
            dropboxLogin.setVisible(true);
            dropboxLogout.setVisible(false);
        }
    }
}