package com.java.zhengkw.data;

import android.provider.BaseColumns;

import java.util.HashMap;

public final class NewsPersistenceContract {
    private NewsPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NewsEntry implements BaseColumns {
        // list item
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_INDEX = "idx";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_PICTURE = "picture";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_READ = "read";
        //--------------------------------------------------------------
        // detail
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_JSON = "json";

        public static final HashMap<String, String> categoryMap = new HashMap<>();

        public static final HashMap<String, String> categoryDict = new HashMap<>();

        static
        {
            categoryMap.put("all", "0");
            categoryMap.put("news", "1");
            categoryMap.put("paper", "2");
            categoryMap.put("event", "3");

            categoryDict.put("0", "all");
            categoryDict.put("1", "news");
            categoryDict.put("2", "paper");
            categoryDict.put("3", "event");
        }


    }
}