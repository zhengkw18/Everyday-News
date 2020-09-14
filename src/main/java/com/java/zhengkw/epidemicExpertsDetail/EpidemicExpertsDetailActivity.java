package com.java.zhengkw.epidemicExpertsDetail;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.java.zhengkw.R;
import com.java.zhengkw.data.EpidemicExpert;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;

public class EpidemicExpertsDetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EpidemicExpert mEpidemicExpert;
    private static EpidemicExpertsDetailActivity.ViewPagerAdapter mPagerAdapter;
    private NewsRepository mNewsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemicexpertsdetail);
        Context mContext = EpidemicExpertsDetailActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mNewsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        mEpidemicExpert = getIntent().getParcelableExtra("expert");

        TextView nameView = findViewById(R.id.epidemicexpertsdetail_name);
        nameView.setText(mEpidemicExpert.name);
        TextView bioView = findViewById(R.id.epidemicexpertsdetail_bio);
        bioView.setText(Html.fromHtml(mEpidemicExpert.bio));
        bioView.setMovementMethod(ScrollingMovementMethod.getInstance());
        ImageView imageView = findViewById(R.id.epidemicexpertsdetail_image);
        Glide.with(mContext).load(mEpidemicExpert.avatar).placeholder(R.drawable.downloading).into(imageView);
        TextView activityView = findViewById(R.id.epidemicexpertsdetail_activity_content);
        activityView.setText(String.valueOf(mEpidemicExpert.activity));
        TextView affiliationView = findViewById(R.id.epidemicexpertsdetail_affiliation_content);
        affiliationView.setText(mEpidemicExpert.affiliation);
        TextView citationsView = findViewById(R.id.epidemicexpertsdetail_citations_content);
        citationsView.setText(String.valueOf(mEpidemicExpert.citations));
        TextView pubsView = findViewById(R.id.epidemicexpertsdetail_pubs_content);
        pubsView.setText(String.valueOf(mEpidemicExpert.pubs));
        TextView sociabilityView = findViewById(R.id.epidemicexpertsdetail_sociability_content);
        sociabilityView.setText(String.valueOf(mEpidemicExpert.sociability));
        TextView positionView = findViewById(R.id.epidemicexpertsdetail_position_content);
        positionView.setText(mEpidemicExpert.position);
        mToolbar = findViewById(R.id.epidemicexpertsdetail_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.navigation);
        ViewPager mViewPager = findViewById(R.id.epidemicexpertsdetail_viewpager);
        EpidemicExpertsDetailActivity.ViewPagerAdapter mPagerAdapter = new EpidemicExpertsDetailActivity.ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.epidemicexpertsdetail_tabs);
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
        EpidemicExpertsDetailFragment fragment_edu;
        EpidemicExpertsDetailFragment fragment_work;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragment_edu = new EpidemicExpertsDetailFragment();
            fragment_work = new EpidemicExpertsDetailFragment();
            fragment_edu.setEpidemicExpert(mEpidemicExpert);
            fragment_edu.setType(0);
            fragment_work.setEpidemicExpert(mEpidemicExpert);
            fragment_work.setType(1);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) return fragment_edu;
            else return fragment_work;
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
            if (position == 0) return "教育经历";
            else return "工作经历";
        }
    }
}
