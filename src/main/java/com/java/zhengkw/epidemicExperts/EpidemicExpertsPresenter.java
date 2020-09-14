package com.java.zhengkw.epidemicExperts;

import com.java.zhengkw.data.EpidemicExpert;
import com.java.zhengkw.utils.EpidemicExpertsUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EpidemicExpertsPresenter {

    private EpidemicExpertsFragment mEpidemicExpertsView;

    private int isPassedAway;

    public EpidemicExpertsPresenter(EpidemicExpertsFragment epidemicExpertsView, int _isPassedAway) {
        mEpidemicExpertsView = epidemicExpertsView;
        mEpidemicExpertsView.setPresenter(this);
        isPassedAway = _isPassedAway;
    }

    public void load() {
        if (mEpidemicExpertsView.isLoaded()) return;
        new Thread() {
            @Override
            public void run() {
                StringBuilder content = new StringBuilder();
                try {
                    URL url = new URL("https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    ArrayList<EpidemicExpert> epidemicExperts;
                    if (isPassedAway == 0) {
                        epidemicExperts = EpidemicExpertsUtil.parseEpidemicExperts(content.toString());
                    } else {
                        epidemicExperts = EpidemicExpertsUtil.parseEpidemicExpertsPassedAway(content.toString());
                    }
                    mEpidemicExpertsView.show(epidemicExperts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
