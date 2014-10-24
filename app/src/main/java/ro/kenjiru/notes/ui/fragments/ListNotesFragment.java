package ro.kenjiru.notes.ui.fragments;

import android.os.Bundle;
import android.os.Handler;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.notes.EndlessScrollListener;
import ro.kenjiru.notes.ui.fragments.notes.NotesAdapter;
import ro.kenjiru.notes.ui.fragments.notes.NotesFragment;

public class ListNotesFragment extends NotesFragment {
    private static final int ITEMS_PER_PAGE = 3;

    private long folderId = Folder.ALL_FOLDERS;

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
                loadDataAsync(page, getItemsPerPage());
            }
        });
    }

    private void loadDataAsync(final int page, final int itemsPerPage) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadMoreData(page, itemsPerPage);
            }
        });
    }

    private void loadMoreData(int page, int itemsPerPage) {
        From from = new Select()
                .from(Note.class)
                .orderBy("title ASC")
                .offset(page * itemsPerPage)
                .limit(itemsPerPage);

        if (folderId != Folder.ALL_FOLDERS) {
            from = from.where("folder = ?", folderId);
        }

        List<Note> notes = from.execute();

        NotesAdapter listAdapter = (NotesAdapter) getListAdapter();
        listAdapter.addAll(notes);
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getFolderId() {
        return folderId;
    }
}