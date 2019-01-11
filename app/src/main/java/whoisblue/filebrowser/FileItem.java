package whoisblue.filebrowser;


public class FileItem {
    public static int TYPE_FILE = 1;
    public static int TYPE_DIR = 2;

    private int type;
    private String name;
    private String path;

    public FileItem(int type, String name, String path) {
        this.type = type;
        this.name = name;
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
