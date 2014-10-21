package ro.kenjiru.notes.ui.fragments.folders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Folder;

public class FoldersAdapter extends ArrayAdapter<Folder> {
    private final List<Folder> folders;

    public FoldersAdapter(Context context, List<Folder> folders) {
        super(context, R.layout.folder_list_row, folders);
        this.folders = folders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = getInflater().inflate(R.layout.folder_list_row, parent, false);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        Folder folder = this.getItem(position);

        thumbnail.setImageResource(R.drawable.ic_launcher);
        name.setText(folder.getName());

        return convertView;
    }

    public List<Folder> getAllItems() {
        return folders;
    }

    private LayoutInflater getInflater() {
        return LayoutInflater.from(getContext());
    }
}
