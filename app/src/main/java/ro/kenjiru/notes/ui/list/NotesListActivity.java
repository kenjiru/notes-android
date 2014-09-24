package ro.kenjiru.notes.ui.list;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import ro.kenjiru.notes.R;

public class NotesListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewactivity);

        ArrayList<String> list = getNotesList();
        ThumbnailArrayAdapter adapter = new ThumbnailArrayAdapter(this, R.layout.list_row, list);

        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
    }

    private ArrayList<String> getNotesList() {
        String[] values = new String[] { "Introduction", "Shopping List", "Todo", "Using Dropbox",
                "Price details", "Coffee", "Movie list", "JavaScript tutorial", "CSS layout howto" };

        ArrayList<String> list = new ArrayList<String>();
        for (String value : values) {
            list.add(value);
        }
        return list;
    }

}