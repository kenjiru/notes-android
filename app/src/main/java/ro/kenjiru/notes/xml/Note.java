package ro.kenjiru.notes.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import org.w3c.dom.Document;

import java.util.Date;

@XStreamAlias("note")
public class Note {
    @XStreamAsAttribute
    private float version;

    @XStreamAlias("text")
    private Document text;

    @XStreamAlias("last-change-date")
    private Date lastChangeDate;

    @XStreamAlias("last-metadata-change-date")
    private Date lastMetadataChangeDate;

    @XStreamAlias("create-date")
    private Date createDate;

    @XStreamAlias("cursor-position")
    private int cursorPosition;

    @XStreamAlias("selection-bound-position")
    private int selectionBoundPositionPosition;

    @XStreamAlias("width")
    private int width;

    @XStreamAlias("height")
    private int height;

    @XStreamAlias("x")
    private int x;

    @XStreamAlias("y")
    private int y;

    @XStreamAlias("open-on-startup")
    private boolean openOnStartup;

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

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Date getLastMetadataChangeDate() {
        return lastMetadataChangeDate;
    }

    public void setLastMetadataChangeDate(Date lastMetadataChangeDate) {
        this.lastMetadataChangeDate = lastMetadataChangeDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public int getSelectionBoundPositionPosition() {
        return selectionBoundPositionPosition;
    }

    public void setSelectionBoundPositionPosition(int selectionBoundPositionPosition) {
        this.selectionBoundPositionPosition = selectionBoundPositionPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOpenOnStartup() {
        return openOnStartup;
    }

    public void setOpenOnStartup(boolean openOnStartup) {
        this.openOnStartup = openOnStartup;
    }
}
