package ro.kenjiru.notes.ui.fragments.folders;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.intent.Action;
import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.ui.activities.ListNotesActivity;

public class ListFoldersFragment extends ListFragment {

    private static final String FOLDER = "FOLDER";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createListAdapter();
        addAllFolder();
    }

    private void createListAdapter() {
        List<Folder> folders = new ArrayList<Folder>();

        ListAdapter listAdapter = new FoldersAdapter(getActivity(), folders);
        setListAdapter(listAdapter);
    }

    private void addAllFolder() {
        List<Folder> folders = new Select()
                .from(Folder.class)
                .orderBy("name ASC")
                .execute();

        FoldersAdapter listAdapter= (FoldersAdapter) this.getListAdapter();
        listAdapter.addAll(folders);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Folder folder = (Folder) listView.getItemAtPosition(position);

        Intent intent = new Intent(Action.FILTER, null, getActivity(), ListNotesActivity.class);
        intent.putExtra(FOLDER, folder);

        startActivity(intent);
    }
}
