package com.remimichel.activities;


import com.remimichel.connection.ConnectionController;
import com.remimichel.model.Torrent;

import java.util.List;

public interface SearchActivities {

    public ConnectionController getConnectionController();
    public List<Torrent> getTorrents();

}
