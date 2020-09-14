package com.java.zhengkw.history;

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

public class HistoryActivity extends AppCompatActivity {
    private HistoryPresenter mPresenter;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Context mContext = HistoryActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        NewsRepository newsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        mToolbar = findViewById(R.id.history_toolbar);

        HistoryFragment historyFragment = (HistoryFragment) getSupportFragmentManager().findFragmentById(R.id.history_frame);
        if (historyFragment == null) {
            // Create the fragment
            historyFragment = HistoryFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), historyFragment, R.id.history_frame);
        }
        mPresenter = new HistoryPresenter(newsRepository, historyFragment, mToolbar, this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.navigation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.history_toolbar_clear) {
                    mPresenter.clearHistory();
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
