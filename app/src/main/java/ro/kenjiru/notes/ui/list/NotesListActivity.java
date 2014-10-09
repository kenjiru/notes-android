package ro.kenjiru.notes.ui.list;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.NoteViewActivity;
import ro.kenjiru.notes.ui.SettingsActivity;

public class NotesListActivity extends ListActivity  {
    private static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setAdapter();
        setClickListener();
        setScrollListener();
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

    private void setAdapter() {
        ListAdapter listAdapter = new NotesAdapter(this);
        setListAdapter(listAdapter);
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
                }, 500);
            }
        });
    }

    private void loadMoreData(int page) {
        List<Note> notes = new Select()
                .from(Note.class)
                .orderBy("title ASC")
                .offset((page-2)*3)
                .limit(3)
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