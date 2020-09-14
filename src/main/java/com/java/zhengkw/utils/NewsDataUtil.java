package com.java.zhengkw.utils;

import com.java.zhengkw.data.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.java.zhengkw.data.NewsPersistenceContract.NewsEntry.categoryMap;

public class NewsDataUtil {
    public static ArrayList<News> parseLastedNewsListJson(String json) {
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONObject listJsonObj = new JSONObject(json);
            JSONArray listJsonArray = listJsonObj.getJSONArray("data");
            for (int i = 0; i < listJsonArray.length(); i++) {
                JSONObject newsJsonObj = (JSONObject) listJsonArray.get(i);
                String content = newsJsonObj.getString("content");
                String id = newsJsonObj.getString("_id");
                String category = categoryMap.get(newsJsonObj.getString("type"));
                StringBuilder author = new StringBuilder();
                if (newsJsonObj.has("authors")) {
                    JSONArray authors = newsJsonObj.getJSONArray("authors");
                    for (int j = 0; j < authors.length() - 1; j++) {
                        author.append(authors.getJSONObject(j).getString("name")).append(", ");
                    }
                    author.append(authors.getJSONObject(authors.length() - 1).getString("name"));
                }
                String seg_text = "";
                if (category == "3") {
                    seg_text = newsJsonObj.getString("seg_text");
                }
                String source = "";
                if (newsJsonObj.has("source")) source = newsJsonObj.getString("source");
                String time = newsJsonObj.getString("time");
                String title = newsJsonObj.getString("title");
                String url = "";
                if (newsJsonObj.has("urls")) {
                    JSONArray urls = newsJsonObj.getJSONArray("urls");
                    if (urls.length() > 0) url = urls.getString(0);
                }
                newsList.add(new News(id, author.toString(), title, category, "", source, time, url, content, seg_text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    public static News parseNewsDetail(String json) {
        News news = null;
        try {
            JSONObject newsJsonObj = new JSONObject(json);
            newsJsonObj = newsJsonObj.getJSONObject("data");
            String content = newsJsonObj.getString("content");
            String id = newsJsonObj.getString("_id");
            String category = categoryMap.get(newsJsonObj.getString("type"));
            StringBuilder author = new StringBuilder();
            if (newsJsonObj.has("authors")) {
                JSONArray authors = newsJsonObj.getJSONArray("authors");
                for (int j = 0; j < authors.length() - 1; j++) {
                    author.append(authors.getJSONObject(j).getString("name")).append(", ");
                }
                author.append(authors.getJSONObject(authors.length() - 1).getString("name"));
            }
            String source = "";
            if (newsJsonObj.has("source")) source = newsJsonObj.getString("source");
            String time = newsJsonObj.getString("time");
            String title = newsJsonObj.getString("title");
            String url = "";
            if (newsJsonObj.has("urls")) {
                JSONArray urls = newsJsonObj.getJSONArray("urls");
                if (urls.length() > 0) url = urls.getString(0);
            }
            news = new News(id, author.toString(), title, category, "", source, time, url, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public static ArrayList<News> parseWholeNewsList(NewsListJsonObject list) {
        ArrayList<News> newsList = new ArrayList<>();
        for (int i = 0; i < list.getDatas().size(); i++) {
            NewsJsonObject newsJsonObject = list.getDatas().get(i);
            if (categoryMap.keySet().contains(newsJsonObject.getType()))
                newsList.add(new News(newsJsonObject.get_id(), newsJsonObject.getTitle(), categoryMap.get(newsJsonObject.getType()), newsJsonObject.getTime()));
        }
        return newsList;
    }
}
