package com.torrenttotransmission.requests;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.torrenttotransmission.model.Connection;

/**
 * Created by remimichel on 17/08/2014.
 */
public class TorrentAddJsonObjectRequest extends JsonObjectRequest{

    private String sessionId;
    private String username;
    private String password;

    public TorrentAddJsonObjectRequest(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener, String sessionId, String username, String password)
    {
        super(method, url, jsonRequest, listener, errorListener);
        this.sessionId = (sessionId != null) ? sessionId : "";
        this.username = username;
        this.password = password;
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        if(Connection.authentificationRequired){
            String auth = "Basic "
                    + Base64.encodeToString((this.username + ":" + this.password).getBytes(),
                    Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }
        headers.put("X-Transmission-Session-Id", this.sessionId);
        return headers;
    }
}
