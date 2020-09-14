package com.java.zhengkw.epidemicEvents;

import android.content.Context;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsDataSource;
import com.java.zhengkw.data.NewsRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class EpidemicEventsPresenter {
    private NewsRepository mNewsRepository;
    private EpidemicEventsActivity mActivity;

    public EpidemicEventsPresenter(NewsRepository newsRepository, Toolbar toolbar, EpidemicEventsActivity activity) {
        mNewsRepository = newsRepository;
        mActivity = activity;
    }

    public void start() {
        mNewsRepository.getClusters(new NewsDataSource.GetEventsClusterCallback() {
            @Override
            public void onClusterLoaded(HashMap<String, ArrayList<News>> clusters) {
                mActivity.setClusters(clusters);
            }

            @Override
            public void onClusterNotAvaliable() {

            }
        });
    }


    public void getCoverPicture(final Context context, final News news, final EpidemicEventsFragment.ItemViewHolder itemview, final int position, final EpidemicEventsFragment fragment) {
        mNewsRepository.getCoverPicture(news, new NewsDataSource.GetPictureCallback() {
            @Override
            public void onPictureLoaded(final String picture) {
                fragment.setPicture(position, picture);
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
