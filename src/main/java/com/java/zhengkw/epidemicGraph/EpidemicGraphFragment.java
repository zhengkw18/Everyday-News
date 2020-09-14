package com.java.zhengkw.epidemicGraph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.java.zhengkw.data.EpidemicGraph;
import com.java.zhengkw.epidemicGraphDetail.EpidemicGraphDetailActivity;
import com.java.zhengkw.utils.StringUtil;

import java.util.ArrayList;

public class EpidemicGraphFragment extends Fragment {
    private EpidemicGraphPresenter mPresenter;

    private EpidemicGraphFragment.EpidemicGraphAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    public static EpidemicGraphFragment newInstance() {
        return new EpidemicGraphFragment();
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
        View root = inflater.inflate(R.layout.fragment_epidemicgraph, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicgraph);
        Context mContext = getContext();
        mAdapter = new EpidemicGraphFragment.EpidemicGraphAdapter(mContext, new ArrayList<EpidemicGraph>(0));
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        return root;
    }

    public void setPresenter(EpidemicGraphPresenter presenter) {
        mPresenter = presenter;
    }

    public void show(ArrayList<EpidemicGraph> epidemicGraphs) {
        mAdapter.setData(epidemicGraphs);
        mRecyclerView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }

    public boolean isActive() {
        return isAdded();
    }

    private class EpidemicGraphAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<EpidemicGraph> epidemicGraphs;

        private Context context;

        public EpidemicGraphAdapter(Context _context, ArrayList<EpidemicGraph> _epidemicGraphs) {
            this.inflater = LayoutInflater.from(_context);
            this.epidemicGraphs = _epidemicGraphs;
            this.context = _context;
        }

        public void setData(ArrayList<EpidemicGraph> _epidemicGraphs) {
            this.epidemicGraphs = _epidemicGraphs;
        }

        public EpidemicGraph getItem(int i) {
            return epidemicGraphs.get(i);

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
                final View view = inflater.inflate(R.layout.epidemicgraph_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicGraphFragment.ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), EpidemicGraphDetailActivity.class);
                        int position = (int) view.getTag();
                        EpidemicGraph epidemicGraph = epidemicGraphs.get(position);
                        intent.putExtra("graph", epidemicGraph);
                        startActivity(intent);
                    }
                });
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpidemicGraphFragment.ItemViewHolder) {
                EpidemicGraph epidemicGraph = epidemicGraphs.get(position);
                EpidemicGraphFragment.ItemViewHolder itemHolder = (EpidemicGraphFragment.ItemViewHolder) holder;
                itemHolder.newsTitle.setText(epidemicGraph.label);
                if(StringUtil.getLength(epidemicGraph.label)>20){
                    itemHolder.newsIntro.setText(StringUtil.clipString(epidemicGraph.description, 48));
                }else{
                    itemHolder.newsIntro.setText(StringUtil.clipString(epidemicGraph.description, 64));
                }
                itemHolder.position = position;
                if (epidemicGraph.img.isEmpty()) {
                    Glide.with(context).load(R.drawable.downloading).placeholder(R.drawable.downloading).into(itemHolder.newsImage);
                } else {
                    Glide.with(context).load(epidemicGraph.img).placeholder(R.drawable.downloading).into(itemHolder.newsImage);
                }

                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return epidemicGraphs.size();
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle = null;
        TextView newsIntro = null;
        ImageView newsImage = null;
        int position = -1;

        public ItemViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.epidemicgraph_label);
            newsIntro = (TextView) view.findViewById(R.id.epidemicgraph_content);
            newsImage = (ImageView) view.findViewById(R.id.epidemicgraph_image);
        }
    }
}
