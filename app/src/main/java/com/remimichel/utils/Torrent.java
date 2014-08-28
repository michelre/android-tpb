package com.remimichel.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Torrent implements Comparable<Torrent>, Parcelable{

    @Override
    public int compareTo(Torrent torrent) {
        return torrent.downloadLink.compareTo(this.downloadLink);
    }

    public String toString(){ return this.title; }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Torrent(String downloadLink, String size, String title) {
        this.downloadLink = downloadLink;
        this.size = size;
        this.title = title;
    }

    private String downloadLink;
    private String size;
    private String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.downloadLink);
        out.writeString(this.size);
        out.writeString(this.title);
    }

    public static final Parcelable.Creator<Torrent> CREATOR
            = new Parcelable.Creator<Torrent>() {
        public Torrent createFromParcel(Parcel in) {
            return new Torrent(in);
        }

        public Torrent[] newArray(int size) {
            return new Torrent[size];
        }
    };

    private Torrent(Parcel in) {
        this.downloadLink = in.readString();
        this.size = in.readString();
        this.title = in.readString();
    }
}
