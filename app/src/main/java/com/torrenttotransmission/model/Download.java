package com.torrenttotransmission.model;

import com.google.gson.annotations.SerializedName;

public class Download implements Comparable<Download> {


    public Download(int id, String name, float percentDone, int rateDownload, int rateUpload, float size, int status) {
        this.id = id;
        this.name = name;
        this.percentDone = percentDone;
        this.rateDownload = rateDownload;
        this.rateUpload = rateUpload;
        this.size = size;
        this.status = status;
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

    public int getRateDownload() {
        return this.rateDownload;
    }

    public int getRateUpload() {
        return this.rateUpload;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("percentDone")
    private float percentDone;
    @SerializedName("rateDownload")
    private int rateDownload;
    @SerializedName("rateUpload")
    private int rateUpload;
    @SerializedName("totalSize")
    private float size;
    @SerializedName("status")
    private int status;
}
