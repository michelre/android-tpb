package com.remimichel.listeners;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
import com.remimichel.deserializers.DownloadDeserializer;
import com.remimichel.fragments.DialogDeleteDownloadFragment;
import com.remimichel.model.Connection;
import com.remimichel.model.Download;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadLongClickListener implements AdapterView.OnItemLongClickListener {

    private DownloadActivity activity;
    private List<Download> downloads;
    private RequestQueue queue;
    private DialogDeleteDownloadFragment deleteDownloadDialog;
    private Download downloadToDelete;

    public Download getDownloadToDelete(){
        return this.downloadToDelete;
    }

    public DownloadLongClickListener(List<Download> downloads, DownloadActivity activity) {
        this.downloads = downloads;
        this.activity = activity;
        this.queue = Volley.newRequestQueue(this.activity);
        this.deleteDownloadDialog = new DialogDeleteDownloadFragment();
        this.deleteDownloadDialog.setDownloadLongClickListener(this);
        this.deleteDownloadDialog.setActivity(this.activity);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.deleteDownloadDialog.show(this.activity.getFragmentManager(), "delete-download");
        this.downloadToDelete = this.downloads.get(i);
        Log.e("JSON OBJECT", " " + this.downloadToDelete.getId());
        return false;
    }

    public void doDelete(boolean removeLocalData){
        try {
            String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
            JSONObject jsonObject = new JSONObject("{\"method\":\"torrent-remove\", \"arguments\":{\"ids\":[" + this.downloadToDelete.getId() + "], \"delete-local-data\":" + removeLocalData + "}}");

            Log.e("JSON OBJECT", jsonObject.toString() + " " + this.downloadToDelete.getId());
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonArray) {
                    try {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Download.class, new DownloadDeserializer()).create();
                        Type collectionType = new TypeToken<Collection<Download>>() {
                        }.getType();
                        ArrayList<Download> downloads = new ArrayList<Download>((Collection<Download>) gson.fromJson(jsonArray.getJSONObject("arguments").getJSONArray("torrents").toString(), collectionType));
                        DownloadLongClickListener.this.activity.getAdapter().updateDownloadsList(downloads);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("ERROR DOWNLOAD INFO", volleyError.toString());
                }
            }) {

                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    String auth = "Basic "
                            + Base64.encodeToString((Connection.username + ":" + Connection.password).getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    headers.put("X-Transmission-Session-Id", Connection.sessionId);
                    return headers;
                }

            };
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            this.queue.add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
