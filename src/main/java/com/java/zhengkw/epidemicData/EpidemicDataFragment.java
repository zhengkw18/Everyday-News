package com.java.zhengkw.epidemicData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.java.zhengkw.R;
import com.java.zhengkw.data.EpidemicData;
import com.java.zhengkw.data.EpidemicDataPersistenceContract;
import com.java.zhengkw.data.NewsLocalDataSource;
import com.java.zhengkw.data.NewsRemoteDataSource;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.EpidemicDataUtil;
import com.java.zhengkw.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EpidemicDataFragment extends Fragment {
    private EpidemicDataPresenter mPresenter;

    private EpidemicDataFragment.EpidemicDataAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private LineChart mLineChart;

    private Context mActivityContext;

    private TextView mTextView1, mTextView2, mTextView3, mTextView4;

    private View root = null;

    private int mCategory;

    private boolean loaded = false;

    private LoadingDialog dialog;

    public static EpidemicDataFragment newInstance() {
        return new EpidemicDataFragment();
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
        mPresenter.start(mCategory);
    }

    public void setContext(Context context) {
        mActivityContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root != null) return root;
        root = inflater.inflate(R.layout.fragment_epidemicdata, container, false);
        Bundle argumentBundle = getArguments();
        mCategory = argumentBundle.getInt("category");
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mActivityContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        NewsRepository newsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        EpidemicDataPresenter presenter = new EpidemicDataPresenter(this, newsRepository);
        this.setPresenter(presenter);
        mRecyclerView = root.findViewById(R.id.recyclerView_epidemicdata);
        Context mContext = getContext();
        mAdapter = new EpidemicDataFragment.EpidemicDataAdapter(mContext, mCategory);
        mRecyclerView.setAdapter(mAdapter);
        mLineChart = root.findViewById(R.id.chart1);
        initChart();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTextView1 = root.findViewById(R.id.epidemicdata_header1);
        mTextView2 = root.findViewById(R.id.epidemicdata_header2);
        mTextView3 = root.findViewById(R.id.epidemicdata_header3);
        mTextView4 = root.findViewById(R.id.epidemicdata_header4);
        mTextView1.setVisibility(View.INVISIBLE);
        mTextView2.setVisibility(View.INVISIBLE);
        mTextView3.setVisibility(View.INVISIBLE);
        mTextView4.setVisibility(View.INVISIBLE);
        LoadingDialog.Builder builder = new LoadingDialog.Builder(mActivityContext)
                .setMessage("加载中...")
                .setCancelable(true);
        dialog = builder.create();
        dialog.show();
        return root;
    }

    private void initChart() {
        mLineChart.setNoDataText("");
        mLineChart.setScaleEnabled(false);
        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setEnabled(false);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(EpidemicDataUtil.numDays - 0.5f);
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= EpidemicDataUtil.numDays; i++) {
            list.add("第" + i + "天");
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(list));
    }

    public void setPresenter(EpidemicDataPresenter presenter) {
        mPresenter = presenter;
    }

    private void showChart(EpidemicData.EpidemicDataRow row) {
        List<Entry> entries_confirmed = new ArrayList<>();
        List<Entry> entries_cured = new ArrayList<>();
        List<Entry> entries_dead = new ArrayList<>();
        for (int i = 0; i < EpidemicDataUtil.numDays; i++) {
            EpidemicData.EpidemicDataSingle single = row.data.get(row.data.size() - EpidemicDataUtil.numDays + i);
            entries_confirmed.add(new Entry(i, single.confirmed));
            entries_cured.add(new Entry(i, single.cured));
            entries_dead.add(new Entry(i, single.dead));
        }
        LineDataSet set_confirmed = new LineDataSet(entries_confirmed, "感染");
        set_confirmed.setColor(Color.BLACK);
        set_confirmed.setValueTextSize(9f);
        set_confirmed.setLineWidth(3f);
        set_confirmed.setCircleColor(Color.BLACK);
        set_confirmed.setCircleRadius(4f);
        set_confirmed.setCircleHoleRadius(2f);
        LineDataSet set_cured = new LineDataSet(entries_cured, "治愈");
        set_cured.setColor(Color.GREEN);
        set_cured.setValueTextSize(9f);
        set_cured.setLineWidth(3f);
        set_cured.setCircleColor(Color.GREEN);
        set_cured.setCircleRadius(4f);
        set_cured.setCircleHoleRadius(2f);
        LineDataSet set_dead = new LineDataSet(entries_dead, "死亡");
        set_dead.setColor(Color.RED);
        set_dead.setValueTextSize(9f);
        set_dead.setLineWidth(3f);
        set_dead.setCircleColor(Color.RED);
        set_dead.setCircleRadius(4f);
        set_dead.setCircleHoleRadius(2f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set_confirmed);
        dataSets.add(set_cured);
        dataSets.add(set_dead);
        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        Description description = new Description();
        String region;
        if (mCategory == 0) {
            region = EpidemicDataPersistenceContract.CountryEntry.idToDisplay.get(row.id);
        } else {
            region = EpidemicDataPersistenceContract.ChinaEntry.idToDisplay.get(row.id);
        }
        description.setText(region + "近" + EpidemicDataUtil.numDays + "天疫情数据");
        description.setTextSize(20);
        description.setTextAlign(Paint.Align.CENTER);
        description.setPosition(550, 60);
        mLineChart.setDescription(description);
        mLineChart.invalidate();
    }

    public void show(EpidemicData epidemicdata) {
        loaded = true;
        Collections.sort(epidemicdata.mData, new Comparator<EpidemicData.EpidemicDataRow>() {
            @Override
            public int compare(EpidemicData.EpidemicDataRow t1, EpidemicData.EpidemicDataRow t2) {
                int confirmed1 = t1.data.get(t1.data.size() - 1).confirmed, confirmed2 = t2.data.get(t1.data.size() - 1).confirmed;
                if (confirmed1 < confirmed2) return 1;
                if (confirmed1 > confirmed2) return -1;
                return 0;
            }
        });
        dialog.dismiss();
        showChart(epidemicdata.mData.get(0));
        mAdapter.setData(epidemicdata);
        mRecyclerView.setVisibility(View.VISIBLE);
        mTextView1.post(new Runnable() {
            @Override
            public void run() {
                mTextView1.setVisibility(View.VISIBLE);
            }
        });
        mTextView2.post(new Runnable() {
            @Override
            public void run() {
                mTextView2.setVisibility(View.VISIBLE);
            }
        });
        mTextView3.post(new Runnable() {
            @Override
            public void run() {
                mTextView3.setVisibility(View.VISIBLE);
            }
        });
        mTextView4.post(new Runnable() {
            @Override
            public void run() {
                mTextView4.setVisibility(View.VISIBLE);
            }
        });
        handler.sendEmptyMessage(0);
    }

    public boolean isActive() {
        return isAdded();
    }

    public boolean isLoaded() {
        return loaded;
    }

    private class EpidemicDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;
        private int mCategory;
        private EpidemicData epidemicData = new EpidemicData();

        public EpidemicDataAdapter(Context _context, int category) {
            this.inflater = LayoutInflater.from(_context);
            this.mCategory = category;
        }

        public void setData(EpidemicData _epidemicData) {
            this.epidemicData = _epidemicData;
        }

        public EpidemicData.EpidemicDataRow getItem(int i) {
            return epidemicData.mData.get(i);

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
                final View view = inflater.inflate(R.layout.epidemicdata_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new EpidemicDataFragment.ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        EpidemicData.EpidemicDataRow row = epidemicData.mData.get(position);
                        showChart(row);
                    }
                });
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpidemicDataFragment.ItemViewHolder) {
                EpidemicData.EpidemicDataRow row = epidemicData.mData.get(position);
                EpidemicData.EpidemicDataSingle single = row.data.get(row.data.size() - 1);
                EpidemicDataFragment.ItemViewHolder itemHolder = (EpidemicDataFragment.ItemViewHolder) holder;
                if (mCategory == 0) {
                    itemHolder.region.setText(EpidemicDataPersistenceContract.CountryEntry.idToDisplay.get(row.id));

                } else {
                    itemHolder.region.setText(EpidemicDataPersistenceContract.ChinaEntry.idToDisplay.get(row.id));
                }
                itemHolder.confirmed.setText(String.valueOf(single.confirmed));
                itemHolder.cured.setText(String.valueOf(single.cured));
                itemHolder.dead.setText(String.valueOf(single.dead));
                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return epidemicData.mData.size();
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView region = null;
        TextView confirmed = null;
        TextView cured = null;
        TextView dead = null;

        public ItemViewHolder(View view) {
            super(view);
            region = view.findViewById(R.id.epidemicdata_region);
            confirmed = view.findViewById(R.id.epidemicdata_confirmed);
            cured = view.findViewById(R.id.epidemicdata_cured);
            dead = view.findViewById(R.id.epidemicdata_dead);
        }
    }
}
