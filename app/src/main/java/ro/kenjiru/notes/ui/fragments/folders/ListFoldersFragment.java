package ro.kenjiru.notes.ui.fragments.folders;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.intent.Action;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.model.SpecialFolder;
import ro.kenjiru.notes.ui.activities.ListNotesActivity;

public class ListFoldersFragment extends ListFragment {

    public interface OnFolderSelectedListener {
        public void onFolderSelected(long folderId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnFolderSelectedListener == false) {
            Log.w("KENJIRU", "The activity doesn't implement OnFolderSelectedListener");
        }
    }

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

        FoldersAdapter listAdapter = (FoldersAdapter) this.getListAdapter();
        listAdapter.add(SpecialFolder.createAllFolders());
        listAdapter.add(SpecialFolder.createNoFolder());
        listAdapter.addAll(folders);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Folder folder = (Folder) listView.getItemAtPosition(position);
        long folderId;

        if (folder instanceof SpecialFolder) {
            folderId = ((SpecialFolder) folder).getSpecialId();
        } else {
            folderId = folder.getId();
        }

        notifyParentActivity(folderId);
        startFilterNotesActivity(folderId);
    }

    private void startFilterNotesActivity(long folderId) {
        Intent intent = new Intent(Action.FILTER_NOTES, null, getActivity(), ListNotesActivity.class);
        intent.putExtra(Extra.FOLDER_ID, folderId);

        startActivity(intent);
    }

    private void notifyParentActivity(long folderId) {
        Activity activity = getActivity();

        if (activity instanceof OnFolderSelectedListener) {
            ((OnFolderSelectedListener) activity).onFolderSelected(folderId);
        }
    }
}
