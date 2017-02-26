package ro.kenjiru.notes.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import org.w3c.dom.Document;

@XStreamAlias("note")
public class Note {
    @XStreamAsAttribute
    private float version;

    @XStreamAlias("text")
    private Document text;

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public Document getText() {
        return text;
    }

    public void setText(Document text) {
        this.text = text;
    }
}
