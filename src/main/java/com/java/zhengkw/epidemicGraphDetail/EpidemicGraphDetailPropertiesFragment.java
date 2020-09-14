package com.java.zhengkw.epidemicGraphDetail;

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
import com.java.zhengkw.data.EpidemicGraph;

public class EpidemicGraphDetailPropertiesFragment extends Fragment {

    private EpidemicGraphDetailPropertiesFragment.EpidemicGraphDetailPropertiesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private EpidemicGraph mEpidemicGraph;

    public static EpidemicGraphDetailPropertiesFragment newInstance() {
        return new EpidemicGraphDetailPropertiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_epidemicgraphdetailproperties, container, false);

        Context mContext = getContext();
        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicgraphdetail_properties);
        mAdapter = new EpidemicGraphDetailPropertiesFragment.EpidemicGraphDetailPropertiesAdapter(mContext);
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

    private class EpidemicGraphDetailPropertiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;

        public EpidemicGraphDetailPropertiesAdapter(Context _context) {
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
                final View view = inflater.inflate(R.layout.epidemicgraphdetail_propertied_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicGraphDetailPropertiesFragment.ItemViewHolderProperties(view);
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpidemicGraphDetailPropertiesFragment.ItemViewHolderProperties) {
                EpidemicGraphDetailPropertiesFragment.ItemViewHolderProperties itemHolder = (EpidemicGraphDetailPropertiesFragment.ItemViewHolderProperties) holder;
                itemHolder.entityName.setText(mEpidemicGraph.properties_name.get(position));
                itemHolder.entityContent.setText(mEpidemicGraph.properties_content.get(position));
                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return mEpidemicGraph.properties_name.size();
        }

    }

    public class ItemViewHolderProperties extends RecyclerView.ViewHolder {

        TextView entityName = null;
        TextView entityContent = null;

        public ItemViewHolderProperties(View view) {
            super(view);
            entityName = (TextView) view.findViewById(R.id.epidemicgraphdetail_properties_name);
            entityContent = (TextView) view.findViewById(R.id.epidemicgraphdetail_properties_content);
        }
    }
}
