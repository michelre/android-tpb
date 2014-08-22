package com.remimichel.listeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.remimichel.utils.Torrent;
import com.remimichel.utils.Torrents;
import com.remimichel.requests.AuthentificationRequest;
import com.remimichel.requests.TorrentAddJsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by remimichel on 14/08/2014.
 */
public class ItemClickListener implements AdapterView.OnItemClickListener{

    private Torrents torrents;
    private String host;
    private String port;
    private String username;
    private String password;
    private String sessionId;
    private Torrent selectedTorrent;
    private ProgressDialog progressDialog;
    private Activity activity;

    public ItemClickListener(Torrents torrents, Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        this.host = sharedPref.getString("KEY_HOST", "0.0.0.0");
        this.port = sharedPref.getString("KEY_PORT", "9091");
        this.username = sharedPref.getString("KEY_USERNAME", "");
        this.password = sharedPref.getString("KEY_PASSWORD", "");
        this.torrents = torrents;
        this.activity = activity;
        //this.initSessionId();
    }

    public void initSessionId(){
        RequestQueue queue = Volley.newRequestQueue(this.activity);
        String url = "http://" + ItemClickListener.this.host + ":" + ItemClickListener.this.port + "/transmission/rpc";
        StringRequest stringRequest = new AuthentificationRequest(Request.Method.POST, url, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Une erreur chiante", volleyError.toString());
                if(volleyError != null && volleyError.networkResponse.statusCode == 409){
                    Log.e("ERROR 409", "ERROR 409");
                    ItemClickListener.this.sessionId = volleyError.networkResponse.headers.get("X-Transmission-Session-Id");
                }
            }
        }, this.username, this.password);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void addTorrent(){
        RequestQueue queue = Volley.newRequestQueue(this.activity);
        String url = "http://" + ItemClickListener.this.host + ":" + ItemClickListener.this.port + "/transmission/rpc";
        try {
            JSONObject jsonObject = new JSONObject("{'method':'torrent-add','arguments':{'paused':false,'filename':'" + ItemClickListener.this.selectedTorrent.getDownloadLink() + "'}}");
            TorrentAddJsonObjectRequest torrentAddRequest = new TorrentAddJsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {
                    ItemClickListener.this.progressDialog.dismiss();
                }
            }, null, this.sessionId, this.username, this.password);
            torrentAddRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(torrentAddRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.selectedTorrent = this.torrents.get(position);
        this.progressDialog = ProgressDialog.show(this.activity, this.selectedTorrent.getTitle(), "Sending to transmission...", true, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ItemClickListener.this.addTorrent();
                } catch (Exception e) {}
            }
        }).start();
    }
}
