package com.remimichel.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.remimichel.adapters.DownloadsAdapter;
import com.remimichel.adapters.SearchAdapter;
import com.remimichel.listeners.ErrorResponseSearchListener;
import com.remimichel.utils.Download;
import com.remimichel.utils.DownloadDeserializer;
import com.remimichel.utils.DownloadsInfoTask;
import com.remimichel.utils.Torrent;
import com.remimichel.utils.TorrentDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadActivity extends ListActivity {

    private ScheduledThreadPoolExecutor scheduledThread;
    private List<Download> downloads;
    private DownloadsAdapter adapter;

    public ScheduledThreadPoolExecutor getScheduledThread() {
        return scheduledThread;
    }

    public void setScheduledThread(ScheduledThreadPoolExecutor scheduledThread) {
        this.scheduledThread = scheduledThread;
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<Download> downloads) {
        this.downloads = downloads;
    }

    public DownloadsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(DownloadsAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_download);
        this.downloads = new ArrayList<Download>();

        this.adapter = new DownloadsAdapter(this.downloads, this);
        this.setListAdapter(adapter);

        this.scheduledThread = new ScheduledThreadPoolExecutor(1);
        //this.scheduledThread.scheduleWithFixedDelay(new DownloadsInfoTask(this), 0, 2, TimeUnit.SECONDS);
        //this.progressDialog = ProgressDialog.show(this, "", "Searching torrents...", true, false);
    }

    @Override
    public void onPause(){
        super.onPause();
        this.scheduledThread.shutdown();
    }

    @Override
    public void onResume(){
        super.onResume();
        scheduledThread.scheduleWithFixedDelay(new DownloadsInfoTask(this), 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchable, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //private ProgressDialog progressDialog;
    //private ArrayList<Torrent> torrents;
}