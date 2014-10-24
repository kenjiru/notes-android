package ro.kenjiru.notes.ui.fragments.folders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Folder;

public class FoldersSpinnerAdapter extends ArrayAdapter<Folder> implements SpinnerAdapter {
    private final List<Folder> folders;

    public FoldersSpinnerAdapter(Context context, List<Folder> folders) {
        super(context, android.R.layout.simple_spinner_item, folders);
        this.folders = folders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(getContext(), android.R.layout.simple_spinner_item, null);

        Folder folder = this.getItem(position);
        textView.setText(folder.getName());

        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(getContext(), android.R.layout.simple_dropdown_item_1line, null);

        Folder folder = this.getItem(position);
        textView.setText(folder.getName());

        return textView;
    }
}
