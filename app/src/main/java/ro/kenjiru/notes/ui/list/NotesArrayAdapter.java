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

import java.util.List;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;

/**
* Created by radu on 24.09.14.
*/
class NotesArrayAdapter extends BaseAdapter {
    private Activity activity = null;
    private static LayoutInflater inflater=null;
    private List<Note> items = null;

    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ROW = 1;

    private static final int serverListSize = 20;

    public NotesArrayAdapter(Activity activity, List<Note> list) {
        this.items = list;
        this.activity = activity;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     *  returns the correct view
     */
    @Override
    public  View getView(int position, View convertView, ViewGroup parent){
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            return getFooterView(position, convertView, parent);
        }

        return getRowView(position, convertView, parent);
    };

    public View getRowView(int position, View convertView, ViewGroup parent) {
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

    /**
     * returns a View to be displayed in the last row.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getFooterView(int position, View convertView,
                              ViewGroup parent) {
        if (position >= serverListSize && serverListSize > 0) {
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(activity);

            tvLastRow.setHint("Reached the last row.");
            tvLastRow.setGravity(Gravity.CENTER);

            return tvLastRow;
        }

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_loading, parent, false);
        }

        return convertView;
    }

    public void addAll(List<Note> newNotes) {
        for (Note note : newNotes) {
            items.add(note);
        }

        this.notifyDataSetChanged();
    }

    /**
     * disable click events on indicating rows
     */
    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == VIEW_TYPE_ROW;
    }

    /**
     * One type is normal data row, the other type is Progressbar
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * the size of the List plus one, the one is the last row, which displays a Progressbar
     */
    @Override
    public int getCount() {
        return items.size() + 1;
    }

    /**
     * return the type of the row,
     * the last row indicates the user that the ListView is loading more data
     */
    @Override
    public int getItemViewType(int position) {
        return (position >= items.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ROW;
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
