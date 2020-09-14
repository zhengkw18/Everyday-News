package com.java.zhengkw.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.java.zhengkw.utils.DatabaseManager;

import java.util.ArrayList;

public class NewsLocalDataSource implements NewsDataSource {
    private static NewsLocalDataSource INSTANCE;

    private static DatabaseManager mDbHelper;

    private NewsLocalDataSource(Context context) {
        NewsDbHelper _mDbHelper = new NewsDbHelper(context);
        mDbHelper = DatabaseManager.getInstance(_mDbHelper);
    }

    public static NewsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback) {
        ArrayList<News> newsList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        if (category == 0) {
            cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " ORDER BY " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_INDEX + " LIMIT 10 OFFSET " + (page * 10 - 10), null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE "
                    + NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY + " = '" + category
                    + "' ORDER BY " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_INDEX + " LIMIT 10 OFFSET " + (page * 10 - 10), null);
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID));
                String mcategory = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY));
                String picture = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE));
                String author = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR));
                String source = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE));
                String time = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME));
                String title = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL));
                boolean read = (cursor.getInt(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ)) == 1);
                String content = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CONTENT));
                String json = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_JSON));
                News news = new News(id, author, title, mcategory, picture, source, time, url, read, content, json);
                newsList.add(news);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
        if (newsList.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onNewsListLoaded(newsList);
        }
    }

    @Override
    public void getNews(@NonNull String newsId, boolean isDetailed, @NonNull GetNewsCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE "
                + NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " = '" + newsId + "'", null);
        News news = null;
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                boolean read = (cursor.getInt(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ)) == 1);
                if (!read && isDetailed) {
                    cursor.close();
                    mDbHelper.closeDatabase();
                    callback.onDataNotAvailable();
                    return;
                }
                String id = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID));
                String mcategory = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY));
                String picture = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE));
                String author = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR));
                String source = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE));
                String time = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME));
                String title = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL));
                String content = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CONTENT));
                String json = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_JSON));
                news = new News(id, author, title, mcategory, picture, source, time, url, read, content, json);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
        if (news == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onNewsLoaded(news);
        }
    }

    @Override
    public void getHistoryNewsList(int page, @NonNull LoadNewsListCallback callback) {
        ArrayList<News> newsList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE "
                + NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ + " = 1 ORDER BY " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME + " DESC LIMIT 10 OFFSET " + (page * 10 - 10), null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID));
                String mcategory = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY));
                String picture = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE));
                String author = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR));
                String source = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE));
                String time = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME));
                String title = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL));
                boolean read = cursor.getInt(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ)) == 1;
                String content = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CONTENT));
                String json = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_JSON));
                News news = new News(id, author, title, mcategory, picture, source, time, url, read, content, json);
                newsList.add(news);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
        if (newsList.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onNewsListLoaded(newsList);
        }
    }

    @Override
    public void unhistoryNews(@NonNull String newsId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (newsId.isEmpty()) {
            //if empty, clear all history
            db.execSQL("UPDATE " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " SET " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ + " = 0 WHERE " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ + " = 1");
            mDbHelper.closeDatabase();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ, 0);
        String selection = NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {newsId};
        db.update(NewsPersistenceContract.NewsEntry.TABLE_NAME, values, selection, selectionArgs);
        mDbHelper.closeDatabase();
    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < newsList.size(); i++) {
            News news = newsList.get(i);
            ContentValues values = new ContentValues();
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID, news.getId());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY, news.getCategory());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE, news.getPicture());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME, news.getTime());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL, news.getUrl());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ, 0);
            values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_JSON, news.getJson());
            db.insertWithOnConflict(NewsPersistenceContract.NewsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        mDbHelper.closeDatabase();
    }

    @Override
    public void searchNews(String keyWord, int page, @NonNull LoadNewsListCallback callback) {
        ArrayList<News> newsList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE "
                + NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE + " LIKE '%" + keyWord
                + "%' ORDER BY " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME + " DESC LIMIT 10 OFFSET " + (page * 10 - 10), null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID));
                String mcategory = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CATEGORY));
                String picture = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE));
                String author = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR));
                String source = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE));
                String time = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TIME));
                String title = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL));
                boolean read = cursor.getInt(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ)) == 1;
                String content = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CONTENT));
                String json = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_JSON));
                News news = new News(id, author, title, mcategory, picture, source, time, url, read, content, json);
                newsList.add(news);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
        if (newsList.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onNewsListLoaded(newsList);
        }
    }

    @Override
    public void getCoverPicture(News news, @NonNull GetPictureCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE "
                + NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " = '" + news.getId() + "'", null);
        String picture = null;
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                picture = cursor.getString(cursor.getColumnIndex(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
        if (picture == null || picture.isEmpty()) {
            callback.onPictureNotAvailable();
        } else {
            callback.onPictureLoaded(picture);
        }
    }

    public void updateNewsDetail(@NonNull News news) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{news.getId()});
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
                values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
                values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_URL, news.getUrl());
                values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_CONTENT, news.getContent());
                values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_READ, 1);
                String selection = NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
                String[] selectionArgs = {news.getId()};
                db.update(NewsPersistenceContract.NewsEntry.TABLE_NAME, values, selection, selectionArgs);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
    }

    public void updateNewsPicture(@NonNull News news) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " WHERE " + NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{news.getId()});
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(NewsPersistenceContract.NewsEntry.COLUMN_NAME_PICTURE, news.getPicture());
                String selection = NewsPersistenceContract.NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
                String[] selectionArgs = {news.getId()};
                db.update(NewsPersistenceContract.NewsEntry.TABLE_NAME, values, selection, selectionArgs);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        mDbHelper.closeDatabase();
    }
}
