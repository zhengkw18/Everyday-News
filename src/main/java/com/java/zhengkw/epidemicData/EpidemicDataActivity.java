package com.java.zhengkw.epidemicData;

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

public class EpidemicDataActivity extends AppCompatActivity {
    private EpidemicDataPresenter mPresenter;

    private Context mContext;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemicdata);

        mContext = EpidemicDataActivity.this;
        mToolbar = findViewById(R.id.epidemicdata_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.navigation);

        ViewPager mViewPager = findViewById(R.id.epidemicdata_viewpager);
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.epidemicdata_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
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
        private EpidemicDataFragment[] fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new EpidemicDataFragment[2];
            for (int i = 0; i < 2; i++) {
                fragments[i] = new EpidemicDataFragment();
                Bundle args = new Bundle();
                args.putInt("category", i);
                fragments[i].setArguments(args);
                fragments[i].setContext(mContext);
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
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
            if (position == 0) return "全球";
            else return "中国";
        }
    }
}
