package ro.kenjiru.notes.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("sync")
public class Manifest {
    @XStreamAsAttribute
    private int revision;

    @XStreamAlias("server-id")
    @XStreamAsAttribute
    private String serverId;

    @XStreamImplicit
    private List<NoteEntry> notes;

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public List<NoteEntry> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteEntry> notes) {
        this.notes = notes;
    }
}
