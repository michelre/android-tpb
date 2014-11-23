package com.torrenttotransmission.model;

import com.google.gson.annotations.SerializedName;

public class TorrentPiratebay extends Torrent{

    @SerializedName("url")
    private String url;
    @SerializedName("download_link")
    private String downloadLink;

    public TorrentPiratebay(String size, String title, String url, String downloadLink) {
        super(size, title);
        this.url = url;
        this.downloadLink = downloadLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public int compareTo(Torrent torrent) {
        return ((TorrentPiratebay)torrent).url.compareTo(this.url);
    }
}
