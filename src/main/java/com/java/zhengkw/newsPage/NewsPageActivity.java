package com.java.zhengkw.newsPage;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.java.zhengkw.R;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.ActivityUtil;

public class NewsPageActivity extends AppCompatActivity {

    private NewsPagePresenter mPresenter;

    private Toolbar mToolbar;

    private String mNewsId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspage);
        Context mContext = NewsPageActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        NewsRepository newsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        mNewsId = getIntent().getStringExtra("newsId");
        mToolbar = findViewById(R.id.news_page_toolbar);

        NewsPageFragment newsPageFragment = (NewsPageFragment) getSupportFragmentManager().findFragmentById(R.id.page_frame);
        if (newsPageFragment == null) {
            // Create the fragment
            newsPageFragment = NewsPageFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), newsPageFragment, R.id.page_frame);
        }
        mPresenter = new NewsPagePresenter(newsRepository, newsPageFragment, mToolbar, this, mContext);
        mPresenter.setContext(mContext);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.navigation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start(mNewsId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_page, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.news_page_toolbar_share) {
                    mPresenter.showShareDialog();
                }
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
