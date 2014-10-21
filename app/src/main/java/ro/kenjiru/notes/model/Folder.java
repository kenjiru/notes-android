package ro.kenjiru.notes.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

@Table(name = "folders")
public class Folder extends Model implements Serializable {
    @Column(name = "name")
    private String name;

    public static long ANY_FOLDER = -1;

    public Folder() {
    }

    public Folder(String name) {
        this.name = name;
    }

    public List<Note> notes() {
        return getMany(Note.class, "folder");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
