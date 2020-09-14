package com.java.zhengkw.search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.java.zhengkw.R;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.ActivityUtil;


public class SearchActivity extends AppCompatActivity {
    private SearchPresenter mSearchPresenter;

    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        EditText editText = (EditText) findViewById(R.id.search_edit_text);
        editText.setHorizontallyScrolling(true);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSearchFragment.clearNews();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    mSearchFragment.clearNews();
                else
                    mSearchPresenter.loadNews(editable.toString(), 1);
            }
        });

        mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_result_frame);
        if (mSearchFragment == null) {
            mSearchFragment = SearchFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), mSearchFragment, R.id.search_result_frame);
        }
        Context mContext = SearchActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mSearchPresenter = new SearchPresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), mSearchFragment);

    }
}
