package ro.kenjiru.notes.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;

class NotesAdapter extends ArrayAdapter<Note> {
    public NotesAdapter(Context context) {
        super(context, R.layout.list_row, new ArrayList<Note>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = getInflater().inflate(R.layout.list_row, parent, false);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        Note note = this.getItem(position);

        thumbnail.setImageResource(R.drawable.ic_launcher);
        title.setText(note.getTitle());
        description.setText(note.getShortDescription());

        return convertView;
    }

    private LayoutInflater getInflater() {
        return LayoutInflater.from(getContext());
    }
}
