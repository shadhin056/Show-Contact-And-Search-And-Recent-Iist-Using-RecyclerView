package com.example.contactlistshowandsearchinlistview.SQLite;

public class SQLiteData {
    public static final String TABLE_NAME = "recent_contact";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "name";
    public static final String COLUMN_PRIORITY = "priority";

    private Integer id;
    private String number;
    private Integer name;
    private Integer image;
    private Integer priority;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + COLUMN_NUMBER + " TEXT,"+ COLUMN_NAME + " TEXT,"+ COLUMN_IMAGE + " TEXT,"
                    + COLUMN_PRIORITY + " INTEGER"
                    + ")";

    public SQLiteData() {
    }

    public SQLiteData(Integer id, String number, Integer priority) {
        this.id = id;
        this.number = number;
        this.priority = priority;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnNumber() {
        return COLUMN_NUMBER;
    }

    public static String getColumnPriority() {
        return COLUMN_PRIORITY;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }
}
