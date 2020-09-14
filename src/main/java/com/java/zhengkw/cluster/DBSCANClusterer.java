package com.java.zhengkw.cluster;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DBSCANClusterer<V> {

    private double epsilon = 1f;

    private int minimumNumberOfClusterMembers = 2;

    private DistanceMetric<V> metric = null;

    private ArrayList<V> inputValues = null;

    private HashSet<V> visitedPoints = new HashSet<V>();

    public DBSCANClusterer(final Collection<V> inputValues, int minNumElements, double maxDistance, DistanceMetric<V> metric) {
        setInputValues(inputValues);
        setMinimalNumberOfMembersForCluster(minNumElements);
        setMaximalDistanceOfClusterMembers(maxDistance);
        setDistanceMetric(metric);
    }

    public void setDistanceMetric(final DistanceMetric<V> metric) {
        this.metric = metric;
    }

    public void setInputValues(final Collection<V> collection) {
        this.inputValues = new ArrayList<V>(collection);
    }

    public void setMinimalNumberOfMembersForCluster(final int minimalNumberOfMembers) {
        this.minimumNumberOfClusterMembers = minimalNumberOfMembers;
    }

    public void setMaximalDistanceOfClusterMembers(final double maximalDistance) {
        this.epsilon = maximalDistance;
    }

    private ArrayList<Pair<Integer, V>> getNeighbours(final V inputValue) {
        ArrayList<Pair<Integer, V>> neighbours = new ArrayList<>();
        for (int i = 0; i < inputValues.size(); i++) {
            V candidate = inputValues.get(i);
            if (metric.calculateDistance(inputValue, candidate) <= epsilon) {
                neighbours.add(new Pair<>(i, candidate));
            }
        }
        return neighbours;
    }

    private ArrayList<Pair<Integer, V>> mergeRightToLeftCollection(final ArrayList<Pair<Integer, V>> neighbours1,
                                                                   final ArrayList<Pair<Integer, V>> neighbours2) {
        for (int i = 0; i < neighbours2.size(); i++) {
            Pair<Integer, V> tempPt = neighbours2.get(i);
            if (!neighbours1.contains(tempPt)) {
                neighbours1.add(tempPt);
            }
        }
        return neighbours1;
    }

    public ArrayList<HashMap<Integer, V>> performClustering() {

        ArrayList<HashMap<Integer, V>> resultList = new ArrayList<>();
        visitedPoints.clear();

        ArrayList<Pair<Integer, V>> neighbours;
        int index = 0;

        while (inputValues.size() > index) {
            V p = inputValues.get(index);
            if (!visitedPoints.contains(p)) {
                visitedPoints.add(p);
                neighbours = getNeighbours(p);

                if (neighbours.size() >= minimumNumberOfClusterMembers) {
                    int ind = 0;
                    while (neighbours.size() > ind) {
                        Pair<Integer, V> r = neighbours.get(ind);
                        if (!visitedPoints.contains(r.second)) {
                            visitedPoints.add(r.second);
                            ArrayList<Pair<Integer, V>> individualNeighbours = getNeighbours(r.second);
                            if (individualNeighbours.size() >= minimumNumberOfClusterMembers) {
                                neighbours = mergeRightToLeftCollection(neighbours, individualNeighbours);
                            }
                        }
                        ind++;
                    }
                    HashMap<Integer, V> result = new HashMap<>();
                    for (Pair<Integer, V> pair : neighbours) {
                        result.put(pair.first, pair.second);
                    }
                    resultList.add(result);
                }
            }
            index++;
        }
        return resultList;
    }

}