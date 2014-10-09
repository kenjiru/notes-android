package ro.kenjiru.notes.ui.list;

import android.app.Activity;
import android.content.Context;
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
    private List<Note> items = null;

    private static final int VIEW_TYPE_COUNT = 3;

    public static final int VIEW_TYPE_ROW = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final int VIEW_TYPE_LAST_ROW = 2;

    private static final int serverListSize = 13;

    public NotesAdapter(Activity activity) {
        this.activity = activity;
        this.items = new ArrayList<Note>();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent){
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ROW :
                convertView = handleRowView(position, convertView, parent);
                break;
            case VIEW_TYPE_LOADING :
                convertView = handleLoadingView(convertView, parent);
                break;
            case VIEW_TYPE_LAST_ROW:
                convertView = handleLastRowView(convertView, parent);
                break;
        }

        return convertView;
    }

    private View handleRowView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = getInflater().inflate(R.layout.list_row, parent, false);
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

    private View handleLoadingView(View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = getInflater().inflate(R.layout.list_loading, parent, false);
        }

        return convertView;
    }

    private View handleLastRowView(View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = getInflater().inflate(R.layout.list_last_row, parent, false);
        }

        return convertView;
    }

    private LayoutInflater getInflater() {
        return (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
