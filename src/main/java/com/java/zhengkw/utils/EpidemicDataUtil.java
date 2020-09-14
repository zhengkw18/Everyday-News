package com.java.zhengkw.utils;

import com.java.zhengkw.data.EpidemicData;
import com.java.zhengkw.data.EpidemicDataPersistenceContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EpidemicDataUtil {
    public static int numDays = 5;

    public static EpidemicData parseEpidemicDataCountry(String json) {
        EpidemicData data = new EpidemicData();
        try {
            JSONObject JsonObj = new JSONObject(json);
            for (int i = 0; i < EpidemicDataPersistenceContract.CountryEntry.NUM_COUNTRY; i++) {
                JSONObject rowJson = JsonObj.getJSONObject(EpidemicDataPersistenceContract.CountryEntry.idToName.get(i));
                JSONArray rowArray = rowJson.getJSONArray("data");
                int length = rowArray.length();
                EpidemicData epidemicData = new EpidemicData();
                EpidemicData.EpidemicDataRow row = epidemicData.new EpidemicDataRow();
                for (int j = 0; j < numDays; j++) {
                    JSONArray singlearray = rowArray.getJSONArray(length - numDays + j);
                    EpidemicData.EpidemicDataSingle single = epidemicData.new EpidemicDataSingle();
                    single.confirmed = singlearray.getInt(0);
                    single.cured = singlearray.getInt(2);
                    single.dead = singlearray.getInt(3);
                    row.data.add(single);
                }

                row.id = i;
                data.mData.add(row);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static EpidemicData parseEpidemicDataChina(String json) {
        EpidemicData data = new EpidemicData();
        try {
            JSONObject JsonObj = new JSONObject(json);
            for (int i = 0; i < EpidemicDataPersistenceContract.ChinaEntry.NUM_CHINA; i++) {
                JSONObject rowJson = JsonObj.getJSONObject(EpidemicDataPersistenceContract.ChinaEntry.idToName.get(i));
                JSONArray rowArray = rowJson.getJSONArray("data");
                int length = rowArray.length();
                EpidemicData epidemicData = new EpidemicData();
                EpidemicData.EpidemicDataRow row = epidemicData.new EpidemicDataRow();
                for (int j = 0; j < numDays; j++) {
                    JSONArray singlearray = rowArray.getJSONArray(length - numDays + j);
                    EpidemicData.EpidemicDataSingle single = epidemicData.new EpidemicDataSingle();
                    single.confirmed = singlearray.getInt(0);
                    single.cured = singlearray.getInt(2);
                    single.dead = singlearray.getInt(3);
                    row.data.add(single);
                }
                row.id = i;
                data.mData.add(row);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
