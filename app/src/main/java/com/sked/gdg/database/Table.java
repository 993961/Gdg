package com.sked.gdg.database;

/**
 * Created by Sanjeet on 25-Feb-15.
 */
public class Table {
    public static final String ID = "_id";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";
    public static final String DELETED = "deleted";
    public static final String SYNC_ID = "sync_id";

    public static class post {
        public static final String POST_ID = "id",
                E_TAG = "etag",
                TITLE = "title",
                URL = "url",
                PROVIDER = "provider",
                PROVIDER_TYPE = "provider_type",
                POST_TYPE = "post_type";
        public static final String TABLE_NAME = "post";
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME
                + " ( "
                + ID + " integer primary key autoincrement, "
                + POST_ID + " text unique, "
                + E_TAG + " text, "
                + TITLE + " text, "
                + URL + " text, "
                + PROVIDER + " text, "
                + POST_TYPE + " integer , "
                + PROVIDER_TYPE + " integer , "
                + SYNC_ID + " text, "
                + DELETED + " integer default 0, "
                + CREATED + " datetime default current_timestamp, "
                + UPDATED + " datetime default current_timestamp )";
    }
}

