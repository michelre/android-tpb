package com.remimichel.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.remimichel.adapters.DownloadsAdapter;
import com.remimichel.model.Download;
import com.remimichel.utils.DownloadsInfoTask;

import java.util.ArrayList;
import java.util.List;
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
        scheduledThread.scheduleWithFixedDelay(new DownloadsInfoTask(this), 0, 3000, TimeUnit.MILLISECONDS);
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