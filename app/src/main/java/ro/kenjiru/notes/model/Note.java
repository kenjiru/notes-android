package ro.kenjiru.notes.model;

public class Note {
    private String title;
    private String description;
    private final int SHORT_DESCRIPTION_LENGTH = 30;

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
