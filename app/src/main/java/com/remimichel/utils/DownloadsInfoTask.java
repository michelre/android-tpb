package com.remimichel.utils;

import android.app.Activity;
import android.app.ListActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.remimichel.activities.DownloadActivity;
import com.remimichel.adapters.DownloadsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DownloadsInfoTask implements Runnable {

    private DownloadActivity activity;
    private int index;
    private int top;

    public DownloadsInfoTask(DownloadActivity activity) {
        this.activity = activity;
    }

    private void saveScrollPosition() {
        this.index = this.activity.getListView().getFirstVisiblePosition();
        View v = this.activity.getListView().getChildAt(0);
        this.top = (v == null) ? 0 : v.getTop();
    }

    @Override
    public void run() {
        try {
            RequestQueue queue = Volley.newRequestQueue(this.activity);
            String url = "http://82.122.122.250:9091/transmission/rpc";
            JSONObject jsonObject = new JSONObject("{\"method\":\"torrent-get\", \"arguments\":{\"fields\":[\"name\", \"id\", \"percentDone\"]}}");
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonArray) {
                    try {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Download.class, new DownloadDeserializer()).create();
                        Type collectionType = new TypeToken<Collection<Download>>() {
                        }.getType();
                        ArrayList<Download> downloads = new ArrayList<Download>((Collection<Download>) gson.fromJson(jsonArray.getJSONObject("arguments").getJSONArray("torrents").toString(), collectionType));
                        if(DownloadsInfoTask.this.activity.getAdapter() != null){
                            Log.e("Main", "notifyDataSetChanged() now");
                            DownloadsInfoTask.this.activity.getAdapter().updateDownloadsList(downloads);
                        }
                        //DownloadsInfoTask.this.saveScrollPosition();
                        //DownloadsInfoTask.this.activity.getListView().setSelectionFromTop(DownloadsInfoTask.this.index, DownloadsInfoTask.this.top);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, null) {

                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    String auth = "Basic "
                            + Base64.encodeToString(("pi:ixe7yiem").getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    headers.put("X-Transmission-Session-Id", "UNDdmK0n7fIYa74h3l8kmFkFCuD3Ox567jbr84mfzCDFj1l3");
                    return headers;
                }

            };
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
