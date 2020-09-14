package com.java.zhengkw.search;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsDataSource;
import com.java.zhengkw.data.NewsRepository;

import java.util.ArrayList;

public class SearchPresenter {
    private NewsRepository mNewsRepository;

    private SearchFragment mSearchView;

    public SearchPresenter(NewsRepository newsRepository, SearchFragment newsView) {
        mNewsRepository = newsRepository;
        mSearchView = newsView;
        mSearchView.setPresenter(this);
    }


    public void loadNews(String keyWord, int page) {
        loadNewsList(keyWord, page);
    }

    private void loadNewsList(final String keyWord, final int page) {
        mNewsRepository.searchNews(keyWord, page, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<>(news);
                if (!mSearchView.isActive()) {
                    return;
                }
                mSearchView.showNews(keyWord, page, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void getCoverPicture(final Context context, final News news, final SearchFragment.ItemViewHolder itemview, final int position) {
        mNewsRepository.getCoverPicture(news, new NewsDataSource.GetPictureCallback() {
            @Override
            public void onPictureLoaded(final String picture) {
                mSearchView.setPicture(position, picture);
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
