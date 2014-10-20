package ro.kenjiru.notes.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.view.ViewNoteFragment;

public class ViewNoteActivity extends Activity {

    private static final String NOTE = "NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

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
        Bundle savedInstanceState = intent.getExtras();
        Note note = (Note) savedInstanceState.getSerializable(NOTE);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
