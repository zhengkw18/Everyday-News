package com.java.zhengkw.cluster;

import android.util.Pair;

import com.java.zhengkw.data.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clusterer {
    public static final int MAX_FEATURES = 100;

    public static final double EPS = 0.7;

    public static final int NUM_SAMPLES = 5;

    public static final int NUM_CLUSTERS = 20;

    public static final String remove_chars = "[`-~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]cov19";

    public static HashMap<String, ArrayList<News>> cluster(final ArrayList<News> news) {
        HashMap<String, Integer> tf_all = new HashMap<>();
        int[] num_of_words = new int[news.size()];
        for (int i = 0; i < news.size(); i++) {
            String[] words = news.get(i).getJson().split(" ");
            for (String word : words) {
                word = word.trim();
                if (remove_chars.contains(word)) continue;
                if (word.length() > 1) {
                    num_of_words[i] = num_of_words[i] + 1;
                    if (tf_all.containsKey(word)) {
                        tf_all.put(word, tf_all.get(word) + 1);
                    } else {
                        tf_all.put(word, 1);
                    }
                }
            }
        }
        //提取词频较高的词
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tf_all.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> t1, Map.Entry<String, Integer> t2) {
                return -t1.getValue().compareTo(t2.getValue());
            }
        });
        ArrayList<String> bagofwords = new ArrayList<>();
        int cnt = 0;
        for (Map.Entry<String, Integer> entry : list) {
            bagofwords.add(entry.getKey());
            cnt++;
            if (cnt >= MAX_FEATURES) break;
        }
        double[][] tf = new double[news.size()][bagofwords.size()];
        int[][] tf_count = new int[news.size()][bagofwords.size()];
        int[] df = new int[bagofwords.size()];
        for (int i = 0; i < news.size(); i++) {
            for (int j = 0; j < bagofwords.size(); j++) {
                int _count = count(news.get(i).getJson(), bagofwords.get(j));
                tf_count[i][j] = _count;
                if (_count > 0) df[j] = df[j] + 1;
                tf[i][j] = (double) _count / num_of_words[i];
            }
        }
        for (int i = 0; i < news.size(); i++) {
            for (int j = 0; j < bagofwords.size(); j++) {
                tf[i][j] = tf[i][j] * (Math.log((double) (news.size()) / (df[j])) + 1);
            }
        }


//        ArrayList<ArrayList<Double>> tf_idf = new ArrayList<>();
//        for (int i = 0; i < news.size(); i++) {
//            ArrayList<Double> point = new ArrayList<>();
//            double sum = 0;
//            for (int j = 0; j < bagofwords.size(); j++) {
//                point.add(tf[i][j]);
//                sum += tf[i][j] * tf[i][j];
//            }
//            sum = Math.sqrt(sum);
//            for (int j = 0; j < bagofwords.size(); j++) {
//                point.set(j, point.get(j) / sum);
//            }
//            tf_idf.add(point);
//        }
//        DBSCANClusterer<ArrayList<Double>> clusterer = new DBSCANClusterer<>(tf_idf, NUM_SAMPLES, EPS, new DistanceMetric<ArrayList<Double>>() {
//            @Override
//            public double calculateDistance(ArrayList<Double> val1, ArrayList<Double> val2) {
//                double dist = 0;
//                for (int i = 0; i < val1.size(); i++) {
//                    dist += (val1.get(i) - val2.get(i)) * (val1.get(i) - val2.get(i));
//                }
//                return dist;
//            }
//        });
//        ArrayList<HashMap<Integer, ArrayList<Double>>> result = clusterer.performClustering();


        ArrayList<Pair<Integer, ArrayList<Double>>> tf_idf = new ArrayList<>();
        for (int i = 0; i < news.size(); i++) {
            ArrayList<Double> point = new ArrayList<>();
            double sum = 0;
            for (int j = 0; j < bagofwords.size(); j++) {
                point.add(tf[i][j]);
                sum += tf[i][j] * tf[i][j];
            }
            sum = Math.sqrt(sum);
            for (int j = 0; j < bagofwords.size(); j++) {
                point.set(j, point.get(j) / sum);
            }
            tf_idf.add(new Pair<>(i, point));
        }
        KMeans kMeans = new KMeans(tf_idf, NUM_CLUSTERS);
        ArrayList<HashMap<Integer, ArrayList<Double>>> result = kMeans.doKMeans();


        ArrayList<ArrayList<Integer>> cluster_result = new ArrayList<>();
        for (HashMap<Integer, ArrayList<Double>> cluster : result) {
            if (cluster.size() > 1) {
                cluster_result.add(new ArrayList<>(cluster.keySet()));
            }
        }
        HashMap<String, ArrayList<News>> cateToNews = new HashMap<>();
        for (ArrayList<Integer> ids : cluster_result) {
            Integer[] count_words = new Integer[bagofwords.size()];
            for (int i = 0; i < bagofwords.size(); i++) {
                count_words[i] = 0;
                for (int j = 0; j < ids.size(); j++) {
                    count_words[i] += tf_count[ids.get(j)][i];
                }
            }
            int maxcount = 0;
            int candidate = -1;
            for (int i = 0; i < bagofwords.size(); i++) {
                if (count_words[i] > maxcount) {
                    maxcount = count_words[i];
                    candidate = i;
                }
            }
            ArrayList<News> news1 = new ArrayList<>();
            for (Integer integer : ids) {
                news1.add(news.get(integer));
            }
            String key = bagofwords.get(candidate);
            if (cateToNews.containsKey(key)) {
                cateToNews.get(key).addAll(news1);
            } else {
                cateToNews.put(key, news1);
            }
            for (ArrayList<News> news2 : cateToNews.values()) {
                Collections.sort(news2, new Comparator<News>() {
                    @Override
                    public int compare(News t1, News t2) {
                        return -t1.getTime().compareTo(t2.getTime());
                    }
                });
            }
        }
        return cateToNews;
    }

    public static int count(String srcStr, String findStr) {
        int count = 0;
        int index = 0;
        while ((index = srcStr.indexOf(findStr, index)) != -1) {
            index = index + findStr.length();
            count++;

        }
        return count;
    }
}
