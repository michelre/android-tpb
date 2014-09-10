package com.remimichel.connection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Base64;
import android.util.Log;

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
import com.remimichel.fragments.DialogConfigFragment;
import com.remimichel.model.Connection;
import com.remimichel.model.Download;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConnectionDialogs {

    private ProgressDialog progressDialogConnecting;
    private DialogConfigFragment dialogConfigFragment;
    private Activity activity;

    public ConnectionDialogs(Activity activity){
        this.activity = activity;
        this.dialogConfigFragment = new DialogConfigFragment();
        this.dialogConfigFragment.setActivity(activity);
    }

    public void showProgressDialog(){
        if(this.progressDialogConnecting == null)
            this.progressDialogConnecting = ProgressDialog.show(this.activity, "", "Connecting to the transmission server...");
    }

    public void dismissProgressDialogConnecting(){
        if(this.progressDialogConnecting != null){
            this.progressDialogConnecting.dismiss();
            this.progressDialogConnecting = null;
        }
    }

    public void showDialogConfig(){
        this.dialogConfigFragment.show(this.activity.getFragmentManager(), "settings_dialog");
    }
}
