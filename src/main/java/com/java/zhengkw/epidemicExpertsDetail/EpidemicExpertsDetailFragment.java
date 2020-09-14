package com.java.zhengkw.epidemicExpertsDetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhengkw.R;
import com.java.zhengkw.data.EpidemicExpert;

public class EpidemicExpertsDetailFragment extends Fragment {
    private EpidemicExpertsDetailFragment.EpidemicExpertsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private EpidemicExpert mEpidemicExpert;
    private int type;

    public static EpidemicExpertsDetailFragment newInstance() {
        return new EpidemicExpertsDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemicexpertsdetail, container, false);
        Context mContext = getContext();
        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicexpertsdetail);
        mAdapter = new EpidemicExpertsDetailFragment.EpidemicExpertsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        return root;
    }

    public boolean isActive() {
        return isAdded();
    }

    public void setEpidemicExpert(EpidemicExpert epidemicExpert) {
        mEpidemicExpert = epidemicExpert;
    }

    public void setType(int type) {
        this.type = type;
    }

    private class EpidemicExpertsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private Context context;

        public EpidemicExpertsAdapter(Context _context) {
            this.inflater = LayoutInflater.from(_context);
            this.context = _context;
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
                final View view = inflater.inflate(R.layout.epidemicexpertsdetail_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicExpertsDetailFragment.ItemViewHolder(view);
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpidemicExpertsDetailFragment.ItemViewHolder) {
                EpidemicExpertsDetailFragment.ItemViewHolder itemHolder = (EpidemicExpertsDetailFragment.ItemViewHolder) holder;
                if (type == 0) itemHolder.text.setText(mEpidemicExpert.edu.get(position));
                else itemHolder.text.setText(mEpidemicExpert.work.get(position));
                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            if (type == 0) return mEpidemicExpert.edu.size();
            else return mEpidemicExpert.work.size();
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView text = null;

        public ItemViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.epidemicexpertsdetail_text);
        }
    }
}
