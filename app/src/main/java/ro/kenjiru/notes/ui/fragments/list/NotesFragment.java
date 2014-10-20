package ro.kenjiru.notes.ui.fragments.list;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.activities.ViewNoteActivity;

public class NotesFragment extends ListFragment {

    private static final String LIST_INSTANCE_STATE = "LIST_INSTANCE_STATE";
    private static final String LIST_ADAPTER_ARRAY = "LIST_ADAPTER_ARRAY";
    private static final String NOTE = "NOTE";

    private List<Note> initialNotes = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        restoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInstanceState(outState);
    }

    private void saveInstanceState(Bundle outState) {
        ListView listView = getListView();
        NotesAdapter adapter = (NotesAdapter) listView.getAdapter();

        outState.putParcelable(LIST_INSTANCE_STATE, listView.onSaveInstanceState());
        outState.putSerializable(LIST_ADAPTER_ARRAY, (ArrayList<Note>) adapter.getAllItems());
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        createListAdapter(savedInstanceState);
        restoreListInstanceState(savedInstanceState);
    }

    private void createListAdapter(Bundle savedInstanceState) {
        List<Note> notes = new ArrayList<Note>();

        if (savedInstanceState != null) {
            Serializable listAdapterArray = savedInstanceState.getSerializable(LIST_ADAPTER_ARRAY);

            if (listAdapterArray != null) {
                notes = (ArrayList<Note>) listAdapterArray;
            }
        } else {
            if (initialNotes != null) {
                notes = initialNotes;
            }
        }

        ListAdapter listAdapter = new NotesAdapter(getActivity(), notes);
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

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Note entry = (Note) listView.getItemAtPosition(position);

        Intent intent = new Intent(getActivity(), ViewNoteActivity.class);
        intent.putExtra(NOTE, entry);
        startActivity(intent);
    }

    public void setNotes(List<Note> initialNotes) {
        this.initialNotes = initialNotes;
    }
}