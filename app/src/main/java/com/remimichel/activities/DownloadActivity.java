package com.remimichel.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.remimichel.adapters.DownloadsAdapter;
import com.remimichel.adapters.LeftDrawerAdapter;
import com.remimichel.connection.ConnectionController;
import com.remimichel.connection.ConnectionDialogsController;
import com.remimichel.drawer.DrawerToggle;
import com.remimichel.fragments.DialogHelpFragment;
import com.remimichel.listeners.DownloadLongClickListener;
import com.remimichel.listeners.DownloadShortClickListener;
import com.remimichel.listeners.DrawerItemClickListener;
import com.remimichel.model.Connection;
import com.remimichel.model.ConnectionState;
import com.remimichel.model.Download;
import com.remimichel.connection.CheckActiveConnection;
import com.remimichel.requests.DownloadsInfoTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadActivity extends ListActivity implements CheckActiveConnection {

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
    private DialogHelpFragment helpDialog;
    private DownloadLongClickListener downloadLongClickListener;
    private DownloadShortClickListener downloadShortClickListener;

    /**
     * ******************
     * ACTIVITY METHODS  *
     * *******************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Downloads");
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
        if (id == R.id.action_help) {
            this.helpDialog.show(this.getFragmentManager(), "help-dialog");
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
    private void init() {
        this.downloadLongClickListener = new DownloadLongClickListener(this);
        this.downloadShortClickListener = new DownloadShortClickListener(this);
        this.getListView().setOnItemLongClickListener(this.downloadLongClickListener);
        this.getListView().setOnItemClickListener(this.downloadShortClickListener);
        this.helpDialog = new DialogHelpFragment();
        this.helpDialog.setActivity(this);
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
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), "Downloads"));
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
        if(Connection.state == ConnectionState.CONNECTED)
            this.task = this.scheduledThread.scheduleAtFixedRate(downloadsInfoTask, 0, 2000, TimeUnit.MILLISECONDS);
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
}