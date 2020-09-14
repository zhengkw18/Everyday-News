package com.java.zhengkw.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "News.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " ( " +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE+" UNIQUE" + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ + BOOLEAN_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_NAME_JSON + TEXT_TYPE +
                    " )";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
