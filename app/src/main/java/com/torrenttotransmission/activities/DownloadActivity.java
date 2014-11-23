package com.torrenttotransmission.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.torrenttotransmission.adapters.DownloadsAdapter;
import com.torrenttotransmission.adapters.LeftDrawerAdapter;
import com.torrenttotransmission.connection.ConnectionController;
import com.torrenttotransmission.connection.ConnectionDialogsController;
import com.torrenttotransmission.deserializers.DownloadDeserializer;
import com.torrenttotransmission.drawer.DrawerToggle;
import com.torrenttotransmission.fragments.DialogActionDownloadFragment;
import com.torrenttotransmission.listeners.DrawerItemClickListener;
import com.torrenttotransmission.model.Connection;
import com.torrenttotransmission.model.ConnectionState;
import com.torrenttotransmission.model.Download;
import com.torrenttotransmission.connection.CheckActiveConnection;
import com.torrenttotransmission.requests.DownloadsInfoTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadActivity extends ListActivity implements CheckActiveConnection, ConnectionControllerActivities{

    /**
     * ***********
     * ATTRIBUTES  *
     * *************
     */
    private ScheduledFuture task;
    private ScheduledThreadPoolExecutor scheduledThread;
    private List<Download> downloads;
    private DownloadsAdapter adapter;
    private ActionBarDrawerToggle drawerToggle;
    private ConnectionController connectionController;
    private DialogActionDownloadFragment deleteDownloadDialog;
    private ProgressDialog stopOrRestartProgress;

    /**
     * ******************
     * ACTIVITY METHODS  *
     * *******************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getResources().getString(R.string.title_activity_download));
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_download);
        this.init();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FlurryAgent.onStartSession(this, "65C555N873XQQCYQRNR6");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }


    @Override
    public void onPause(){
        super.onPause();
        if(this.task != null)
            this.task.cancel(false);
        connectionController.resetConnectionState();
    }

    @Override
    public void onResume(){
        super.onResume();
        connectionController.checkConnectionState();
        connectionController.notifyConnectionState();
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ******************
     * SERVICES METHODS  *
     * *******************
     */
    public void clickPlayHandler(View v){
        doStopOrRestart((Download)v.getTag());
    }

    public void clickRemoveHandler(View v){
        this.deleteDownloadDialog.setDownload((Download)v.getTag());
        this.deleteDownloadDialog.show(getFragmentManager(), "delete-download");
    }

    public void doStopOrRestart(final Download download){
        try {
            stopOrRestartProgress = ProgressDialog.show(this, "", getResources().getString(R.string.waiting), true, false);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
            JSONObject jsonObject;
            if(download.getStatus() == 0)
                jsonObject = new JSONObject("{\"method\":\"torrent-start\", \"arguments\":{\"ids\":[" + download.getId() + "]}}");
            else
                jsonObject = new JSONObject("{\"method\":\"torrent-stop\", \"arguments\":{\"ids\":[" + download.getId() + "]}}");
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonArray) {
                    if(download.getStatus() == 0){
                        download.setStatus(6);
                    }else{
                        download.setStatus(0);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    stopOrRestartProgress.dismiss();
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
            queue.add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doDelete(Download download, boolean removeLocalData){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + Connection.host + ":" + Connection.port + "/transmission/rpc";
            JSONObject jsonObject = new JSONObject("{\"method\":\"torrent-remove\", \"arguments\":{\"ids\":[" + download.getId() + "], \"delete-local-data\":" + removeLocalData + "}}");
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonArray) {
                    try {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Download.class, new DownloadDeserializer()).create();
                        Type collectionType = new TypeToken<Collection<Download>>() {
                        }.getType();
                        ArrayList<Download> downloads = new ArrayList<Download>((Collection<Download>) gson.fromJson(jsonArray.getJSONObject("arguments").getJSONArray("torrents").toString(), collectionType));
                        updateDownloadsList(downloads);
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
            queue.add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void init() {
        this.deleteDownloadDialog = new DialogActionDownloadFragment();
        this.deleteDownloadDialog.setActivity(this);
        this.downloads = new ArrayList<Download>();
        this.adapter = new DownloadsAdapter(this);
        this.setListAdapter(adapter);
        connectionController = new ConnectionController(this, new ConnectionDialogsController(this, this));
        this.scheduledThread = new ScheduledThreadPoolExecutor(1);
        this.initDrawer();
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.searchable_activity);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), getResources().getString(R.string.title_activity_download)));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void updateDownloadsList(List<Download> downloads) {
        this.downloads.clear();
        this.downloads.addAll(downloads);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void doTask() {
        DownloadsInfoTask downloadsInfoTask = new DownloadsInfoTask(this);
        if(Connection.state == ConnectionState.CONNECTED){
            this.task = this.scheduledThread.scheduleAtFixedRate(downloadsInfoTask, 0, 2500, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * ******************
     * GETTERS & SETTERS  *
     * *******************
     */
    public List<Download> getDownloads(){
        return this.downloads;
    }

    public DownloadsAdapter getAdapter() {
        return adapter;
    }

    @Override
    public ConnectionController getConnectionController() {
        return this.connectionController;
    }

    public ProgressDialog getStopOrRestartProgress() {
        return stopOrRestartProgress;
    }

    public ScheduledThreadPoolExecutor getScheduledThread() {
        return scheduledThread;
    }
}