package com.remimichel.utils;

public class Torrent implements Comparable<Torrent>{

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
}
