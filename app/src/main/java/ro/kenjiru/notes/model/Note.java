package ro.kenjiru.notes.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "notes")
public class Note extends Model implements Serializable {
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    private final int SHORT_DESCRIPTION_LENGTH = 30;

    public Note() {}

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getShortDescription() {
        int length = description.length() - 1;

        if (length > this.SHORT_DESCRIPTION_LENGTH) {
            length = this.SHORT_DESCRIPTION_LENGTH;
        }

        return description.substring(0, length);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
