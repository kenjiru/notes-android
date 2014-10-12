package ro.kenjiru.notes.ui.list;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.NoteViewActivity;
import ro.kenjiru.notes.ui.SettingsActivity;

public class NotesListActivity extends ListActivity  {
    private static final int RESULT_SETTINGS = 1;
    private static final String LIST_INSTANCE_STATE = "LIST_INSTANCE_STATE";
    private static final String LIST_ADAPTER_ARRAY = "LIST_ADAPTER_ARRAY";

    private boolean stateRestored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        restoreInstanceState(savedInstanceState);
    }

    private void saveInstanceState(Bundle outState) {
        ListView listView = getListView();
        NotesAdapter adapter = (NotesAdapter) listView.getAdapter();

        outState.putParcelable(LIST_INSTANCE_STATE, listView.onSaveInstanceState());
        outState.putSerializable(LIST_ADAPTER_ARRAY, (ArrayList<Note>) adapter.getAllItems());
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        // onCreate and onSaveInstanceState might both execute
        if (!stateRestored) {
            createListAdapter(savedInstanceState);
            restoreListInstanceState(savedInstanceState);

            setClickListener();
            setScrollListener();

            stateRestored = true;
        }
    }

    private void createListAdapter(Bundle savedInstanceState) {
        ArrayList<Note> notes = new ArrayList<Note>();

        if (savedInstanceState != null) {
            Serializable listAdapterArray = savedInstanceState.getSerializable(LIST_ADAPTER_ARRAY);

            if (listAdapterArray != null) {
                notes = (ArrayList<Note>) listAdapterArray;
            }
        }

        ListAdapter listAdapter = new NotesAdapter(this, notes);
        setListAdapter(listAdapter);
    }

    private void restoreListInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable listInstanceState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
            if (listInstanceState != null) {
                getListView().onRestoreInstanceState(listInstanceState);
            }
        }
    }

    private void setClickListener() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note entry = (Note) parent.getItemAtPosition(position);

                Intent intent = new Intent(NotesListActivity.this, NoteViewActivity.class);
                intent.putExtra("Note", entry);
                startActivity(intent);
            }
        });

    }

    private void setScrollListener() {
        getListView().setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        loadMoreData(page);
                    }
                }, 1000);
            }
        });
    }

    private void loadMoreData(int page) {
        List<Note> notes = new Select()
                .from(Note.class)
                .orderBy("title ASC")
                .offset(page*3)
                .limit(7)
                .execute();

        NotesAdapter listAdapter = (NotesAdapter) getListAdapter();
        listAdapter.addAll(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notes, menu);
        return true;
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