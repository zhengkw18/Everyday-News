package com.java.zhengkw.data;

import java.util.ArrayList;

public final class EpidemicData {
    public ArrayList<EpidemicDataRow> mData = new ArrayList<>();

    public EpidemicData() {

    }

    public EpidemicData(ArrayList<EpidemicDataRow> data) {
        mData = data;
    }

    public class EpidemicDataRow {
        public int id;
        public ArrayList<EpidemicDataSingle> data = new ArrayList<>();

        public EpidemicDataRow() {
        }
    }

    public class EpidemicDataSingle {
        public int confirmed;
        public int dead;
        public int cured;

        public EpidemicDataSingle() {
        }
    }
}
