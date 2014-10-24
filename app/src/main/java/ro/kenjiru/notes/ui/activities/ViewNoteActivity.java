package ro.kenjiru.notes.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.Model;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.view.ViewNoteFragment;

public class ViewNoteActivity extends Activity {

    private static final String NOTE = "NOTE";

    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        if (savedInstanceState == null) {
            handleIntent();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        determineNote();
        createViewNoteFragment();
    }

    private void determineNote() {
        Intent intent = getIntent();
        Bundle savedInstanceState = intent.getExtras();

        long noteId = savedInstanceState.getLong(Extra.NOTE_ID);
        note = Model.load(Note.class, noteId);
    }

    private void createViewNoteFragment() {
        ViewNoteFragment viewNoteFragment = (ViewNoteFragment) getFragmentManager().findFragmentById(R.id.view_note_fragment);
        viewNoteFragment.setNote(note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
