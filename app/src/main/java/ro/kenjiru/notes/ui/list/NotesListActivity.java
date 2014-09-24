package ro.kenjiru.notes.ui.list;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;

public class NotesListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewactivity);

        ArrayList<Note> list = getNotesList();
        NotesArrayAdapter adapter = new NotesArrayAdapter(this, R.layout.list_row, list);

        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
    }

    private ArrayList<Note> getNotesList() {
        String[] values = new String[] { "Introduction", "Shopping List", "Todo", "Using Dropbox",
                "Price details", "Coffee", "Movie list", "JavaScript tutorial", "CSS layout howto" };

        ArrayList<Note> list = new ArrayList<Note>();
        for (String value : values) {
            list.add(new Note(value, "A slightly longer description."));
        }
        return list;
    }

}