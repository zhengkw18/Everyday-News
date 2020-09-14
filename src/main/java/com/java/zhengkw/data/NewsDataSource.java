package com.java.zhengkw.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public interface NewsDataSource {


    interface LoadNewsListCallback {

        void onNewsListLoaded(ArrayList<News> newsList);

        void onDataNotAvailable();


    }

    interface GetPictureCallback {

        void onPictureLoaded(String picture);

        void onPictureNotAvailable();
    }

    interface GetNewsCallback {

        void onNewsLoaded(News news);

        void onDataNotAvailable();
    }

    interface GetEventsClusterCallback {
        void onClusterLoaded(HashMap<String, ArrayList<News>> clusters);

        void onClusterNotAvaliable();
    }

    void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback);

    void getNews(@NonNull String newsId, boolean isDetailed, @NonNull GetNewsCallback callback);

    void getHistoryNewsList(int page, @NonNull LoadNewsListCallback callback);

    void unhistoryNews(@NonNull String newsId);

    void saveNewsList(@NonNull ArrayList<News> newsList);

    void updateNewsDetail(@NonNull News news);

    void updateNewsPicture(@NonNull News news);

    void searchNews(String keyWord, int page, @NonNull LoadNewsListCallback callback);

    void getCoverPicture(News news, @NonNull GetPictureCallback callback);
}
