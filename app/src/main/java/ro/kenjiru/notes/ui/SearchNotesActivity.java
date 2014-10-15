package ro.kenjiru.notes.ui;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.list.NotesAdapter;

public class SearchNotesActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_notes);

        createListAdapter();
        setClickListener();
        handleIntent(getIntent());
    }

    private void createListAdapter() {
        ListAdapter listAdapter = new NotesAdapter(this, new ArrayList<Note>());
        setListAdapter(listAdapter);
    }

    private void setClickListener() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note entry = (Note) parent.getItemAtPosition(position);

                Intent intent = new Intent(SearchNotesActivity.this, NoteViewActivity.class);
                intent.putExtra("Note", entry);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            executeSearch(query);
        }
    }

    private void executeSearch(String query) {
        Log.println(Log.DEBUG, "search query", query);

        List<Note> notes = new Select()
                .from(Note.class)
                .where("description LIKE ?", "%" + query + "%")
                .orderBy("title ASC")
                .execute();

        NotesAdapter listAdapter = (NotesAdapter) getListAdapter();
        listAdapter.addAll(notes);
    }
}
