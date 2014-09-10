package com.remimichel.listeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.remimichel.model.Connection;
import com.remimichel.model.Torrent;
import com.remimichel.requests.TorrentAddJsonObjectRequest;
import com.remimichel.connection.CheckActiveConnection;
import com.remimichel.connection.ConnectionStateController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemClickListener implements AdapterView.OnItemClickListener, CheckActiveConnection {

    private ArrayList<Torrent> torrents;
    private String sessionId;
    private Torrent selectedTorrent;
    private ProgressDialog progressDialog;
    private Activity activity;
    private RequestQueue queue;

    private ConnectionStateController connectionStateController;

    public ItemClickListener(ArrayList<Torrent> torrents, Activity activity) {
        this.torrents = torrents;
        this.activity = activity;
        this.queue = Volley.newRequestQueue(this.activity);
        this.connectionStateController = new ConnectionStateController(this.activity, this);
    }

    private void addTorrent() {
        this.progressDialog = ProgressDialog.show(this.activity, "", "Sending to transmission...", true, true);
        String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
        try {
            JSONObject jsonObject = new JSONObject("{'method':'torrent-add','arguments':{'paused':false,'filename':'" + ItemClickListener.this.selectedTorrent.getDownloadLink() + "'}}");
            TorrentAddJsonObjectRequest torrentAddRequest = new TorrentAddJsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    ItemClickListener.this.progressDialog.dismiss();
                }
            }, null, Connection.sessionId, Connection.username, Connection.password);
            torrentAddRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            ItemClickListener.this.queue.add(torrentAddRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.selectedTorrent = this.torrents.get(position);
        ItemClickListener.this.connectionStateController.checkConnectionState();
    }

    @Override
    public void doTask() {
        this.addTorrent();
    }
}
