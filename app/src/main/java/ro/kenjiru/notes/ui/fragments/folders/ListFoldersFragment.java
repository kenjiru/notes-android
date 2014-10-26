package ro.kenjiru.notes.ui.fragments.folders;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.intent.Action;
import ro.kenjiru.notes.intent.Extra;
import ro.kenjiru.notes.model.Folder;
import ro.kenjiru.notes.model.SpecialFolder;
import ro.kenjiru.notes.ui.activities.ListNotesActivity;

public class ListFoldersFragment extends ListFragment implements NewFolderDialog.CreateFolderDialogListener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list_folders, container, false);

        attachButtonClickListener(fragmentView);
        attachListLongClickListener(fragmentView);

        return fragmentView;
    }

    private void attachListLongClickListener(View fragmentView) {
        ListView listView = (ListView) fragmentView.findViewById(android.R.id.list);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                removeFolder(index);

                return true;
            }
        });
    }

    private void removeFolder(int index) {
        Folder folder = (Folder) getListAdapter().getItem(index);

        if (folder instanceof SpecialFolder == false) {
            Toast.makeText(getActivity(), "Removed folder " + folder.getName(), Toast.LENGTH_SHORT).show();
            folder.delete();

            removeAllFolders();
            addAllFolders();
        }
    }

    private void attachButtonClickListener(View fragmentView) {
        View createButton = fragmentView.findViewById(R.id.new_folder_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFolderClicked();
            }
        });
    }

    private void newFolderClicked() {
        DialogFragment dialog = new NewFolderDialog();

        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "CreateFolderDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String folderName) {
        createFolder(folderName);
        removeAllFolders();
        addAllFolders();
    }

    private void createFolder(String folderName) {
        Folder folder = new Folder(folderName);
        folder.save();

        Toast.makeText(getActivity(), "Created folder " + folderName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createListAdapter();
        addAllFolders();
    }

    private void createListAdapter() {
        List<Folder> folders = new ArrayList<Folder>();

        ListAdapter listAdapter = new FoldersAdapter(getActivity(), folders);
        setListAdapter(listAdapter);
    }

    private void addAllFolders() {
        List<Folder> folders = new Select()
                .from(Folder.class)
                .orderBy("name ASC")
                .execute();

        FoldersAdapter listAdapter = (FoldersAdapter) this.getListAdapter();

        listAdapter.add(SpecialFolder.createAllFolders());
        listAdapter.add(SpecialFolder.createNoFolder());
        listAdapter.addAll(folders);
    }

    private void removeAllFolders() {
        FoldersAdapter listAdapter = (FoldersAdapter) this.getListAdapter();
        listAdapter.clear();
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
