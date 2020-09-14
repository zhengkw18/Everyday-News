package com.java.zhengkw.newsPage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.NewsShareActivity;

public class NewsPagePresenter {

    private NewsPageFragment mView;

    private NewsRepository mRespository;

    private News mNews;

    private Context mContext;

    public NewsPagePresenter(@NonNull NewsRepository respository, @NonNull NewsPageFragment NewsView, Toolbar toolbar, NewsPageActivity activity, Context context) {
        mRespository = respository;
        mView = NewsView;
        mView.setPresenter(this);
        mContext = context;
    }

    public void start(String newsId) {
        mRespository.getNews(newsId, true, new NewsDataSource.GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                mNews = news;
                mRespository.getCoverPicture(news, new NewsDataSource.GetPictureCallback() {
                    @Override
                    public void onPictureLoaded(String picture) {
                        mNews.setPicture(picture);
                    }

                    @Override
                    public void onPictureNotAvailable() {
                    }
                });
                mView.onGetNews();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public News getNews() {
        return mNews;
    }

    public void showShareDialog() {
        String url = mNews.getUrl();
        String title = mNews.getTitle();
        String picture = mNews.getPicture();
        String text = mNews.getContent();
        NewsShareActivity a;

        a = new NewsShareActivity(mContext);
        a.showShare(title, text, url, picture);
    }

    public void setContext(Context context) {
    }
}
