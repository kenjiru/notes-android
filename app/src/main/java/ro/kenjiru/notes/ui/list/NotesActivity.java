package ro.kenjiru.notes.ui.list;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.ViewNoteActivity;

public class NotesActivity extends ListActivity  {
    private static final String LIST_INSTANCE_STATE = "LIST_INSTANCE_STATE";
    private static final String LIST_ADAPTER_ARRAY = "LIST_ADAPTER_ARRAY";

    private boolean stateRestored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        restoreInstanceState(savedInstanceState);
    }

    private void saveInstanceState(Bundle outState) {
        ListView listView = getListView();
        NotesAdapter adapter = (NotesAdapter) listView.getAdapter();

        outState.putParcelable(LIST_INSTANCE_STATE, listView.onSaveInstanceState());
        outState.putSerializable(LIST_ADAPTER_ARRAY, (ArrayList<Note>) adapter.getAllItems());
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        // onCreate and onSaveInstanceState might both execute
        if (!stateRestored) {
            createListAdapter(savedInstanceState);
            restoreListInstanceState(savedInstanceState);

            setClickListener();

            stateRestored = true;
        }
    }

    private void createListAdapter(Bundle savedInstanceState) {
        ArrayList<Note> notes = new ArrayList<Note>();

        if (savedInstanceState != null) {
            Serializable listAdapterArray = savedInstanceState.getSerializable(LIST_ADAPTER_ARRAY);

            if (listAdapterArray != null) {
                notes = (ArrayList<Note>) listAdapterArray;
            }
        }

        ListAdapter listAdapter = new NotesAdapter(this, notes);
        setListAdapter(listAdapter);
    }

    private void restoreListInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable listInstanceState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
            if (listInstanceState != null) {
                getListView().onRestoreInstanceState(listInstanceState);
            }
        }
    }

    private void setClickListener() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note entry = (Note) parent.getItemAtPosition(position);

                Intent intent = new Intent(NotesActivity.this, ViewNoteActivity.class);
                intent.putExtra("Note", entry);
                startActivity(intent);
            }
        });

    }
}