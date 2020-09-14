package com.java.zhengkw.epidemicGraph;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.java.zhengkw.R;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.ActivityUtil;

public class EpidemicGraphActivity extends AppCompatActivity {
    private EpidemicGraphPresenter mPresenter;

    private Context mContext;

    private Toolbar mToolbar;
    private EpidemicGraphFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epidemicgraph);

        mContext = EpidemicGraphActivity.this;
        mToolbar = findViewById(R.id.epidemicgraph_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.navigation);
        final EditText editText = (EditText) findViewById(R.id.epidemicgraph_edit_text);
        editText.setHorizontallyScrolling(true);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mPresenter.load(editText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        mFragment = (EpidemicGraphFragment) getSupportFragmentManager().findFragmentById(R.id.epidemicgraph_frame);
        if (mFragment == null) {
            mFragment = EpidemicGraphFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.epidemicgraph_frame);
        }
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mPresenter = new EpidemicGraphPresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), mFragment);
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
