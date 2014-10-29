package ro.kenjiru.notes.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.folders.FoldersSpinnerAdapter;
import ro.kenjiru.notes.ui.fragments.view.ViewNoteFragment;

public class ViewNoteActivity extends Activity implements ActionBar.OnNavigationListener {

    private Note note = null;
    private ArrayAdapter<Folder> foldersAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        restoreInstanceState(savedInstanceState);
        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(Extra.NOTE_ID, note.getId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        restoreInstanceState(null);
        updateUi();
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreNoteFromBundle(savedInstanceState);
        } else {
            Intent intent = getIntent();

            if (intent != null) {
                Bundle extras = intent.getExtras();
                restoreNoteFromBundle(extras);
            }
        }
    }

    private void restoreNoteFromBundle(Bundle bundle) {
        long noteId = bundle.getLong(Extra.NOTE_ID);

        this.note = Model.load(Note.class, noteId);
    }

    private void updateUi() {
        createViewNoteFragment();
        configureDropDownNavigation();
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

    private void configureDropDownNavigation() {
        foldersAdapter = new FoldersSpinnerAdapter(getActionBar().getThemedContext() , getFolders());
        int folderPosition = foldersAdapter.getPosition(note.getFolder());

        ActionBar actionBar = getActionBar();
        actionBar.setListNavigationCallbacks(foldersAdapter, this);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setSelectedNavigationItem(folderPosition);
    }

    private List<Folder> getFolders() {
        List<Folder> actualFolders = new Select()
                .from(Folder.class)
                .orderBy("name ASC")
                .execute();

        List<Folder> folders = new ArrayList<Folder>();
        folders.add(new Folder("None"));
        folders.addAll(actualFolders);

        return folders;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        Folder folder = foldersAdapter.getItem(itemPosition);

        if (!folder.equals(note.getFolder())) {
            note.setFolder(folder);
            note.save();

            showMovedMessage(folder.getName());
        }

        return false;
    }

    private void showMovedMessage(String folderName) {
        String message = getString(R.string.moved_to);
        message = String.format(message, folderName);

        Toast.makeText(getBaseContext(), Html.fromHtml(message), Toast.LENGTH_SHORT).show();
    }
}
