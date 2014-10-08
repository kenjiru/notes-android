package ro.kenjiru.notes.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.NoteViewActivity;
import ro.kenjiru.notes.ui.SettingsActivity;

public class NotesListActivity extends Activity {
    private static final int RESULT_SETTINGS = 1;

    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setClickListener();
        setAdapter();
        setScrollListener();
    }

    private void setClickListener() {
        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        NotesAdapter adapter = new NotesAdapter(this);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        this.adapter = adapter;
    }

    private void setScrollListener() {
        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setOnScrollListener(new EndlessScrollListener() {
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
                }, 5000);
            }
        });
    }

    private void loadMoreData(int page) {
        List<Note> notes = new Select()
                .from(Note.class)
                .orderBy("title ASC")
                .offset(page*3)
                .limit(3)
                .execute();

        adapter.addAll(notes);
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