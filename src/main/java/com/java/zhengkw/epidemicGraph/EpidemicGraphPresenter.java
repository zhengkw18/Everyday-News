package com.java.zhengkw.epidemicGraph;

import com.java.zhengkw.data.EpidemicGraph;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.EpidemicGraphUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EpidemicGraphPresenter {
    private NewsRepository mNewsRepository;

    private EpidemicGraphFragment mEpidemicGraphView;

    public EpidemicGraphPresenter(NewsRepository newsRepository, EpidemicGraphFragment epidemicGraphView) {
        mNewsRepository = newsRepository;
        mEpidemicGraphView = epidemicGraphView;
        mEpidemicGraphView.setPresenter(this);
    }

    public void load(final String keyWord) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=" + keyWord.replace(" ", "%20"));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    ArrayList<EpidemicGraph> epidemicGraphs = EpidemicGraphUtil.parseEpidemicGraph(content.toString());
                    mEpidemicGraphView.show(epidemicGraphs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
