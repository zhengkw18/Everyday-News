package com.java.zhengkw.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsPersistenceContract;
import com.java.zhengkw.newsPage.NewsPageActivity;
import com.java.zhengkw.utils.StringUtil;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchPresenter mPresenter;

    private SearchAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private int lastVisibleItem = 0;

    private int mPage = 0;

    private String mKeyWord = "";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView_search);
        Context mContext = getContext();
        mAdapter = new SearchFragment.SearchAdapter(mContext, new ArrayList<News>(0));
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);


        // 实现底部上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mPresenter.loadNews(mKeyWord, mPage + 1);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });
        return root;
    }

    public void setPresenter(SearchPresenter presenter) {
        mPresenter = presenter;
    }

    public void showNews(String keyWord, int page, ArrayList<News> newsList) {
        mAdapter.replaceData(newsList);
        mPage = page;
        mKeyWord = keyWord;
        mRecyclerView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }

    public void clearNews() {
        mAdapter.clearData();
        handler.sendEmptyMessage(0);
    }

    public boolean isActive() {
        return isAdded();
    }

    public void setPicture(int position, String picture) {
        mAdapter.getItem(position).setPicture(picture);
    }

    private class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<News> newsList;

        private Context context;

        public SearchAdapter(Context _context, ArrayList<News> _newsList) {
            this.inflater = LayoutInflater.from(_context);
            this.newsList = _newsList;
            this.context = _context;
        }

        public void replaceData(ArrayList<News> _newsList) {
            this.newsList.addAll(_newsList);
        }

        public void clearData() {
            this.newsList.clear();
        }

        public News getItem(int i) {
            return newsList.get(i);

        }

        @Override
        public int getItemViewType(int position) {
            if (position <= getItemCount()) return 1;
            else return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 1) {
                final View view = inflater.inflate(R.layout.search_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView newsTitle = view.findViewById(R.id.search_news_title);
                        newsTitle.setTextColor(getResources().getColor(R.color.colorCategoryGray));
                        Intent intent = new Intent();
                        intent.setClass(getContext(), NewsPageActivity.class);
                        int position = (int) view.getTag();
                        News news = newsList.get(position);
                        news.setRead(true);
                        intent.putExtra("newsId", mAdapter.getItem(position).getId());
                        startActivity(intent);
                    }
                });
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SearchFragment.ItemViewHolder) {
                News news = newsList.get(position);
                SearchFragment.ItemViewHolder itemHolder = (SearchFragment.ItemViewHolder) holder;
                itemHolder.newsTitle.setText(StringUtil.clipString(news.getTitle(), 50));
                if (news.isRead()) {
                    itemHolder.newsTitle.setTextColor(Color.rgb(158, 158, 158));
                } else
                    itemHolder.newsTitle.setTextColor(getResources().getColor(R.color.colorFont));
                itemHolder.newsIntro.setText(NewsPersistenceContract.NewsEntry.categoryDict.get(news.getCategory()) + " | " + news.getTime());
                itemHolder.position = position;
                if (news.getPicture().isEmpty()) {
                    Glide.with(context).load(R.drawable.downloading).placeholder(R.drawable.downloading).into(itemHolder.newsImage);
                    try {
                        mPresenter.getCoverPicture(context, news, itemHolder, position);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Glide.with(context).load(news.getPicture()).placeholder(R.drawable.downloading).into(itemHolder.newsImage);
                }

                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle = null;
        TextView newsIntro = null;
        ImageView newsImage = null;
        int position = -1;

        public ItemViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.search_news_title);
            newsIntro = (TextView) view.findViewById(R.id.search_news_source);
            newsImage = (ImageView) view.findViewById(R.id.search_news_image);
        }
    }
}
