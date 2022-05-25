package ai.yantranet.smartagent.model;

public class Note {
    public static final String TABLE_NAME = "fetch";

    public static final String COLUMN_NUMBER = "Number";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";


    public static final String COLUMN_SIZE = "sizeInBytes";
    public static final String COLUMN_PATH = "cdn_path";
    private int id;
    private String name;
    private String type;
    private String sizeInBytes;
    private String cdn_path;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " TEXT NOT NULL UNIQUE,"

                    + COLUMN_NAME + " TEXT,"

                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_SIZE + " INTEGER,"
                    + COLUMN_PATH + " TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String name, String type, String sizeInBytes, String cdn_path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sizeInBytes = sizeInBytes;
        this.cdn_path = cdn_path;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(String sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getCdn_path() {
        return cdn_path;
    }

    public void setCdn_path(String cdn_path) {
        this.cdn_path = cdn_path;
    }
}
