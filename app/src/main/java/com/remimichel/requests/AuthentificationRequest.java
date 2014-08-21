package com.remimichel.requests;

import android.util.Base64;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by remimichel on 18/08/2014.
 */
public class AuthentificationRequest extends StringRequest {

    private String username;
    private String password;

    public AuthentificationRequest(int method, String url, Listener<String> listener, ErrorListener errorListener, String username, String password){
        super(method, url, listener, errorListener);
        this.username = username;
        this.password = password;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{
        Map<String, String> headers = new HashMap<String, String>();
        String auth = "Basic "
                + Base64.encodeToString((this.username + ":" + this.password).getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
    }
}
