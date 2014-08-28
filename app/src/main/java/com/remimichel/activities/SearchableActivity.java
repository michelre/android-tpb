package com.remimichel.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.remimichel.adapters.SearchAdapter;
import com.remimichel.listeners.ErrorResponseSearchListener;
import com.remimichel.listeners.ScrollSearchListener;
import com.remimichel.utils.Torrent;
import com.remimichel.utils.TorrentDeserializer;
import com.android.volley.Response;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

public class SearchableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_searchable);
        this.progressDialog = ProgressDialog.show(this, "", "Searching torrents...", true, false);
        this.torrents = new ArrayList<Torrent>();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.doSearch();
        }
    }

    public void doSearch() {
        try {
            try {
                this.saveScrollPosition();
                this.setLoading(true);
                RequestQueue queue = Volley.newRequestQueue(SearchableActivity.this);
                //String url = "http://90.42.184.170:9001/torrents/search?q=" + URLEncoder.encode(SearchableActivity.this.query, "UTF-8") + "&offset=" + SearchableActivity.this.currentPage;
                String url = "http://82.122.122.250:9001/torrents/search?q=" + URLEncoder.encode(SearchableActivity.this.query, "UTF-8") + "&offset=" + SearchableActivity.this.currentPage;
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
            SearchAdapter adapter = new SearchAdapter(this.torrents, this);
            this.setListAdapter(adapter);
            this.setLoading(false);
            ListView listView = this.getListView();
            listView.setOnScrollListener(new ScrollSearchListener(this));
            listView.setSelectionFromTop(this.index, this.top);
            //listView.setOnItemClickListener(new ItemClickListener(torrents, this));
        }
        this.progressDialog.dismiss();

    }

    private void saveScrollPosition() {
        this.index = this.getListView().getFirstVisiblePosition();
        View v = this.getListView().getChildAt(0);
        this.top = (v == null) ? 0 : v.getTop();
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
        if( id == R.id.action_download){
            Intent intent = new Intent(this, DownloadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void incrementCurrentPage() {
        this.currentPage += 1;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    private boolean loading;
    private ProgressDialog progressDialog;
    private ArrayList<Torrent> torrents;
    private int currentPage = 0;
    private String query = "";
    private int index;
    private int top;
}