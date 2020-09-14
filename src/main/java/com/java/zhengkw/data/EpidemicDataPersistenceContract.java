package com.java.zhengkw.data;

import android.provider.BaseColumns;

import java.util.HashMap;

public final class EpidemicDataPersistenceContract {
    private EpidemicDataPersistenceContract() {
    }

    public static abstract class CountryEntry implements BaseColumns {

        public static final int NUM_COUNTRY = 11;

        public static final HashMap<Integer, String> idToName = new HashMap<>();

        public static final HashMap<String, Integer> nameToId = new HashMap<>();

        public static final HashMap<Integer, String> idToDisplay = new HashMap<>();

        static {
            idToName.put(0, "United States of America");
            idToName.put(1, "China");
            idToName.put(2, "India");
            idToName.put(3, "Brazil");
            idToName.put(4, "United Kingdom");
            idToName.put(5, "Spain");
            idToName.put(6, "Russia");
            idToName.put(7, "Peru");
            idToName.put(8, "France");
            idToName.put(9, "Canada");
            idToName.put(10, "Mexico");


            nameToId.put("United States of America", 0);
            nameToId.put("China", 1);
            nameToId.put("India", 2);
            nameToId.put("Brazil", 3);
            nameToId.put("United Kingdom", 4);
            nameToId.put("Spain", 5);
            nameToId.put("Russia", 6);
            nameToId.put("Peru", 7);
            nameToId.put("France", 8);
            nameToId.put("Canada", 9);
            nameToId.put("Mexico", 10);

            idToDisplay.put(0, "美国");
            idToDisplay.put(1, "中国");
            idToDisplay.put(2, "印度");
            idToDisplay.put(3, "巴西");
            idToDisplay.put(4, "英国");
            idToDisplay.put(5, "西班牙");
            idToDisplay.put(6, "俄罗斯");
            idToDisplay.put(7, "秘鲁");
            idToDisplay.put(8, "法国");
            idToDisplay.put(9, "加拿大");
            idToDisplay.put(10, "墨西哥");
        }
    }

    public static abstract class ChinaEntry implements BaseColumns {

        public static final int NUM_CHINA = 34;

        public static final HashMap<Integer, String> idToName = new HashMap<>();

        public static final HashMap<String, Integer> nameToId = new HashMap<>();

        public static final HashMap<Integer, String> idToDisplay = new HashMap<>();

        static {
            idToName.put(0, "China|Hong Kong");
            idToName.put(1, "China|Xinjiang");
            idToName.put(2, "China|Beijing");
            idToName.put(3, "China|Sichuan");
            idToName.put(4, "China|Gansu");
            idToName.put(5, "China|Guangdong");
            idToName.put(6, "China|Guangxi");
            idToName.put(7, "China|Hebei");
            idToName.put(8, "China|Shaanxi");
            idToName.put(9, "China|Shanxi");
            idToName.put(10, "China|Yunnan");
            idToName.put(11, "China|Chongqing");
            idToName.put(12, "China|Inner Mongol");
            idToName.put(13, "China|Shandong");
            idToName.put(14, "China|Zhejiang");
            idToName.put(15, "China|Tianjin");
            idToName.put(16, "China|Liaoning");
            idToName.put(17, "China|Fujian");
            idToName.put(18, "China|Jiangsu");
            idToName.put(19, "China|Hainan");
            idToName.put(20, "China|Macao");
            idToName.put(21, "China|Jilin");
            idToName.put(22, "China|Hubei");
            idToName.put(23, "China|Jiangxi");
            idToName.put(24, "China|Heilongjiang");
            idToName.put(25, "China|Anhui");
            idToName.put(26, "China|Guizhou");
            idToName.put(27, "China|Hunan");
            idToName.put(28, "China|Henan");
            idToName.put(29, "China|Ningxia");
            idToName.put(30, "China|Qinghai");
            idToName.put(31, "China|Xizang");
            idToName.put(32, "China|Taiwan");
            idToName.put(33, "China|Shanghai");

            nameToId.put("China|Hong Kong", 0);
            nameToId.put("China|Xinjiang", 1);
            nameToId.put("China|Beijing", 2);
            nameToId.put("China|Sichuan", 3);
            nameToId.put("China|Gansu", 4);
            nameToId.put("China|Guangdong", 5);
            nameToId.put("China|Guangxi", 6);
            nameToId.put("China|Hebei", 7);
            nameToId.put("China|Shaanxi", 8);
            nameToId.put("China|Shanxi", 9);
            nameToId.put("China|Yunnan", 10);
            nameToId.put("China|Chongqing", 11);
            nameToId.put("China|Inner Mongol", 12);
            nameToId.put("China|Shandong", 13);
            nameToId.put("China|Zhejiang", 14);
            nameToId.put("China|Tianjin", 15);
            nameToId.put("China|Liaoning", 16);
            nameToId.put("China|Fujian", 17);
            nameToId.put("China|Jiangsu", 18);
            nameToId.put("China|Hainan", 19);
            nameToId.put("China|Macao", 20);
            nameToId.put("China|Jilin", 21);
            nameToId.put("China|Hubei", 22);
            nameToId.put("China|Jiangxi", 23);
            nameToId.put("China|Heilongjiang", 24);
            nameToId.put("China|Anhui", 25);
            nameToId.put("China|Guizhou", 26);
            nameToId.put("China|Hunan", 27);
            nameToId.put("China|Henan", 28);
            nameToId.put("China|Ningxia", 29);
            nameToId.put("China|Qinghai", 30);
            nameToId.put("China|Xizang", 31);
            nameToId.put("China|Taiwan", 32);
            nameToId.put("China|Shanghai", 33);

            idToDisplay.put(0, "香港");
            idToDisplay.put(1, "新疆");
            idToDisplay.put(2, "北京");
            idToDisplay.put(3, "四川");
            idToDisplay.put(4, "甘肃");
            idToDisplay.put(5, "广东");
            idToDisplay.put(6, "广西");
            idToDisplay.put(7, "河北");
            idToDisplay.put(8, "陕西");
            idToDisplay.put(9, "山西");
            idToDisplay.put(10, "云南");
            idToDisplay.put(11, "重庆");
            idToDisplay.put(12, "内蒙古");
            idToDisplay.put(13, "山东");
            idToDisplay.put(14, "浙江");
            idToDisplay.put(15, "天津");
            idToDisplay.put(16, "辽宁");
            idToDisplay.put(17, "福建");
            idToDisplay.put(18, "江苏");
            idToDisplay.put(19, "海南");
            idToDisplay.put(20, "澳门");
            idToDisplay.put(21, "吉林");
            idToDisplay.put(22, "湖北");
            idToDisplay.put(23, "江西");
            idToDisplay.put(24, "黑龙江");
            idToDisplay.put(25, "安徽");
            idToDisplay.put(26, "贵州");
            idToDisplay.put(27, "湖南");
            idToDisplay.put(28, "河南");
            idToDisplay.put(29, "宁夏");
            idToDisplay.put(30, "青海");
            idToDisplay.put(31, "西藏");
            idToDisplay.put(32, "台湾");
            idToDisplay.put(33, "上海");
        }
    }
}
