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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.remimichel.listeners.ErrorResponseSearchListener;
import com.remimichel.listeners.ResponseSearchListener;
import com.remimichel.utils.Torrent;
import com.remimichel.utils.Torrents;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchableActivity extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_searchable);
        this.progressDialog = ProgressDialog.show(this, "", "Searching torrents...", true, false);
        this.previousButton = (Button)this.findViewById(R.id.prev);
        this.previousButton.setEnabled(false);
        this.nextButton = (Button)this.findViewById(R.id.next);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.doSearch();
        }
    }

    public void showProgressDialog(){
        this.progressDialog.show();
    }

    public void dismissProgressDialog(){
        this.progressDialog.dismiss();
    }

    public void clickPreviousButton(View view){
        //Button previousButton = (Button)view.findViewById(R.id.prev);
        if(this.currentPage > 0){
            this.showProgressDialog();
            this.currentPage -= 1;
            this.doSearch();
            if(this.currentPage == 0)
                this.previousButton.setEnabled(false);
            else
                this.previousButton.setEnabled(true);
        }
    }

    public void clickNextButton(View view){
        this.previousButton.setEnabled(true);
        this.showProgressDialog();
        this.currentPage += 1;
        this.doSearch();
    }

    public void doSearch() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RequestQueue queue = Volley.newRequestQueue(SearchableActivity.this);
                        //String url = "http://90.42.184.170:9001/torrents/search?q=" + URLEncoder.encode(SearchableActivity.this.query, "UTF-8") + "&offset=" + SearchableActivity.this.currentPage;
                        String url = "http://192.168.1.27:9001/torrents/search?q=" + URLEncoder.encode(SearchableActivity.this.query, "UTF-8") + "&offset=" + SearchableActivity.this.currentPage;
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new ResponseSearchListener(SearchableActivity.this), new ErrorResponseSearchListener(SearchableActivity.this));
                        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        queue.add(jsonArrayRequest);
                    } catch (UnsupportedEncodingException e) {}
                }
            }).start();
        } catch (Exception e) {
        }
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
        if( id == R.id.action_settings){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void incrementCurrentPage() {
        this.currentPage += 1;
    }

    public void setCurrentListTorrents(Torrents torrents){
        this.currentListTorrents = torrents;
    }

    public Torrents getCurrentListTorrents(){
        return this.currentListTorrents;
    }

    public ProgressDialog getProgressDialog(){
        return this.progressDialog;
    }

    private ProgressDialog progressDialog;
    private Torrents currentListTorrents = new Torrents(new Torrent("", "", ""));
    private int currentPage = 0;
    private String query = "";
    private Button previousButton;
    private Button nextButton;
}