package com.java.zhengkw.data;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.zhengkw.cluster.Clusterer;
import com.java.zhengkw.utils.NewsDataUtil;
import com.java.zhengkw.utils.NewsListJsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class NewsRepository implements NewsDataSource {
    private static NewsRepository INSTANCE = null;

    private final NewsDataSource mNewsRemoteDataSource;

    private final NewsDataSource mNewsLocalDataSource;

    private NewsRepository(@NonNull NewsDataSource newsRemoteDataSource,
                           @NonNull NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;
    }

    public static NewsRepository getInstance(NewsDataSource newsRemoteDataSource,
                                             NewsDataSource newsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsRemoteDataSource, newsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getNewsList(final int page, final int category, @NonNull final LoadNewsListCallback callback) {
        mNewsLocalDataSource.getNewsList(page, category, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {
                getNewsListFromRemoteDataSource(page, category, callback);
            }
        });
    }

    public void getNewsListFromRemoteDataSource(final int page, final int category, @NonNull final LoadNewsListCallback callback) {
        mNewsRemoteDataSource.getNewsList(page, category, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                for (final News news : newsList) {
                    mNewsLocalDataSource.getNews(news.getId(), false, new GetNewsCallback() {

                        @Override
                        public void onNewsLoaded(News news2) {
                            if (news2.isRead()) news.setRead(true);
                        }

                        @Override
                        public void onDataNotAvailable() {
                        }
                    });
                }
                mNewsLocalDataSource.saveNewsList(newsList);
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    public void getCoverPicture(final News news, @NonNull final GetPictureCallback callback) {
        if (!news.getPicture().isEmpty()) {
            callback.onPictureLoaded(news.getPicture());
            return;
        }
        mNewsLocalDataSource.getCoverPicture(news, new GetPictureCallback() {
            @Override
            public void onPictureLoaded(String picture) {
                news.setPicture(picture);
                callback.onPictureLoaded(picture);
            }

            @Override
            public void onPictureNotAvailable() {
                getCoverPictureFromRemoteDataSource(news, callback);
            }
        });
    }

    public void getCoverPictureFromRemoteDataSource(final News news, @NonNull final GetPictureCallback callback) {
        mNewsRemoteDataSource.getCoverPicture(news, new GetPictureCallback() {
            @Override
            public void onPictureLoaded(String picture) {
                news.setPicture(picture);
                mNewsLocalDataSource.updateNewsPicture(news);
                callback.onPictureLoaded(picture);
            }

            @Override
            public void onPictureNotAvailable() {
                callback.onPictureNotAvailable();
            }
        });
    }

    @Override
    public void getNews(@NonNull final String newsId, boolean isDetailed, @NonNull final GetNewsCallback callback) {
        mNewsLocalDataSource.getNews(newsId, isDetailed, new GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                getNewsFromRemoteDataSource(newsId, callback);
            }
        });
    }

    public void getNewsFromRemoteDataSource(@NonNull String newsId, @NonNull final GetNewsCallback callback) {
        mNewsRemoteDataSource.getNews(newsId, true, new GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                mNewsLocalDataSource.updateNewsDetail(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getHistoryNewsList(int page, @NonNull final LoadNewsListCallback callback) {
        mNewsLocalDataSource.getHistoryNewsList(page, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void unhistoryNews(@NonNull String newsId) {
        //if empty, clear all history
        mNewsLocalDataSource.unhistoryNews(newsId);
    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {

    }

    @Override
    public void updateNewsDetail(@NonNull News news) {

    }

    @Override
    public void updateNewsPicture(@NonNull News news) {

    }

    @Override
    public void searchNews(String keyWord, int page, @NonNull final LoadNewsListCallback callback) {
        mNewsLocalDataSource.searchNews(keyWord, page, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    public void loadInitialNewsList(@NonNull final LoadNewsListCallback callback) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/events.json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    NewsListJsonObject list = objectMapper.readValue(url, NewsListJsonObject.class);
                    ArrayList<News> newsList = NewsDataUtil.parseWholeNewsList(list);
                    mNewsLocalDataSource.saveNewsList(newsList);
                    callback.onNewsListLoaded(newsList);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onDataNotAvailable();
                }
            }
        }.start();
    }

    public void getClusters(@NonNull final GetEventsClusterCallback callback) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type=event&page=1&size=1000");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    ArrayList<News> newsList = NewsDataUtil.parseLastedNewsListJson(content.toString());
                    HashMap<String, ArrayList<News>> clusters = Clusterer.cluster(newsList);
                    callback.onClusterLoaded(clusters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
