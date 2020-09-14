package com.java.zhengkw.epidemicExperts;

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
import com.java.zhengkw.data.EpidemicExpert;
import com.java.zhengkw.epidemicExpertsDetail.EpidemicExpertsDetailActivity;

import java.util.ArrayList;

public class EpidemicExpertsFragment extends Fragment {
    private EpidemicExpertsPresenter mPresenter;

    private EpidemicExpertsFragment.EpidemicExpertsAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private int isPassedAway;

    private boolean isLoaded = false;

    private View root;

    public static EpidemicExpertsFragment newInstance() {
        return new EpidemicExpertsFragment();
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
    public void onResume() {
        super.onResume();
        mPresenter.load();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root != null) return root;
        root = inflater.inflate(R.layout.fragment_epidemicexperts, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicexperts);
        Context mContext = getContext();
        mAdapter = new EpidemicExpertsFragment.EpidemicExpertsAdapter(mContext, new ArrayList<EpidemicExpert>(0));
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPresenter = new EpidemicExpertsPresenter(this, isPassedAway);
        return root;
    }

    public void setIsPassedAway(int _isPassedAway) {
        isPassedAway = _isPassedAway;
    }

    public void setPresenter(EpidemicExpertsPresenter presenter) {
        mPresenter = presenter;
    }

    public void show(ArrayList<EpidemicExpert> epidemicExperts) {
        isLoaded = true;
        mAdapter.setData(epidemicExperts);
        mRecyclerView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }

    public boolean isActive() {
        return isAdded();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    private class EpidemicExpertsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<EpidemicExpert> epidemicExperts;

        private Context context;

        public EpidemicExpertsAdapter(Context _context, ArrayList<EpidemicExpert> _epidemicExperts) {
            this.inflater = LayoutInflater.from(_context);
            this.epidemicExperts = _epidemicExperts;
            this.context = _context;
        }

        public void setData(ArrayList<EpidemicExpert> _epidemicExperts) {
            this.epidemicExperts = _epidemicExperts;
        }

        public EpidemicExpert getItem(int i) {
            return epidemicExperts.get(i);

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
                final View view = inflater.inflate(R.layout.epidemicexperts_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicExpertsFragment.ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), EpidemicExpertsDetailActivity.class);
                        int position = (int) view.getTag();
                        EpidemicExpert epidemicExpert = epidemicExperts.get(position);
                        intent.putExtra("expert", epidemicExpert);
                        startActivity(intent);
                    }
                });
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpidemicExpertsFragment.ItemViewHolder) {
                EpidemicExpert epidemicExpert = epidemicExperts.get(position);
                EpidemicExpertsFragment.ItemViewHolder itemHolder = (EpidemicExpertsFragment.ItemViewHolder) holder;
                itemHolder.expertName.setText(epidemicExpert.name);
                itemHolder.expertPosition.setText(epidemicExpert.position);
                itemHolder.expertAffiliation.setText(epidemicExpert.affiliation);
                Glide.with(context).load(epidemicExpert.avatar).placeholder(R.drawable.downloading).into(itemHolder.expertImage);
                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return epidemicExperts.size();
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView expertName = null;
        TextView expertPosition = null;
        TextView expertAffiliation = null;
        ImageView expertImage = null;

        public ItemViewHolder(View view) {
            super(view);
            expertName = (TextView) view.findViewById(R.id.epidemicexperts_name);
            expertPosition = (TextView) view.findViewById(R.id.epidemicexperts_position);
            expertAffiliation = (TextView) view.findViewById(R.id.epidemicexperts_affiliation);
            expertImage = (ImageView) view.findViewById(R.id.epidemicexperts_image);
        }
    }
}
