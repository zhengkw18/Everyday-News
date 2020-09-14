package com.java.zhengkw.newsList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.java.zhengkw.R;
import com.java.zhengkw.category.CategoryActivity;
import com.java.zhengkw.data.NewsPersistenceContract;
import com.java.zhengkw.epidemicData.EpidemicDataActivity;
import com.java.zhengkw.epidemicEvents.EpidemicEventsActivity;
import com.java.zhengkw.epidemicExperts.EpidemicExpertsActivity;
import com.java.zhengkw.epidemicGraph.EpidemicGraphActivity;
import com.java.zhengkw.history.HistoryActivity;

import java.util.ArrayList;

public class NewsListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;

    private static ArrayList<Integer> selectedcategories = new ArrayList<>();

    private static ViewPagerAdapter mPagerAdapter;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newslist);

        mContext = NewsListActivity.this;

        Toolbar mToolbar = findViewById(R.id.newslist_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.newslist_category) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        ViewPager mViewPager = (ViewPager) findViewById(R.id.newslist_viewpager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.newslist_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setHomeAsUpIndicator(R.mipmap.category);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.getMenu().getItem(0).setChecked(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.close();
        selectedcategories = new ArrayList<>();
        selectedcategories.add(0);
        SharedPreferences sp = getSharedPreferences("category", MODE_PRIVATE);
        if (sp.getBoolean("news", true)) selectedcategories.add(1);
        if (sp.getBoolean("paper", true)) selectedcategories.add(2);
        if (sp.getBoolean("event", true)) selectedcategories.add(3);
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newslist, menu);
        final MenuItem item = menu.findItem(R.id.newslist_category);
        item.getActionView().findViewById(R.id.menu_newslist_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch (id) {
            case R.id.nav_history:
                intent.setClass(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_epidemicdata:
                intent.setClass(this, EpidemicDataActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_epidemicgraph:
                intent.setClass(this, EpidemicGraphActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_epidemicevents:
                intent.setClass(this, EpidemicEventsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_epidemicexperts:
                intent.setClass(this, EpidemicExpertsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private NewsListFragment[] fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new NewsListFragment[4];
            for (int i = 0; i < 4; i++) {
                fragments[i] = new NewsListFragment();
                Bundle args = new Bundle();
                args.putInt("category", i);
                fragments[i].setArguments(args);
                fragments[i].setContext(mContext);
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[selectedcategories.get(position)];
        }

        @Override
        public int getCount() {
            return selectedcategories.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String id = String.valueOf(selectedcategories.get(position));
            return NewsPersistenceContract.NewsEntry.categoryDict.get(id);
        }
    }


}
