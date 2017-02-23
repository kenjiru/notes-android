package ro.kenjiru.notes.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("note")
public class NoteEntry {
    @XStreamAsAttribute
    private String id;

    @XStreamAlias("rev")
    @XStreamAsAttribute
    private int revision;

    public NoteEntry(String id, int revision) {
        this.id = id;
        this.revision = revision;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
