package com.java.zhengkw.newsPage;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;

public class NewsPageFragment extends Fragment {

    private NewsPagePresenter mPresenter;
    private TextView mTitleView;
    private TextView mTimeView;
    private TextView mSourceView;
    private TextView mAuthorView;
    private ImageView mCoverImageView;
    private TextView mContentView;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0) {
                showNews(mPresenter.getNews());
            }
        }
    };

    public static NewsPageFragment newInstance() {
        return new NewsPageFragment();
    }

    public NewsPageFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newspage, container, false);
        mTitleView = root.findViewById(R.id.news_page_title);
        mTimeView = root.findViewById(R.id.news_page_time);
        mSourceView = root.findViewById(R.id.news_page_source);
        mAuthorView = root.findViewById(R.id.news_page_author);
        mCoverImageView = root.findViewById(R.id.news_page_cover_image);
        mContentView = root.findViewById(R.id.news_page_content);
        return root;
    }

    public void setPresenter(NewsPagePresenter presenter) {
        mPresenter = presenter;
    }

    public void showNews(News news) {
        mTitleView.setText(news.getTitle());
        String time = news.getTime() + "  |  ";
        mTimeView.setText(time);
        String source = news.getSource() + "  ";
        mSourceView.setText(source);
        mAuthorView.setText(news.getAuthor());
        if(news.getPicture().isEmpty()){
            Glide.with(getContext()).load(R.drawable.downloading).placeholder(R.drawable.downloading).into(mCoverImageView);
        }else {
            Glide.with(getContext()).load(news.getPicture()).into(mCoverImageView);
        }
        String content = news.getContent();
        if (!news.getUrl().isEmpty()) {
            content = content + "<br> <br> " + String.format("原文链接：<a href='%s'>%s</a>", news.getUrl(), news.getUrl());
        }
        mContentView.setText(Html.fromHtml(content));
        mContentView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onGetNews() {
        handler.sendEmptyMessage(0);
    }
}
