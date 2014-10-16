package ro.kenjiru.notes.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.list.NotesActivity;
import ro.kenjiru.notes.ui.list.NotesAdapter;

public class SearchNotesActivity extends NotesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_notes);
        super.onCreate(savedInstanceState);

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
                .where("content LIKE ?", "%" + query + "%")
                .orderBy("title ASC")
                .execute();

        NotesAdapter listAdapter = (NotesAdapter) getListAdapter();
        listAdapter.clear();
        listAdapter.addAll(notes);
    }
}
