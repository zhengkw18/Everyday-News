package com.java.zhengkw.data;

import android.os.Parcel;
import android.os.Parcelable;

public final class News implements Parcelable {

    private final String mId;

    private final String mAuthor;

    private final String mTitle;

    private final String mCategory;

    private String mPicture;

    private final String mSource;

    private final String mTime;

    private final String mUrl;


    private boolean mRead;

    private final String mContent;

    private final String mJson;

    public News(News news, boolean read) {
        mId = news.getId();
        mAuthor = news.getAuthor();
        mTitle = news.getTitle();
        mCategory = news.getCategory();
        mPicture = news.getPicture();
        mSource = news.getSource();
        mTime = news.getTime();
        mUrl = news.getUrl();
        mRead = read;
        mContent = news.getContent();
        mJson = news.getJson();
    }


    private News(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mSource = "";
        mAuthor = in.readString();
        mCategory = "";
        mPicture = "";
        mTime = in.readString();
        mUrl = "";
        mRead = false;
        mContent = "";
        mJson = "";
    }


    public News(String id, String title, String category, String time) {
        mId = id;
        mAuthor = "";
        mTitle = title;
        mCategory = category;
        mPicture = "";
        mSource = "";
        if (time.contains(":")) mTime = time.replace("/", "-");
        else mTime = time.replace("/", "-") + " 00:00:00";
        mUrl = "";
        mRead = false;
        mContent = "";
        mJson = "";
    }

    public News(String id, String author, String title, String category, String picture, String source, String time, String url, String content) {
        mId = id;
        mAuthor = author;
        mTitle = title;
        mCategory = category;
        mPicture = picture;
        mSource = source;
        if (time.contains(":")) mTime = time.replace("/", "-");
        else mTime = time.replace("/", "-") + " 00:00:00";
        mUrl = url;
        mRead = false;
        mContent = content;
        mJson = "";
    }

    public News(String id, String author, String title, String category, String picture, String source, String time, String url, String content, String json) {
        mId = id;
        mAuthor = author;
        mTitle = title;
        mCategory = category;
        mPicture = picture;
        mSource = source;
        if (time.contains(":")) mTime = time.replace("/", "-");
        else mTime = time.replace("/", "-") + " 00:00:00";
        mUrl = url;
        mRead = false;
        mContent = content;
        mJson = json;
    }

    public News(String id, String author, String title, String category, String picture, String source,
                String time, String url, boolean read, String content, String json) {
        mId = id;
        mAuthor = author;
        mTitle = title;
        mCategory = category;
        mPicture = picture;
        mSource = source;
        if (time.contains(":")) mTime = time.replace("/", "-");
        else mTime = time.replace("/", "-") + " 00:00:00";
        mUrl = url;
        mRead = read;
        mContent = content;
        mJson = json;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getPicture() {
        return mPicture;
    }

    public String getSource() {
        return mSource;
    }

    public String getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isRead() {
        return mRead;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public String getJson() {
        return mJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mTime);
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public void setRead(boolean read) {
        mRead = read;
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {

        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

}