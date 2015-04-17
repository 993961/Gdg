package com.sked.gdg.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sanjeet on 26-Oct-14.
 */
public class DBOHelper {
    public static long insert(String tableName, ContentValues contentValues) {
        SQLiteDatabase sqlDB = null;
        try {
            sqlDB = DatabaseHelper.instance().getWritableDatabase();
            assert sqlDB != null;
            long row_id = sqlDB.insert(tableName, null, contentValues);
            if (row_id > 0) {
                return row_id;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            assert sqlDB != null;
            //  sqlDB.close();

        } finally {
            assert sqlDB != null;
            //  sqlDB.close();
        }
        sqlDB.close();
        return -1;
    }


    public static long update(String tableName, ContentValues contentValues, String update_id) {
        SQLiteDatabase sqlDB = null;
        try {
            sqlDB = DatabaseHelper.instance().getWritableDatabase();
            long row_id = sqlDB.update(tableName, contentValues,
                    Table.ID + "=" + update_id, null);
            if (row_id > 0) {
                return row_id;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (sqlDB != null) {
                sqlDB.close();
            }

        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
        }
        if (sqlDB != null) {
            sqlDB.close();
        }
        return -1;
    }

    public static long update(String tableName, ContentValues contentValues, String update_column, String update_id) {
        SQLiteDatabase sqlDB = null;
        try {
            sqlDB = DatabaseHelper.instance().getWritableDatabase();
            long row_id = sqlDB.update(tableName, contentValues,
                    update_column + "=\'" + update_id + "\'", null);
            if (row_id > 0) {
                return row_id;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (sqlDB != null) {
                sqlDB.close();
            }

        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
        }
        if (sqlDB != null) {
            sqlDB.close();
        }
        return -1;
    }


    public static long delete(String tableName, String columnIdName, String ColumnIdValue) {
        SQLiteDatabase sqlDB = null;
        try {
            sqlDB = DatabaseHelper.instance().getWritableDatabase();
            long row_id = sqlDB.delete(tableName,
                    columnIdName + " = " + ColumnIdValue, null);
            if (row_id > 0) {
                return row_id;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            assert sqlDB != null;
            sqlDB.close();

        } finally {
            assert sqlDB != null;
            sqlDB.close();
        }
        sqlDB.close();
        return -1;
    }
}
