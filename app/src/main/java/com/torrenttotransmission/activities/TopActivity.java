package com.torrenttotransmission.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.torrenttotransmission.adapters.LeftDrawerAdapter;
import com.torrenttotransmission.adapters.SearchAdapter;
import com.torrenttotransmission.connection.ConnectionController;
import com.torrenttotransmission.connection.ConnectionDialogsController;
import com.torrenttotransmission.drawer.DrawerToggle;
import com.torrenttotransmission.listeners.DrawerItemClickListener;
import com.torrenttotransmission.listeners.ErrorResponseSearchListener;
import com.torrenttotransmission.listeners.ItemClickListener;
import com.torrenttotransmission.model.Connection;
import com.torrenttotransmission.model.Torrent;
import com.torrenttotransmission.deserializers.TorrentDeserializer;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TopActivity extends ListActivity implements SearchActivities, ConnectionControllerActivities {

    /**
     * ***********
     * ATTRIBUTES  *
     * *************
     */
    private ActionBarDrawerToggle drawerToggle;
    private ItemClickListener itemClickListener;
    private ConnectionController connectionController;
    private ProgressDialog progressDialog;
    private List<Torrent> torrents;
    private SearchAdapter adapter;


    /**
     * ******************
     * ACTIVITY METHODS  *
     * *******************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        getActionBar().setTitle(intent.getStringExtra(CategoryActivity.CATEGORY_SELECTED));
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_searchable);
        this.init();
        this.progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.searching_torrents), true, true);
        if(savedInstanceState == null)
            this.doSearch(intent.getStringExtra(CategoryActivity.CATEGORY_SELECTED));
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
    protected void onResume(){
        super.onResume();
        connectionController.checkConnectionState();
    }

    @Override
    protected void onPause(){
        super.onPause();
        connectionController.resetConnectionState();
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
    private void init(){
        this.adapter = new SearchAdapter(this);
        this.torrents = new ArrayList<Torrent>();
        itemClickListener = new ItemClickListener(this);
        connectionController = new ConnectionController(this, new ConnectionDialogsController(this, this.itemClickListener));
        TextView title = (TextView) findViewById(R.id.title_searchable);
        title.setText(title.getText() + " " + Connection.selectedAPI.getName());
        this.initDrawer();
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.searchable_activity);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), "Top"));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void doSearch(String categoryPath) {
        try {
            RequestQueue queue = Volley.newRequestQueue(TopActivity.this);
            String authorization = (Connection.selectedAPI.getName().equals("t411")) ? "?authorization=" + Connection.selectedAPI.getAuthorization() : "";
            String url = Connection.selectedAPI.getHost()+ ":" + Connection.selectedAPI.getPort() + "/torrents/" + Connection.selectedAPI.getName() + "/top/" + categoryPath + authorization;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    Gson gson = new GsonBuilder().registerTypeAdapter(Torrent.class, new TorrentDeserializer()).create();
                    Type collectionType = new TypeToken<Collection<Torrent>>() {
                    }.getType();
                    ArrayList<Torrent> torrents = new ArrayList<Torrent>((Collection<Torrent>) gson.fromJson(jsonArray.toString(), collectionType));
                    TopActivity.this.updateListView(torrents);
                }
            }, new ErrorResponseSearchListener(TopActivity.this));
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonArrayRequest);

        } catch (Exception e) {
        }
    }

    private void updateListView(ArrayList<Torrent> torrents) {
        if (this.torrents.size() == 0 || this.torrents.get(0).compareTo(torrents.get(0)) != 0) {
            this.torrents.addAll(torrents);
            this.setListAdapter(this.adapter);
            ListView listView = this.getListView();
            listView.setOnItemClickListener(itemClickListener);
        }
        this.progressDialog.dismiss();
    }

    /**
     * ******************
     * GETTERS & SETTERS  *
     * *******************
     */
    public ConnectionController getConnectionController() {
        return connectionController;
    }

    @Override
    public List<Torrent> getTorrents() {
        return this.torrents;
    }

}