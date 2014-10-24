package ro.kenjiru.notes.ui.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.widget.SearchView;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.intent.Action;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.ui.fragments.ListNotesFragment;

public class ListNotesActivity extends Activity {

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
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        long folderId = Folder.ALL_FOLDERS;

        if (Action.FILTER_NOTES.equals(intent.getAction())) {
            long newFolderId = intent.getLongExtra(Extra.FOLDER_ID, Folder.ALL_FOLDERS);
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

        return Folder.ALL_FOLDERS;
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
        getMenuInflater().inflate(R.menu.list_notes, menu);
        configureSearchWidget(menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void configureSearchWidget(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }
}