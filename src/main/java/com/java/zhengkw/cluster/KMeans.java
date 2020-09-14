package com.java.zhengkw.cluster;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class KMeans {
    int cluterNum;
    List<Pair<Integer, ArrayList<Double>>> points = new ArrayList<>();
    List<ArrayList<Double>> centerPoints = new ArrayList<>();
    HashMap<Integer, List<Pair<Integer, ArrayList<Double>>>> clusters = new HashMap<>();

    public KMeans(List<Pair<Integer, ArrayList<Double>>> points, int cluterNum) {
        this.cluterNum = cluterNum;
        this.points = points;
        for (int i = 0; i < cluterNum; i++) {
            centerPoints.add(points.get(i).second);
            clusters.put(i, new ArrayList<Pair<Integer, ArrayList<Double>>>());
        }
    }

    public ArrayList<HashMap<Integer, ArrayList<Double>>> doKMeans() {
        double err = Integer.MAX_VALUE;
        while (err > 0.01 * cluterNum) {
            for (int key : clusters.keySet()) {
                List<Pair<Integer, ArrayList<Double>>> list = clusters.get(key);
                list.clear();
                clusters.put(key, list);
            }
            for (int i = 0; i < points.size(); i++) {
                dispatchPointToCluster(points.get(i), centerPoints);
            }
            err = getClusterCenterPoint(centerPoints, clusters);
        }
        ArrayList<HashMap<Integer, ArrayList<Double>>> result = new ArrayList<>();
        for (List<Pair<Integer, ArrayList<Double>>> val : clusters.values()) {
            HashMap<Integer, ArrayList<Double>> map = new HashMap<>();
            for (Pair<Integer, ArrayList<Double>> pair : val) {
                map.put(pair.first, pair.second);
            }
            result.add(map);
        }
        return result;
    }

    public void dispatchPointToCluster(Pair<Integer, ArrayList<Double>> point, List<ArrayList<Double>> centerPoints) {
        int index = 0;
        double tmpMinDistance = Double.MAX_VALUE;
        for (int i = 0; i < centerPoints.size(); i++) {
            double distance = calDistance(point.second, centerPoints.get(i));
            if (distance < tmpMinDistance) {
                tmpMinDistance = distance;
                index = i;
            }
        }
        List<Pair<Integer, ArrayList<Double>>> list = clusters.get(index);
        list.add(point);
        clusters.put(index, list);
    }

    public double getClusterCenterPoint(List<ArrayList<Double>> centerPoints, HashMap<Integer, List<Pair<Integer, ArrayList<Double>>>> clusters) {
        double error = 0;
        for (int i = 0; i < centerPoints.size(); i++) {
            ArrayList<Double> tmpCenterPoint = centerPoints.get(i);
            Double[] center = new Double[tmpCenterPoint.size()];
            Arrays.fill(center, (double) 0);
            List<Pair<Integer, ArrayList<Double>>> lists = clusters.get(i);

            for (int k = 0; k < tmpCenterPoint.size(); k++) {
                for (int j = 0; j < lists.size(); j++) {
                    center[k] += lists.get(j).second.get(k);
                }
                center[k] /= lists.size();
            }
            for (int k = 0; k < tmpCenterPoint.size(); k++) {
                error += Math.abs(center[k] - tmpCenterPoint.get(k));
            }
            ArrayList<Double> centerlist = new ArrayList<>();
            Collections.addAll(centerlist, center);
            centerPoints.set(i, centerlist);
        }
        return error;
    }

    public double calDistance(ArrayList<Double> point1, ArrayList<Double> point2) {
        double sum = 0;
        for (int i = 0; i < point1.size(); i++) {
            double dist = point1.get(i) - point2.get(i);
            sum += dist * dist;
        }
        return sum;
    }
}