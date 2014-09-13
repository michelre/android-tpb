package com.remimichel.connection;

import android.app.Service;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.remimichel.model.Connection;
import com.remimichel.model.ConnectionState;
import com.remimichel.services.CheckConnectionService;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ConnectionStateTask implements Runnable {

    private CheckConnectionService service;
    private RequestQueue queue;

    public ConnectionStateTask(CheckConnectionService service) {
        this.service = service;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(service);
        Connection.host = sharedPref.getString("KEY_HOST", "0.0.0.0");
        Connection.port = sharedPref.getString("KEY_PORT", "9091");
        Connection.authentificationRequired = sharedPref.getBoolean("KEY_AUTHENTIFICATION_REQUIRED", true);
        Connection.username = sharedPref.getString("KEY_USERNAME", "");
        Connection.password = sharedPref.getString("KEY_PASSWORD", "");
        Connection.sessionId = sharedPref.getString("KEY_X-Transmission-Session-Id", "");
        this.queue = Volley.newRequestQueue(this.service);
    }

    @Override
    public void run() {
        Connection.state = ConnectionState.CONNECTING;
        String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Connection.state = ConnectionState.CONNECTED;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError || error instanceof NoConnectionError){
                    Connection.state = ConnectionState.CONNECTION_ERROR;
                }
                if(error.networkResponse != null && error.networkResponse.statusCode == 409){
                    Connection.sessionId = error.networkResponse.headers.get("X-Transmission-Session-Id");
                    Connection.state = ConnectionState.CONNECTED;
                }
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(jsonObjectRequest);
    }
}
