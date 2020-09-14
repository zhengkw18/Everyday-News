package com.java.zhengkw.newsList;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsDataSource;
import com.java.zhengkw.data.NewsRepository;

import java.util.ArrayList;

public class NewsListPresenter {

    private NewsRepository mNewsRepository;

    private NewsListFragment mNewsView;

    private boolean mFirstLoad = true;

    public NewsListPresenter(NewsRepository newsRepository, NewsListFragment newsView) {
        mNewsRepository = newsRepository;
        mNewsView = newsView;
        mNewsView.setPresenter(this);
    }

    public void loadNews(int page, int category, boolean forceUpdate) {
        loadNewsList(page, category, forceUpdate || mFirstLoad);
        mFirstLoad = false;
    }

    private void loadNewsList(final int page, final int category, boolean forceUpdate) {
        mNewsView.setLoadingIndicator(true);
        if (!forceUpdate) {
            mNewsView.setLoadingIndicator(false);
            return;
        }

        mNewsRepository.getNewsListFromRemoteDataSource(page, category, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<>(news);

                if (!mNewsView.isActive()) {
                    return;
                }

                mNewsView.setLoadingIndicator(false);

                mNewsView.showNews(page, category, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {
                mNewsView.setLoadingIndicator(false);
            }
        });

    }

    public void start() {
        if (mNewsView.getPage() == 0)
            loadNews(1, mNewsView.getCategory(), true);
    }


    public void getCoverPicture(final Context context, final News news, final NewsListFragment.ItemViewHolder itemview, final int position) {
        mNewsRepository.getCoverPicture(news, new NewsDataSource.GetPictureCallback() {
            @Override
            public void onPictureLoaded(final String picture) {
                mNewsView.setPicture(position, picture);
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