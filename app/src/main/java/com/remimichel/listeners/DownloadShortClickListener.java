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
import com.remimichel.activities.DownloadActivity;
import com.remimichel.fragments.DialogActionDownloadFragment;
import com.remimichel.model.Connection;
import com.remimichel.model.Download;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DownloadShortClickListener implements AdapterView.OnItemClickListener {

    private DownloadActivity activity;
    private RequestQueue queue;
    private DialogActionDownloadFragment dialog;
    private Download selectedDownload;

    public DownloadShortClickListener(DownloadActivity activity) {
        this.activity = activity;
        this.queue = Volley.newRequestQueue(this.activity);
        this.dialog = new DialogActionDownloadFragment();
        this.dialog.setDownloadClickListener(this);
        this.dialog.setActivity(this.activity);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.dialog.show(this.activity.getFragmentManager(), "stop-download");
        this.selectedDownload = this.activity.getDownloads().get(i);
    }

    public void doStopOrRestart(){
        try {
            String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
            JSONObject jsonObject;
            if(this.selectedDownload.getStatus() == 0)
                jsonObject = new JSONObject("{\"method\":\"torrent-start\", \"arguments\":{\"ids\":[" + this.selectedDownload.getId() + "]}}");
            else
                jsonObject = new JSONObject("{\"method\":\"torrent-stop\", \"arguments\":{\"ids\":[" + this.selectedDownload.getId() + "]}}");
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonArray) {
                    Download selectedDownload = DownloadShortClickListener.this.selectedDownload;
                    if(selectedDownload.getStatus() == 0){
                        selectedDownload.setStatus(6);
                    }else{
                        selectedDownload.setStatus(0);
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

    public Download getSelectedDownload() {
        return selectedDownload;
    }
}
