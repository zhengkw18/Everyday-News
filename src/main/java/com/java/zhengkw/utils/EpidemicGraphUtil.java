package com.java.zhengkw.utils;

import com.java.zhengkw.data.EpidemicGraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class EpidemicGraphUtil {
    public static ArrayList<EpidemicGraph> parseEpidemicGraph(String json) {
        ArrayList<EpidemicGraph> epidemicGraphs = new ArrayList<>();
        try {
            JSONObject JsonObj = new JSONObject(json);
            JSONArray JsonArray = JsonObj.getJSONArray("data");
            int length = JsonArray.length();
            for (int i = 0; i < length; i++) {
                EpidemicGraph epidemicGraph = new EpidemicGraph();
                JSONObject jsonObject = JsonArray.getJSONObject(i);
                epidemicGraph.label = jsonObject.getString("label");
                if (jsonObject.isNull("img")) {
                    epidemicGraph.img = "";
                } else {
                    epidemicGraph.img = jsonObject.getString("img");
                }
                jsonObject = jsonObject.getJSONObject("abstractInfo");
                epidemicGraph.description = jsonObject.getString("baidu");
                jsonObject = jsonObject.getJSONObject("COVID");
                JSONObject propertiesJson = jsonObject.getJSONObject("properties");
                for (Iterator<String> it = propertiesJson.keys(); it.hasNext(); ) {
                    String name = it.next();
                    String val = propertiesJson.getString(name);
                    epidemicGraph.properties_name.add(name);
                    epidemicGraph.properties_content.add(val);
                }
                JSONArray relationsJson = jsonObject.getJSONArray("relations");
                for (int j = 0; j < relationsJson.length(); j++) {
                    JSONObject relation = relationsJson.getJSONObject(j);
                    epidemicGraph.relations_relation.add(relation.getString("relation"));
                    epidemicGraph.relations_label.add(relation.getString("label"));
                    boolean forward = relation.getBoolean("forward");
                    if (forward) epidemicGraph.relations_forward.add("true");
                    else epidemicGraph.relations_forward.add("false");
                }
                epidemicGraphs.add(epidemicGraph);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return epidemicGraphs;
    }
}
