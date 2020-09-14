package com.java.zhengkw.data;

import androidx.annotation.NonNull;

import com.java.zhengkw.utils.NewsDataUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.java.zhengkw.data.NewsPersistenceContract.NewsEntry.categoryDict;

public class NewsRemoteDataSource implements NewsDataSource {

    private static NewsRemoteDataSource INSTANCE;

    public static NewsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private NewsRemoteDataSource() {
    }

    @Override
    public void getNewsList(final int page, final int category, @NonNull final LoadNewsListCallback callback) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type=" + categoryDict.get(String.valueOf(category)) + "&page=" + page + "&size=10");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    ArrayList<News> newsList = NewsDataUtil.parseLastedNewsListJson(content.toString());
                    callback.onNewsListLoaded(newsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void getNews(@NonNull final String newsId, boolean isDetailed, @NonNull final GetNewsCallback callback) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/event/" + newsId);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    News news = NewsDataUtil.parseNewsDetail(content.toString());
                    callback.onNewsLoaded(news);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void getHistoryNewsList(int page, @NonNull LoadNewsListCallback callback) {

    }


    @Override
    public void unhistoryNews(@NonNull String newsId) {

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
    public void searchNews(final String keyWord, final int page, @NonNull final LoadNewsListCallback callback) {
    }

    @Override
    public void getCoverPicture(final News news, @NonNull final GetPictureCallback callback) {
        new Thread() {
            @Override
            public void run() {
                try {
                    System.setProperty("http.agent", "");
                    URL url = new URL("https://image.baidu.com/search/index?tn=baiduimage&fm=result&ie=utf-8&word=" + news.getTitle().replace(" ", "%20"));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36");
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    StringBuilder html = new StringBuilder();
                    String a = "\"thumbURL\":\"https:.+?\\.(jpg|jpeg|png|PNG)";
                    Pattern p = Pattern.compile(a);
                    while ((str = bufr.readLine()) != null) {
                        html.append(str);
                        Matcher m = p.matcher(str);
                        if (m.find()) {
                            bufr.close();
                            callback.onPictureLoaded(m.group().substring(12));
                            return;
                        }
                    }
                    bufr.close();
                } catch (Exception ignored) {
                }
                callback.onPictureNotAvailable();
            }

        }.start();
    }
}
