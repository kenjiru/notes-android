package ro.kenjiru.notes.ui.list;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;

class NotesAdapter extends BaseAdapter {
    private Activity activity = null;
    private static LayoutInflater inflater=null;
    private List<Note> items = null;

    private static final int VIEW_TYPE_COUNT = 3;

    public static final int VIEW_TYPE_ROW = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final int VIEW_TYPE_LAST_ROW = 2;

    private static final int serverListSize = 13;

    public NotesAdapter(Activity activity) {
        this.activity = activity;
        this.items = new ArrayList<Note>();

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent){
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ROW :
                convertView = handleRowView(position, convertView);
                break;
            case VIEW_TYPE_LOADING :
                convertView = handleLoadingView(position, convertView);
                break;
            case VIEW_TYPE_LAST_ROW:
                convertView = handleLastRowView(position, convertView);
                break;
        }

        return convertView;
    }

    private View handleRowView(int position, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        Note note = this.items.get(position);

        thumbnail.setImageResource(R.drawable.ic_launcher);
        title.setText(note.getTitle());
        description.setText(note.getShortDescription());

        return convertView;
    }

    private View handleLoadingView(int position, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_loading, null);
        }

        return convertView;
    }

    private View handleLastRowView(int position, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_last_row, null);
        }

        return convertView;
    }

    public void addAll(List<Note> newNotes) {
        for (Note note : newNotes) {
            items.add(note);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == VIEW_TYPE_ROW;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= items.size()) {
            if (position >= serverListSize && serverListSize > 0) {
                return VIEW_TYPE_LAST_ROW;
            }
            return VIEW_TYPE_LOADING;
        }

        return VIEW_TYPE_ROW;
    }

    @Override
    public Note getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ROW) ? items.get(position)
                : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ROW) ? position
                : -1;
    }
}
