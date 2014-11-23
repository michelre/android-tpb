package com.torrenttotransmission.listeners;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.torrenttotransmission.activities.APIActivity;
import com.torrenttotransmission.activities.CategoryActivity;
import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.activities.TopActivity;
import com.torrenttotransmission.connection.ConnectionDialogs;
import com.torrenttotransmission.model.Category;
import com.torrenttotransmission.model.Connection;
import com.torrenttotransmission.model.ConnectionState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiItemClickListener implements AdapterView.OnItemClickListener {

    private ConnectionDialogs connectionDialogs;
    private ProgressDialog progressDialog;
    private APIActivity activity;
    private RequestQueue queue;

    public ApiItemClickListener(APIActivity activity) {
        this.activity = activity;
        this.queue = Volley.newRequestQueue(this.activity);
        this.connectionDialogs = new ConnectionDialogs(activity);
    }

    private void startCategoryActivity(){
        Intent intent = new Intent(activity, CategoryActivity.class);
        activity.startActivity(intent);
    }

    private void authenticateT411(){
        progressDialog = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.connecting), true, true);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.activity);
        Connection.selectedAPI.setPseudo(sharedPref.getString("KEY_USERNAME_T411", ""));
        Connection.selectedAPI.setPassword(sharedPref.getString("KEY_PASSWORD_T411", ""));
        String url = Connection.selectedAPI.getHost()+":9002/auth/"+Connection.selectedAPI.getName();
            StringRequest stringRequest  = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        progressDialog.dismiss();
                        if(jsonObject.has("token")){
                            Connection.selectedAPI.setAuthorization(jsonObject.get("token").toString());
                            startCategoryActivity();
                        }else{
                            connectionDialogs.getDialogConfigFragment().setTitle(activity.getResources().getString(R.string.t411_configuration_dialog));
                            connectionDialogs.getDialogConfigFragment().setMessage(activity.getResources().getString(R.string.t411_configuration_message_dialog));
                            connectionDialogs.showDialogConfig();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, null) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", Connection.selectedAPI.getPseudo());
                    params.put("password", Connection.selectedAPI.getPassword());

                    return params;
                }
            };
            queue.add(stringRequest);
        };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Connection.selectedAPI = activity.getApis().get(position);
        if(Connection.selectedAPI.getName().equals("t411")){
            authenticateT411();
        }else{
            startCategoryActivity();
        }
    }
}