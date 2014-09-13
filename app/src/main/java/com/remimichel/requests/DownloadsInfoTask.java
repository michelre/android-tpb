package com.remimichel.requests;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.remimichel.activities.DownloadActivity;
import com.remimichel.connection.CheckActiveConnection;
import com.remimichel.deserializers.DownloadDeserializer;
import com.remimichel.model.Connection;
import com.remimichel.model.ConnectionState;
import com.remimichel.model.Download;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DownloadsInfoTask implements Runnable, CheckActiveConnection {

    private DownloadActivity activity;
    private RequestQueue queue;

    public DownloadsInfoTask(DownloadActivity activity) {
        this.activity = activity;
        this.queue = Volley.newRequestQueue(this.activity);
    }

    @Override
    public void run() {
        this.retrieveDownloadsInfo();
    }

    private void retrieveDownloadsInfo(){
        try {
            String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
            JSONObject jsonObject = new JSONObject("{\"method\":\"torrent-get\", \"arguments\":{\"fields\":[\"name\", \"id\", \"percentDone\", \"rateDownload\", \"rateUpload\", \"totalSize\", \"status\"]}}");
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonArray) {
                    try {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Download.class, new DownloadDeserializer()).create();
                        Type collectionType = new TypeToken<Collection<Download>>() {
                        }.getType();
                        ArrayList<Download> downloads = new ArrayList<Download>((Collection<Download>) gson.fromJson(jsonArray.getJSONObject("arguments").getJSONArray("torrents").toString(), collectionType));
                        DownloadsInfoTask.this.activity.updateDownloadsList(downloads);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Connection.state = ConnectionState.CONNECTION_ERROR;
                }
            }) {

                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    if(Connection.authentificationRequired){
                        String auth = "Basic "
                                + Base64.encodeToString((Connection.username + ":" + Connection.password).getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                    }
                    headers.put("X-Transmission-Session-Id", Connection.sessionId);
                    return headers;
                }

            };
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            this.queue.add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doTask() {
        this.retrieveDownloadsInfo();
    }
}
