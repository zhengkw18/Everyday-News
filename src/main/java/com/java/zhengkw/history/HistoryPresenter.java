package com.java.zhengkw.history;

import android.content.Context;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsDataSource;
import com.java.zhengkw.data.NewsRepository;

import java.util.ArrayList;

public class HistoryPresenter {
    private NewsRepository mNewsRepository;
    private HistoryFragment mHistoryView;

    public HistoryPresenter(NewsRepository newsRepository, HistoryFragment newsView, Toolbar toolbar, HistoryActivity activity) {
        mNewsRepository = newsRepository;
        mHistoryView = newsView;
        mHistoryView.setPresenter(this);
    }

    public void start() {
        if (mHistoryView.getPage() == 0)
            loadNews(1);
    }

    public void loadNews(int page) {
        loadNewsList(page);
    }

    public void clearHistory() {
        mNewsRepository.unhistoryNews("");
        mHistoryView.clearNews();
    }

    private void loadNewsList(final int page) {
        mNewsRepository.getHistoryNewsList(page, new NewsDataSource.LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<>(news);
                if (!mHistoryView.isActive()) {
                    return;
                }
                mHistoryView.showNews(page, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }

    public void getCoverPicture(final Context context, final News news, final HistoryFragment.ItemViewHolder itemview, final int position) {
        mNewsRepository.getCoverPicture(news, new NewsDataSource.GetPictureCallback() {
            @Override
            public void onPictureLoaded(final String picture) {
                mHistoryView.setPicture(position, picture);
                if (itemview.position == position) {
                    itemview.newsImage.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(context).load(picture).placeholder(R.drawable.downloading).into(itemview.newsImage);
                        }
                    });
                }
            }

            @Override
            public void onPictureNotAvailable() {
                if (itemview.position == position) {
                    itemview.newsImage.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(context).load(R.drawable.downloading).placeholder(R.drawable.downloading).into(itemview.newsImage);
                        }
                    });
                }

            }
        });
    }
}
