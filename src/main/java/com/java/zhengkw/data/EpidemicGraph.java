package com.java.zhengkw.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EpidemicGraph implements Parcelable {
    public String label;
    public String img;
    public String description;
    public ArrayList<String> properties_name = new ArrayList<>();
    public ArrayList<String> properties_content = new ArrayList<>();
    public ArrayList<String> relations_relation = new ArrayList<>();
    public ArrayList<String> relations_label = new ArrayList<>();
    public ArrayList<String> relations_forward = new ArrayList<>(); //"true"/"false"

    public EpidemicGraph() {
    }

    protected EpidemicGraph(Parcel in) {
        label = in.readString();
        img = in.readString();
        description = in.readString();
        properties_name = in.createStringArrayList();
        properties_content = in.createStringArrayList();
        relations_relation = in.createStringArrayList();
        relations_label = in.createStringArrayList();
        relations_forward = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(img);
        dest.writeString(description);
        dest.writeStringList(properties_name);
        dest.writeStringList(properties_content);
        dest.writeStringList(relations_relation);
        dest.writeStringList(relations_label);
        dest.writeStringList(relations_forward);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EpidemicGraph> CREATOR = new Creator<EpidemicGraph>() {
        @Override
        public EpidemicGraph createFromParcel(Parcel in) {
            return new EpidemicGraph(in);
        }

        @Override
        public EpidemicGraph[] newArray(int size) {
            return new EpidemicGraph[size];
        }
    };
}
