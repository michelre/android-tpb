package com.remimichel.activities;

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
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.remimichel.adapters.LeftDrawerAdapter;
import com.remimichel.adapters.SearchAdapter;
import com.remimichel.connection.ConnectionController;
import com.remimichel.connection.ConnectionDialogsController;
import com.remimichel.drawer.DrawerToggle;
import com.remimichel.listeners.DrawerItemClickListener;
import com.remimichel.listeners.ErrorResponseSearchListener;
import com.remimichel.listeners.ItemClickListener;
import com.remimichel.listeners.ScrollSearchListener;
import com.remimichel.model.Connection;
import com.remimichel.model.Torrent;
import com.remimichel.deserializers.TorrentDeserializer;
import com.android.volley.Response;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchableActivity extends ListActivity implements SearchActivities{

    /**
     * ***********
     * ATTRIBUTES  *
     * *************
     */
    private ActionBarDrawerToggle drawerToggle;
    private ItemClickListener itemClickListener;
    private ConnectionController connectionController;
    private boolean loading;
    private ProgressDialog progressDialog;
    private List<Torrent> torrents;
    private int currentPage = 0;
    private String query = "";
    private int index;
    private int top;
    private AbsListView.OnScrollListener scrollListener;
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
        getActionBar().setTitle(intent.getStringExtra(SearchManager.QUERY));
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_searchable);
        progressDialog = ProgressDialog.show(this, "", "Searching torrents...", true, false);
        this.init();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.doSearch();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        connectionController.checkConnectionState();
    }

    @Override protected void onPause(){
        super.onPause();
        connectionController.resetConnectionState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchable, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        menu.findItem(R.id.action_help).setVisible(false);
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
        this.torrents = new ArrayList<Torrent>();
        this.itemClickListener = new ItemClickListener(this);
        this.connectionController = new ConnectionController(this, new ConnectionDialogsController(this, this.itemClickListener));
        this.scrollListener = new ScrollSearchListener(this);
        this.adapter = new SearchAdapter(this);
        this.initDrawer();
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.searchable_activity);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), "SEARCHABLE_ACTIVITY"));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void doSearch() {
        try {
            try {
                this.saveScrollPosition();
                this.setLoading(true);
                RequestQueue queue = Volley.newRequestQueue(SearchableActivity.this);
                String url = Connection.hostApi + ":9001/torrents/search?q=" + URLEncoder.encode(SearchableActivity.this.query, "UTF-8") + "&offset=" + SearchableActivity.this.currentPage;
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Torrent.class, new TorrentDeserializer()).create();
                        Type collectionType = new TypeToken<Collection<Torrent>>() {
                        }.getType();
                        ArrayList<Torrent> torrents = new ArrayList<Torrent>((Collection<Torrent>) gson.fromJson(jsonArray.toString(), collectionType));
                        SearchableActivity.this.updateListView(torrents);
                    }
                }, new ErrorResponseSearchListener(SearchableActivity.this));
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonArrayRequest);
            } catch (UnsupportedEncodingException e) {
            }
        } catch (Exception e) {
        }
    }

    private void updateListView(ArrayList<Torrent> torrents){
        if(this.torrents.size() == 0 || this.torrents.get(0).compareTo(torrents.get(0)) != 0){
            this.torrents.addAll(torrents);
            this.setListAdapter(adapter);
            this.setLoading(false);
            ListView listView = this.getListView();
            listView.setOnScrollListener(this.scrollListener);
            listView.setSelectionFromTop(this.index, this.top);
            listView.setOnItemClickListener(this.itemClickListener);
        }
        this.progressDialog.dismiss();

    }

    private void saveScrollPosition() {
        this.index = this.getListView().getFirstVisiblePosition();
        View v = this.getListView().getChildAt(0);
        this.top = (v == null) ? 0 : v.getTop();
    }

    /**
     * ******************
     * GETTERS & SETTERS  *
     * *******************
     */
    public void incrementCurrentPage() {
        this.currentPage += 1;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public List<Torrent> getTorrents(){
        return this.torrents;
    }

}