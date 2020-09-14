package com.java.zhengkw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsDataSource;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.newsList.NewsListActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private NewsRepository mNewsRepository;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mNewsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        final LinearLayout tv_lin = findViewById(R.id.text_lin);
        final LinearLayout tv_hide_lin = findViewById(R.id.text_hide_lin);
        ImageView logo = findViewById(R.id.image);//图片
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_splash_position);
                tv_lin.startAnimation(animation);
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_canvas);
                tv_hide_lin.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        init();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("inited", MODE_PRIVATE);
        if (sp.getBoolean("inited", false)) {
            toggle();
        } else {
            mContext.deleteDatabase("News.db");
            mNewsRepository.loadInitialNewsList(new NewsDataSource.LoadNewsListCallback() {

                @Override
                public void onNewsListLoaded(ArrayList<News> newsList) {
                    toggle();
                }

                @Override
                public void onDataNotAvailable() {
                    finish();
                }
            });
        }
    }

    private void toggle() {
        SharedPreferences sp = getSharedPreferences("inited", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("inited", true);
        editor.apply();
        startActivity(new Intent(this, NewsListActivity.class));
        finish();
    }
}