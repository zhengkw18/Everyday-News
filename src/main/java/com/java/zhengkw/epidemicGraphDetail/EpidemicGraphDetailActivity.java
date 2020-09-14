package com.java.zhengkw.epidemicGraphDetail;

import android.content.Context;
import android.os.Bundle;
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
import com.java.zhengkw.data.EpidemicGraph;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;

public class EpidemicGraphDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EpidemicGraph mEpidemicGraph;

    private static EpidemicGraphDetailActivity.ViewPagerAdapter mPagerAdapter;

    private NewsRepository mNewsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemicgraphdetail);
        Context mContext = EpidemicGraphDetailActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mNewsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        mEpidemicGraph = getIntent().getParcelableExtra("graph");
        TextView labelView = findViewById(R.id.epidemicgraphdetail_label);
        labelView.setText(mEpidemicGraph.label);
        TextView contentView = findViewById(R.id.epidemicgraphdetail_content);
        contentView.setText(mEpidemicGraph.description);
        contentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mToolbar = findViewById(R.id.epidemicgraphdetail_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.navigation);
        ViewPager mViewPager = findViewById(R.id.epidemicgraphdetail_viewpager);
        EpidemicGraphDetailActivity.ViewPagerAdapter mPagerAdapter = new EpidemicGraphDetailActivity.ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.epidemicgraphdetail_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        ImageView imageView = findViewById(R.id.epidemicgraphdetail_image);
        if (mEpidemicGraph.img.isEmpty()) {
            Glide.with(mContext).load(R.drawable.downloading).placeholder(R.drawable.downloading).into(imageView);
        } else {
            Glide.with(mContext).load(mEpidemicGraph.img).placeholder(R.drawable.downloading).into(imageView);
        }
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
        EpidemicGraphDetailRelationsFragment fragment_relations;
        EpidemicGraphDetailPropertiesFragment fragment_properties;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragment_relations = new EpidemicGraphDetailRelationsFragment();
            fragment_properties = new EpidemicGraphDetailPropertiesFragment();
            fragment_relations.setEpidemicGraph(mEpidemicGraph);
            fragment_properties.setEpidemicGraph(mEpidemicGraph);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) return fragment_relations;
            else return fragment_properties;
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
            if (position == 0) return "关系";
            else return "属性";
        }
    }
}
