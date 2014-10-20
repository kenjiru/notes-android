package ro.kenjiru.notes.ui.fragments.view;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ro.kenjiru.notes.R;
import ro.kenjiru.notes.model.Note;
import ro.kenjiru.notes.ui.fragments.view.ListTagHandler;

public class ViewNoteFragment extends Fragment {

    private static final String NOTE = "NOTE";

    private Note currentNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_note, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        restoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInstanceState(outState);
    }

    private void saveInstanceState(Bundle outState) {
        outState.putSerializable(NOTE, currentNote);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        Note note;

        if (savedInstanceState != null) {
            note = (Note) savedInstanceState.getSerializable(NOTE);
        } else {
            note = currentNote;
        }

        if (note != null) {
            TextView textView = (TextView) getActivity().findViewById(R.id.note_content);
            Spanned fromHtml = Html.fromHtml(note.getContent(), null, new ListTagHandler());

            textView.setText(fromHtml);
        }
    }

    public void setNote(Note note) {
        this.currentNote = note;
    }
}
