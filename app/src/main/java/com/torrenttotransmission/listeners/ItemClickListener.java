package com.torrenttotransmission.listeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.torrenttotransmission.activities.ConnectionControllerActivities;
import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.activities.SearchActivities;
import com.torrenttotransmission.fragments.DialogInfoTorrentFragment;
import com.torrenttotransmission.model.Api;
import com.torrenttotransmission.model.Connection;
import com.torrenttotransmission.model.ConnectionState;
import com.torrenttotransmission.model.Torrent;
import com.torrenttotransmission.model.TorrentPiratebay;
import com.torrenttotransmission.model.TorrentT411;
import com.torrenttotransmission.requests.TorrentAddJsonObjectRequest;
import com.torrenttotransmission.connection.CheckActiveConnection;
import org.json.JSONException;
import org.json.JSONObject;


public class ItemClickListener implements AdapterView.OnItemClickListener, CheckActiveConnection {

    private Torrent selectedTorrent;
    private ProgressDialog progressDialog;
    private Activity activity;
    private RequestQueue queue;
    private DialogInfoTorrentFragment dialogInfoTorrentFragment = new DialogInfoTorrentFragment();


    public ItemClickListener(Activity activity) {
        this.activity = activity;
        this.dialogInfoTorrentFragment.setListener(this);
        dialogInfoTorrentFragment.setActivity(activity);
        this.queue = Volley.newRequestQueue(this.activity);
    }

    private void postTorrent(){
        Api api = Connection.selectedAPI;
        String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
        try {
            JSONObject jsonObject = new JSONObject("{\"method\":\"torrent-add\",\"arguments\":{\"paused\":false,\"filename\":\"" + selectedTorrent.getDownloadLink() + "\"}}");
            TorrentAddJsonObjectRequest torrentAddRequest = new TorrentAddJsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    ItemClickListener.this.progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            }, Connection.sessionId, Connection.username, Connection.password);
            torrentAddRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(torrentAddRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addTorrent() {
        this.progressDialog = ProgressDialog.show(this.activity, "", activity.getResources().getString(R.string.sending_transmission), true, true);
        Api api = Connection.selectedAPI;
        String url = api.getHost() + ":" + api.getPort() + "/torrents/t411/download/" + ((TorrentT411)selectedTorrent).getId() + "?authorization=" + api.getAuthorization();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    selectedTorrent.setDownloadLink(jsonObject.getString("magnet_link"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postTorrent();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void torrentInfo(){
       this.progressDialog = ProgressDialog.show(this.activity, "", activity.getResources().getString(R.string.searching_info), true, true);
        Api selectedApi = Connection.selectedAPI;
        String url = selectedApi.getHost() + ":" + selectedApi.getPort() + "/torrents/" + selectedApi.getName() + "/info?id=";
        url += (selectedApi.getName().equals("t411")) ? ((TorrentT411)this.selectedTorrent).getId() + "&authorization=" + selectedApi.getAuthorization() : ((TorrentPiratebay)this.selectedTorrent).getUrl();
        JsonObjectRequest torrentAddRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                try {
                    selectedTorrent.setDescription(jsonObject.get("description").toString());
                    dialogInfoTorrentFragment.show(activity.getFragmentManager(), "torrent-info-dialog");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        torrentAddRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ItemClickListener.this.queue.add(torrentAddRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.selectedTorrent = ((SearchActivities)this.activity).getTorrents().get(position);
        dialogInfoTorrentFragment.setTorrent(selectedTorrent);
        if(selectedTorrent.getDescription().equals(""))
            this.torrentInfo();
        else
            dialogInfoTorrentFragment.show(activity.getFragmentManager(), "torrent-info-dialog");
    }

    @Override
    public void doTask() {
        if(Connection.state != ConnectionState.CONNECTED)
            ((ConnectionControllerActivities)this.activity).getConnectionController().notifyConnectionState();
        else{
            if(selectedTorrent instanceof TorrentT411)
                addTorrent();
            else
                postTorrent();
        }
    }
}
