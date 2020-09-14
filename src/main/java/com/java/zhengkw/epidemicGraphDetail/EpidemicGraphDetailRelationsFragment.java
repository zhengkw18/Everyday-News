package com.java.zhengkw.epidemicGraphDetail;

import android.content.Context;
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
import com.java.zhengkw.data.EpidemicGraph;
import com.java.zhengkw.data.NewsRepository;

public class EpidemicGraphDetailRelationsFragment extends Fragment {
    private EpidemicGraphDetailRelationsFragment.EpidemicGraphDetailRelationsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private NewsRepository newsRepository;
    private EpidemicGraph mEpidemicGraph;

    public static EpidemicGraphDetailRelationsFragment newInstance() {
        return new EpidemicGraphDetailRelationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemicgraphdetailrelations, container, false);

        Context mContext = getContext();
        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicgraphdetail_relations);
        mAdapter = new EpidemicGraphDetailRelationsFragment.EpidemicGraphDetailRelationsAdapter(mContext);
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

    public void setEpidemicGraph(EpidemicGraph epidemicGraph) {
        mEpidemicGraph = epidemicGraph;
    }

    private class EpidemicGraphDetailRelationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private Context context;

        public EpidemicGraphDetailRelationsAdapter(Context _context) {
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
                final View view = inflater.inflate(R.layout.epidemicgraphdetail_relations_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicGraphDetailRelationsFragment.ItemViewHolderRelations(view);
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpidemicGraphDetailRelationsFragment.ItemViewHolderRelations) {
                EpidemicGraphDetailRelationsFragment.ItemViewHolderRelations itemHolder = (EpidemicGraphDetailRelationsFragment.ItemViewHolderRelations) holder;
                itemHolder.entityRelation.setText(mEpidemicGraph.relations_relation.get(position));
                itemHolder.entityLabel.setText(mEpidemicGraph.relations_label.get(position));
                if (mEpidemicGraph.relations_forward.get(position).equals("true")) {
                    Glide.with(context).load(R.drawable.rightarrow).placeholder(R.drawable.rightarrow).into(itemHolder.entityForward);
                } else {
                    Glide.with(context).load(R.drawable.leftarrow).placeholder(R.drawable.leftarrow).into(itemHolder.entityForward);
                }
                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return mEpidemicGraph.relations_relation.size();
        }

    }

    public class ItemViewHolderRelations extends RecyclerView.ViewHolder {

        TextView entityRelation = null;
        TextView entityLabel = null;
        ImageView entityForward = null;

        public ItemViewHolderRelations(View view) {
            super(view);
            entityRelation = (TextView) view.findViewById(R.id.epidemicgraphdetail_relations_relation);
            entityLabel = (TextView) view.findViewById(R.id.epidemicgraphdetail_relations_label);
            entityForward = (ImageView) view.findViewById(R.id.epidemicgraphdetail_relations_forward);
        }
    }
}
