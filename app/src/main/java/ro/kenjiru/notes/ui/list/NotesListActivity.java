package ro.kenjiru.notes.ui.list;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import java.util.ArrayList;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;

public class NotesListActivity extends Activity {

    private NotesArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        setAdapter();
        setScrollListener();
    }

    private void setAdapter() {
        ArrayList<Note> list = getNotesList();
        NotesArrayAdapter adapter = new NotesArrayAdapter(this, list);

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

    private ArrayList<Note> getNotesList() {
        String[] values = new String[] { "Introduction", "Shopping List", "Todo", "Using Dropbox",
                "Price details", "Coffee", "Movie list", "JavaScript tutorial", "CSS layout howto" };
        ArrayList<Note> list = new ArrayList<Note>();

        for (String value : values) {
            list.add(new Note(value, "A slightly longer description."));
        }
        return list;
    }

    private void loadMoreData(int page) {
        String[] newValues = new String[] { "Foo", "Bar", "Baz"};
        ArrayList<Note> list = new ArrayList<Note>();

        for (String value : newValues) {
            list.add(new Note(value, "Dynamically loaded item."));
        }

        adapter.addAll(list);
    }
}