package com.sked.gdg.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gdg.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper _instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initialize(Context context) {
        _instance = new DatabaseHelper(context);
    }

    public static DatabaseHelper instance() {
        return _instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(Table.post.CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
    }

}
