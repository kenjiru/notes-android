package ro.kenjiru.notes.ui.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.notes.NotesFragment;

public class SearchNotesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_notes);

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
        List<Note> notes = new Select()
                .from(Note.class)
                .where("content LIKE ?", "%" + query + "%")
                .orderBy("title ASC")
                .execute();

        NotesFragment notesFragment = (NotesFragment) getFragmentManager().findFragmentById(R.id.list_notes_fragment);

        notesFragment.setNotes(notes);
    }
}
