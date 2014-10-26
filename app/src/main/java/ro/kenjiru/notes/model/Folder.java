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

    public Folder() {

    }

    public Folder(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Folder)) {
            return false;
        }

        Folder that = (Folder) other;

        return this.name.equals(that.getName()) &&
                this.getId() == that.getId();
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
