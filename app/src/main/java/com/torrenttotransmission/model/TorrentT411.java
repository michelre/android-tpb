package com.torrenttotransmission.model;

import com.google.gson.annotations.SerializedName;

public class TorrentT411 extends Torrent{

    @SerializedName("id")
    private String id;

    public TorrentT411(String size, String id, String title) {
        super(size, title);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Torrent torrent) {
        return ((TorrentT411)torrent).id.compareTo(this.id);
    }
}
