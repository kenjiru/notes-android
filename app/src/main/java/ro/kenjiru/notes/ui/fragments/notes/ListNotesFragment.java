package ro.kenjiru.notes.ui.fragments.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.model.SpecialFolder;

public class ListNotesFragment extends NotesFragment {
    private static final int ITEMS_PER_PAGE = 3;

    private long folderId = SpecialFolder.ALL_FOLDERS;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        restoreInstanceState(savedInstanceState);
        setScrollListener();
        attachListLongClickListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInstanceState(outState);
    }

    private void saveInstanceState(Bundle outState) {
        outState.putLong(Extra.FOLDER_ID, folderId);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            folderId = savedInstanceState.getLong(Extra.FOLDER_ID);
        }
    }

    private void attachListLongClickListener() {
        ListView listView = getListView();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                showDeleteDialog(index);

                return true;
            }
        });
    }

    private void showDeleteDialog(final int index) {
        Note note = (Note) getListAdapter().getItem(index);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getActivity().getString(R.string.delete_note);
        message = String.format(message, note.getTitle());

        builder.setMessage(Html.fromHtml(message))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNote(index);
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        builder.create().show();
    }

    private void deleteNote(int index) {
        Note note = (Note) getListAdapter().getItem(index);
        note.delete();

        ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
        adapter.remove(note);
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

        if (SpecialFolder.isSpecialFolder(folderId)) {
            if (folderId == SpecialFolder.NO_FOLDER) {
                from = from.where("folder is null");
            }
        } else {
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