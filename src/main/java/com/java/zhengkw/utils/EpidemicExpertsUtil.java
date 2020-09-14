package com.java.zhengkw.utils;

import com.java.zhengkw.data.EpidemicExpert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class EpidemicExpertsUtil {
    public static ArrayList<EpidemicExpert> parseEpidemicExperts(String json) {
        ArrayList<EpidemicExpert> epidemicExperts = new ArrayList<>();
        try {
            JSONObject JsonObj = new JSONObject(json);
            JSONArray jsonArray = JsonObj.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject expertJson = jsonArray.getJSONObject(i);
                if (expertJson.getBoolean("is_passedaway")) continue;
                EpidemicExpert epidemicExpert = new EpidemicExpert();
                epidemicExpert.avatar = expertJson.getString("avatar");
                if (expertJson.getString("name_zh").isEmpty())
                    epidemicExpert.name = expertJson.getString("name");
                else epidemicExpert.name = expertJson.getString("name_zh");
                JSONObject indicesJson = expertJson.getJSONObject("indices");
                JSONObject profileJson = expertJson.getJSONObject("profile");
                epidemicExpert.activity = indicesJson.getDouble("activity");
                epidemicExpert.citations = indicesJson.getInt("citations");
                epidemicExpert.pubs = indicesJson.getInt("pubs");
                epidemicExpert.sociability = indicesJson.getDouble("sociability");
                epidemicExpert.affiliation = profileJson.getString("affiliation");
                if (profileJson.has("position"))
                    epidemicExpert.position = profileJson.getString("position");
                else epidemicExpert.position = "";
                epidemicExpert.bio = profileJson.getString("bio");
                if (profileJson.has("edu")) {
                    String edu = profileJson.getString("edu");
                    ArrayList<String> _edu = new ArrayList<>();
                    if (!edu.isEmpty()) {
                        _edu = new ArrayList<>(Arrays.asList(edu.split("\n")));
                    }
                    if (_edu.size() == 1) {
                        _edu = new ArrayList<>(Arrays.asList(edu.split("<br>")));
                    }
                    for (String a : _edu) {
                        if (!a.trim().isEmpty()) epidemicExpert.edu.add(a);
                    }
                }
                if (profileJson.has("work")) {
                    String work = profileJson.getString("work");
                    ArrayList<String> _work=new ArrayList<>();
                    if (!work.isEmpty()) {
                        _work = new ArrayList<>(Arrays.asList(work.split("\n")));
                    }
                    if (_work.size() == 1) {
                        _work = new ArrayList<>(Arrays.asList(work.split("<br>")));
                    }
                    for (String a : _work) {
                        if (!a.trim().isEmpty()) epidemicExpert.work.add(a);
                    }
                }
                epidemicExperts.add(epidemicExpert);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return epidemicExperts;
    }

    public static ArrayList<EpidemicExpert> parseEpidemicExpertsPassedAway(String json) {
        ArrayList<EpidemicExpert> epidemicExperts = new ArrayList<>();
        try {
            JSONObject JsonObj = new JSONObject(json);
            JSONArray jsonArray = JsonObj.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject expertJson = jsonArray.getJSONObject(i);
                if (!expertJson.getBoolean("is_passedaway")) continue;
                EpidemicExpert epidemicExpert = new EpidemicExpert();
                epidemicExpert.avatar = expertJson.getString("avatar");
                if (expertJson.getString("name_zh").isEmpty())
                    epidemicExpert.name = expertJson.getString("name");
                else epidemicExpert.name = expertJson.getString("name_zh");
                JSONObject indicesJson = expertJson.getJSONObject("indices");
                JSONObject profileJson = expertJson.getJSONObject("profile");
                epidemicExpert.activity = indicesJson.getDouble("activity");
                epidemicExpert.citations = indicesJson.getInt("citations");
                epidemicExpert.pubs = indicesJson.getInt("pubs");
                epidemicExpert.sociability = indicesJson.getDouble("sociability");
                epidemicExpert.affiliation = profileJson.getString("affiliation");
                if (profileJson.has("position"))
                    epidemicExpert.position = profileJson.getString("position");
                else epidemicExpert.position = "";
                epidemicExpert.bio = profileJson.getString("bio");
                if (profileJson.has("edu")) {
                    String edu = profileJson.getString("edu");
                    ArrayList<String> _edu = new ArrayList<>();
                    if (!edu.isEmpty()) {
                        _edu = new ArrayList<>(Arrays.asList(edu.split("\n")));
                    }
                    if (_edu.size() == 1) {
                        _edu = new ArrayList<>(Arrays.asList(edu.split("<br>")));
                    }
                    for (String a : _edu) {
                        if (!a.trim().isEmpty()) epidemicExpert.edu.add(a);
                    }
                }
                if (profileJson.has("work")) {
                    String work = profileJson.getString("work");
                    ArrayList<String> _work=new ArrayList<>();
                    if (!work.isEmpty()) {
                        _work = new ArrayList<>(Arrays.asList(work.split("\n")));
                    }
                    if (_work.size() == 1) {
                        _work = new ArrayList<>(Arrays.asList(work.split("<br>")));
                    }
                    for (String a : _work) {
                        if (!a.trim().isEmpty()) epidemicExpert.work.add(a);
                    }
                }
                epidemicExperts.add(epidemicExpert);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return epidemicExperts;
    }
}
