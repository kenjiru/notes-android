package ro.kenjiru.notes.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "notes")
public class Note extends Model implements Serializable {
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    private final int SHORT_DESCRIPTION_LENGTH = 30;

    public Note() {}

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getShortDescription() {
        int length = content.length() - 1;

        if (length > this.SHORT_DESCRIPTION_LENGTH) {
            length = this.SHORT_DESCRIPTION_LENGTH;
        }

        return content.substring(0, length);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
