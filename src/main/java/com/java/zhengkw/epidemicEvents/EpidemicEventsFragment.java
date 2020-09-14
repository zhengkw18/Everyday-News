package com.java.zhengkw.epidemicEvents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class EpidemicEventsFragment extends Fragment {
    private EpidemicEventsPresenter mPresenter;

    private EpidemicEventsFragment.EpidemicEventsAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private ArrayList<News> mNewsList;

    private View root = null;

    public static EpidemicEventsFragment newInstance() {
        return new EpidemicEventsFragment();
    }

    public void setNewsList(ArrayList<News> newsList) {
        mNewsList = newsList;
    }

    public void setPicture(int position, String picture) {
        mAdapter.getItem(position).setPicture(picture);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root != null) return root;
        root = inflater.inflate(R.layout.fragment_epidemicevents, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicevents);
        Context mContext = getContext();
        mAdapter = new EpidemicEventsFragment.EpidemicEventsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return root;
    }

    public void setPresenter(EpidemicEventsPresenter presenter) {
        mPresenter = presenter;
    }

    public boolean isActive() {
        return isAdded();
    }

    private class EpidemicEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private Context context;

        public EpidemicEventsAdapter(Context _context) {
            this.inflater = LayoutInflater.from(_context);
            this.context = _context;
        }

        public News getItem(int i) {
            return mNewsList.get(i);

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
                final View view = inflater.inflate(R.layout.epidemicevents_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicEventsFragment.ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView newsTitle = view.findViewById(R.id.epidemicevents_title);
                        newsTitle.setTextColor(getResources().getColor(R.color.colorCategoryGray));
                        Intent intent = new Intent();
                        intent.setClass(getContext(), NewsPageActivity.class);
                        int position = (int) view.getTag();
                        News news = mNewsList.get(position);
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
            if (holder instanceof EpidemicEventsFragment.ItemViewHolder) {
                News news = mNewsList.get(position);
                EpidemicEventsFragment.ItemViewHolder itemHolder = (EpidemicEventsFragment.ItemViewHolder) holder;
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
                        mPresenter.getCoverPicture(context, news, itemHolder, position, EpidemicEventsFragment.this);
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
            return mNewsList.size();
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle = null;
        TextView newsIntro = null;
        ImageView newsImage = null;
        int position = -1;

        public ItemViewHolder(View view) {
            super(view);
            newsTitle = view.findViewById(R.id.epidemicevents_title);
            newsIntro = view.findViewById(R.id.epidemicevents_source);
            newsImage = view.findViewById(R.id.epidemicevents_image);
        }
    }
}
