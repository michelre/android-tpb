package com.torrenttotransmission.model;

public abstract class Torrent implements Comparable<Torrent>{

    private String size;
    private String title;
    private String description;
    private String downloadLink;

    public Torrent(String size, String title) {
        this.size = size;
        this.title = title;
        this.description = "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
