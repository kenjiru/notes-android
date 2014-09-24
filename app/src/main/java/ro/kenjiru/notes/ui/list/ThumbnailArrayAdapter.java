package ro.kenjiru.notes.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ro.kenjiru.notes.R;

/**
* Created by radu on 24.09.14.
*/
class ThumbnailArrayAdapter extends ArrayAdapter<String> {
    private static LayoutInflater inflater=null;
    private List<String> items = null;

    public ThumbnailArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);

        this.items = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        thumbnail.setImageResource(R.drawable.ic_launcher);
        title.setText(this.items.get(position));
        description.setText("bar");

        return convertView;
    }

}
