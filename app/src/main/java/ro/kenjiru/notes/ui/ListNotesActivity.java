package ro.kenjiru.notes.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.list.EndlessScrollListener;
import ro.kenjiru.notes.ui.list.NotesActivity;
import ro.kenjiru.notes.ui.list.NotesAdapter;

public class ListNotesActivity extends NotesActivity {
    private static final int RESULT_SETTINGS = 1;
    private static final int ITEMS_PER_PAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_notes);
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setScrollListener();
    }

    private void setScrollListener() {
        int totalItemsCount = getListAdapter().getCount();

        getListView().setOnScrollListener(new EndlessScrollListener(ITEMS_PER_PAGE, totalItemsCount) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        loadMoreData(page, getItemsPerPage());
                    }
                }, 1000);
            }
        });
    }

    private void loadMoreData(int page, int itemsPerPage) {
        List<Note> notes = new Select()
                .from(Note.class)
                .orderBy("title ASC")
                .offset(page * itemsPerPage)
                .limit(itemsPerPage)
                .execute();

        NotesAdapter listAdapter = (NotesAdapter) getListAdapter();
        listAdapter.addAll(notes);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
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

        builder.append("Notes folder: " + sharedPrefs.getString(SettingsActivity.NOTES_FOLDER, "NULL"));
    }
}