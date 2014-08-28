package com.remimichel.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Download implements Comparable<Download>{

    public Download(int id, String name, float percentDone) {
        this.id = id;
        this.name = name;
        this.percentDone = percentDone;
    }

    @Override
    public int compareTo(Download download) {
        return (this.id == download.getId()) ? 0 : -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(float percentDone) {
        this.percentDone = percentDone;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("percentDone")
    private float percentDone;
}
