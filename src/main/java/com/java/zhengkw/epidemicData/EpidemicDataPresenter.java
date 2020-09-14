package com.java.zhengkw.epidemicData;


import com.java.zhengkw.data.EpidemicData;
import com.java.zhengkw.data.NewsRepository;
import com.java.zhengkw.utils.EpidemicDataUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EpidemicDataPresenter {
    private EpidemicDataFragment mEpidemicDataView;
    private NewsRepository mNewsRepository;

    public EpidemicDataPresenter(EpidemicDataFragment epidemicDataView, NewsRepository newsRepository) {
        mEpidemicDataView = epidemicDataView;
        mNewsRepository = newsRepository;
        mEpidemicDataView.setPresenter(this);
    }

    public void start(int category) {
        if (!mEpidemicDataView.isLoaded())
            load(category);
    }

    private void load(final int category) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    EpidemicData data;
                    if (category == 0) {
                        data = EpidemicDataUtil.parseEpidemicDataCountry(content.toString());
                    } else {
                        data = EpidemicDataUtil.parseEpidemicDataChina(content.toString());
                    }
                    mEpidemicDataView.show(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
