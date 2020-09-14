package com.java.zhengkw.epidemicExperts;

import android.content.Context;
import android.os.Bundle;
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
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;

public class EpidemicExpertsActivity extends AppCompatActivity {

    private Context mContext;

    private NewsRepository mNewsRepository;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemicexperts);

        mContext = EpidemicExpertsActivity.this;
        mToolbar = findViewById(R.id.epidemicexperts_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.navigation);

        ViewPager mViewPager = findViewById(R.id.epidemicexperts_viewpager);
        EpidemicExpertsActivity.ViewPagerAdapter mPagerAdapter = new EpidemicExpertsActivity.ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.epidemicexperts_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mNewsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
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
        private EpidemicExpertsFragment fragment1;
        private EpidemicExpertsFragment fragment2;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragment1 = new EpidemicExpertsFragment();
            fragment1.setIsPassedAway(0);
            fragment2 = new EpidemicExpertsFragment();
            fragment2.setIsPassedAway(1);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) return fragment1;
            else return fragment2;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "高关注学者";
            else return "追忆学者";
        }
    }
}
