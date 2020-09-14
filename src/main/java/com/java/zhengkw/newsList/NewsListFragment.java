package com.java.zhengkw.newsList;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.java.zhengkw.R;
import com.java.zhengkw.data.News;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsPersistenceContract;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.newsPage.NewsPageActivity;
import com.java.zhengkw.search.SearchActivity;
import com.java.zhengkw.utils.StringUtil;

import java.util.ArrayList;

public class NewsListFragment extends Fragment {

    private NewsListPresenter mPresenter;

    private NewsListAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private Context mActivityContext;

    private View root = null;

    private int lastVisibleItem = 0;

    private int mPage = 0;

    private int mCategory = 0;

    private static Bundle mBundleRecyclerViewState = null;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0) {
                mAdapter.notifyDataSetChanged();
                mAdapter.changeLoadingStatus(NewsListAdapter.LOAD_FINISH);
            }
        }
    };

    public NewsListFragment() {
    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root != null) return root;
        root = inflater.inflate(R.layout.fragment_newslist, container, false);

        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mActivityContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        NewsListPresenter presenter = new NewsListPresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), this);
        this.setPresenter(presenter);

        Bundle argumentBundle = getArguments();
        mCategory = argumentBundle.getInt("category");

        mRecyclerView = root.findViewById(R.id.recyclerView);

        mAdapter = new NewsListAdapter(getContext(), new ArrayList<News>(0));
        mRecyclerView.setAdapter(mAdapter);

        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swipeRefreshLayout1);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // 实现底部上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount() && mAdapter.getLoadingStatus() == NewsListAdapter.LOAD_FINISH) {

                    mAdapter.changeLoadingStatus(NewsListAdapter.LOAD_PULLING_UP);
                    mPresenter.loadNews(mPage + 1, mCategory, true);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });

        // 实现顶部下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPresenter.loadNews(1, mCategory, true);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public void setPresenter(NewsListPresenter presenter) {
        mPresenter = presenter;
    }

    public void setContext(Context context) {
        mActivityContext = context;
    }

    public void setPicture(int positon, String picture) {
        mAdapter.getItem(positon - 1).setPicture(picture);
    }

    public void showNews(int page, int category, ArrayList<News> newslist) {
        if (page == 1) mAdapter.clearData();
        mAdapter.replaceData(newslist);
        mPage = page;
        mCategory = category;
        mRecyclerView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }

    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.swipeRefreshLayout1);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    public boolean isActive() {
        return isAdded();
    }

    public int getCategory() {
        return mCategory;
    }

    public int getPage() {
        return mPage;
    }

    private class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<News> newslist;

        private Context context;

        private int loadingStatus;

        private static final int TYPE_SEARCH = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;

        private static final int LOAD_PULLING_UP = 0;
        private static final int LOAD_LOADING = 1;
        private static final int LOAD_FINISH = 2;

        public NewsListAdapter(Context _context, ArrayList<News> _newsList) {
            this.inflater = LayoutInflater.from(_context);
            this.newslist = _newsList;
            this.context = _context;
        }

        public void replaceData(ArrayList<News> _newsList) {
            this.newslist.addAll(_newsList);
        }

        public News getItem(int i) {
            return newslist.get(i);

        }

        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getItemCount()) return TYPE_FOOTER;
            else if (position == 0) return TYPE_SEARCH;
            else return TYPE_ITEM;
        }

        public void clearData() {
            this.newslist.clear();
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                final View view = inflater.inflate(R.layout.newslist_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView newsTitle = view.findViewById(R.id.news_title);
                        newsTitle.setTextColor(getResources().getColor(R.color.colorCategoryGray));
                        Intent intent = new Intent();
                        intent.setClass(getContext(), NewsPageActivity.class);
                        int position = (int) view.getTag();
                        News news = getItem(position - 1);
                        news.setRead(true);
                        intent.putExtra("newsId", mAdapter.getItem(position - 1).getId());
                        startActivity(intent);
                    }
                });
                return holder;
            } else if (viewType == TYPE_SEARCH) {
                final View view = inflater.inflate(R.layout.newslist_search_layout, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SearchActivity.class);
                        startActivity(intent);
                    }
                });
                return new SearchViewHolder(view);
            } else if (viewType == TYPE_FOOTER) {
                final View view = inflater.inflate(R.layout.newslist_footer_layout, parent, false);
                return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemViewHolder) {
                News news = getItem(position - 1);
                ItemViewHolder itemHolder = (ItemViewHolder) holder;
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
            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerHolder = (FooterViewHolder) holder;
                TextView text = footerHolder.loadText;
                switch (loadingStatus) {
                    case LOAD_PULLING_UP:
                        text.setText(R.string.load_text_pulling_up);
                        break;
                    case LOAD_LOADING:
                        text.setText(R.string.load_text_loading);
                        break;
                    case LOAD_FINISH:
                        text.setText(R.string.load_text_finish);
                        break;
                    default:
                        text.setText("");
                }
            }
        }


        @Override
        public int getItemCount() {
            return newslist.size() + 2;
        }

        int getLoadingStatus() {
            return loadingStatus;
        }

        void changeLoadingStatus(int newStatus) {
            loadingStatus = newStatus;
            notifyDataSetChanged();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle = null;
        TextView newsIntro = null;
        ImageView newsImage = null;
        int position = -1;

        public ItemViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsIntro = (TextView) view.findViewById(R.id.news_source);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView loadText = null;

        public FooterViewHolder(View view) {
            super(view);
            loadText = (TextView) view.findViewById(R.id.load_text);
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        public SearchViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}


