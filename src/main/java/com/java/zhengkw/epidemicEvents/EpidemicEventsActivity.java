package com.java.zhengkw.epidemicEvents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EpidemicEventsActivity extends AppCompatActivity {
    private Context mContext;

    private EpidemicEventsPresenter mPresenter;

    private ArrayList<Pair<String, ArrayList<News>>> mClusters = new ArrayList<>();

    private EpidemicEventsActivity.ViewPagerAdapter mPagerAdapter;

    private ViewPager mViewPager;

    private LoadingDialog dialog;

    private NewsRepository newsRepository;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0) {
                mPagerAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_epidemicevents);

        mContext = EpidemicEventsActivity.this;

        Toolbar mToolbar = findViewById(R.id.epidemicevents_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.navigation);

        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        NewsRepository newsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        mPresenter = new EpidemicEventsPresenter(newsRepository, mToolbar, this);

        mViewPager = findViewById(R.id.epidemicevents_viewpager);
        mPagerAdapter = new EpidemicEventsActivity.ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = findViewById(R.id.epidemicevents_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        LoadingDialog.Builder builder = new LoadingDialog.Builder(mContext)
                .setMessage("聚类计算中...")
                .setCancelable(true);
        dialog = builder.create();
        dialog.show();
        mPresenter.start();
    }

    public void setClusters(HashMap<String, ArrayList<News>> clusters) {
        for (Map.Entry<String, ArrayList<News>> entry : clusters.entrySet()) {
            mClusters.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        mPagerAdapter.show();
        dialog.dismiss();
        handler.sendEmptyMessage(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private EpidemicEventsFragment[] fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void show() {
            fragments = new EpidemicEventsFragment[mClusters.size()];
            for (int i = 0; i < mClusters.size(); i++) {
                fragments[i] = new EpidemicEventsFragment();
                fragments[i].setNewsList(mClusters.get(i).second);
                fragments[i].setPresenter(mPresenter);
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return mClusters.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mClusters.get(position).first;
        }
    }
}
