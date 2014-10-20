package ro.kenjiru.notes.ui.fragments;

import android.os.Bundle;
import android.os.Handler;

import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.list.EndlessScrollListener;
import ro.kenjiru.notes.ui.fragments.list.NotesAdapter;
import ro.kenjiru.notes.ui.fragments.list.NotesFragment;

public class ListNotesFragment extends NotesFragment {
    private static final int ITEMS_PER_PAGE = 3;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setScrollListener();
    }

    private void setScrollListener() {
        int totalItemsCount = getListAdapter().getCount();

        getListView().setOnScrollListener(new EndlessScrollListener(ITEMS_PER_PAGE, totalItemsCount) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        loadMoreData(page, getItemsPerPage());
                    }
                }, 1000);
            }
        });
    }

    private void loadMoreData(int page, int itemsPerPage) {
        List<Note> notes = new Select()
                .from(Note.class)
                .orderBy("title ASC")
                .offset(page * itemsPerPage)
                .limit(itemsPerPage)
                .execute();

        NotesAdapter listAdapter = (NotesAdapter) getListAdapter();
        listAdapter.addAll(notes);
    }
}