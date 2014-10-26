package ro.kenjiru.notes.model;

public class SpecialFolder extends Folder {
    private long specialId;

    public static long ALL_FOLDERS = -1;
    public static long NO_FOLDER = -2;

    private SpecialFolder(String name, long specialId) {
        super(name);
        this.specialId = specialId;
    }

    public static SpecialFolder createAllFolders() {
        return new SpecialFolder("All Folders", ALL_FOLDERS);
    }

    public static SpecialFolder createNoFolder() {
        return new SpecialFolder("No Folder", NO_FOLDER);
    }

    public static boolean isSpecialFolder(long folderId) {
        return folderId < 0;
    }

    public long getSpecialId() {
        return specialId;
    }
}
