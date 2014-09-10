package com.remimichel.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.remimichel.adapters.DownloadsAdapter;
import com.remimichel.model.Connection;
import com.remimichel.model.Download;
import com.remimichel.connection.CheckActiveConnection;
import com.remimichel.connection.ConnectionStateController;
import com.remimichel.utils.DownloadsInfoTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadActivity extends ListActivity implements CheckActiveConnection {

    private ScheduledFuture task;
    private ScheduledThreadPoolExecutor scheduledThread;
    private List<Download> downloads;
    private DownloadsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_download);
        this.downloads = new ArrayList<Download>();
        this.adapter = new DownloadsAdapter(this.downloads, this);
        this.setListAdapter(adapter);
        this.scheduledThread = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(this.task != null)
            this.task.cancel(false);
    }

    @Override
    public void onResume(){
        super.onResume();
        new ConnectionStateController(this, this).checkConnectionState();
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

    public DownloadsAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void doTask() {
        DownloadsInfoTask downloadsInfoTask = new DownloadsInfoTask(this);
        if(Connection.state != "CONNECTION_ERROR")
            this.task = this.scheduledThread.scheduleAtFixedRate(downloadsInfoTask, 0, 2000, TimeUnit.MILLISECONDS);
        else{
            this.task.cancel(false);
            this.task = this.scheduledThread.scheduleAtFixedRate(downloadsInfoTask, 0, 2000, TimeUnit.MILLISECONDS);
        }
    }
}