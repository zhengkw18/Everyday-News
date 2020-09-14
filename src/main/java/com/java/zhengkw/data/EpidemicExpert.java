package com.java.zhengkw.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EpidemicExpert implements Parcelable {
    public String avatar;
    public String name;
    public double activity;
    public int citations;
    public int pubs;
    public double sociability;
    public String affiliation;
    public String position;
    public String bio;
    public ArrayList<String> edu = new ArrayList<>();
    public ArrayList<String> work = new ArrayList<>();

    public EpidemicExpert() {
    }

    protected EpidemicExpert(Parcel in) {
        avatar = in.readString();
        name = in.readString();
        activity = in.readDouble();
        citations = in.readInt();
        pubs = in.readInt();
        sociability = in.readDouble();
        affiliation = in.readString();
        position = in.readString();
        bio = in.readString();
        edu = in.createStringArrayList();
        work = in.createStringArrayList();
    }

    public static final Creator<EpidemicExpert> CREATOR = new Creator<EpidemicExpert>() {
        @Override
        public EpidemicExpert createFromParcel(Parcel in) {
            return new EpidemicExpert(in);
        }

        @Override
        public EpidemicExpert[] newArray(int size) {
            return new EpidemicExpert[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(avatar);
        parcel.writeString(name);
        parcel.writeDouble(activity);
        parcel.writeInt(citations);
        parcel.writeInt(pubs);
        parcel.writeDouble(sociability);
        parcel.writeString(affiliation);
        parcel.writeString(position);
        parcel.writeString(bio);
        parcel.writeStringList(edu);
        parcel.writeStringList(work);
    }
}
