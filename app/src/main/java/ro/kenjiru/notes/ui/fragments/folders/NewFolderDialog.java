package ro.kenjiru.notes.ui.fragments.folders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import ro.kenjiru.notes.R;

public class NewFolderDialog extends DialogFragment {

    public interface CreateFolderDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String folderName);
    }

    private CreateFolderDialogListener dialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Fragment parentFragment = getTargetFragment();
        try {
            dialogListener = (CreateFolderDialogListener) parentFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(parentFragment.toString() + " should implement CreateFolderDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_folder, null))
                .setMessage(R.string.create_new_folder)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog newFolderDialog = NewFolderDialog.this.getDialog();
                        TextView folderName = (TextView) newFolderDialog.findViewById(R.id.folder_name);

                        dialogListener.onDialogPositiveClick(NewFolderDialog.this, folderName.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog newFolderDialog = NewFolderDialog.this.getDialog();
                        newFolderDialog.cancel();
                    }
                });

        return builder.create();
    }
}