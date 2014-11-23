package com.torrenttotransmission.activities;

import android.app.Activity;
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

import com.flurry.android.FlurryAgent;
import com.torrenttotransmission.adapters.LeftDrawerAdapter;
import com.torrenttotransmission.connection.ConnectionController;
import com.torrenttotransmission.connection.ConnectionDialogsController;
import com.torrenttotransmission.drawer.DrawerToggle;
import com.torrenttotransmission.fragments.SettingsFragment;
import com.torrenttotransmission.listeners.DrawerItemClickListener;

public class SettingActivity extends Activity implements ConnectionControllerActivities{

    private ConnectionController connectionController;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.title_activity_setting_display));
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        connectionController = new ConnectionController(this, new ConnectionDialogsController(this, null));
        setContentView(R.layout.activity_setting);
        this.initDrawer();
        getFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_framelayout, new SettingsFragment())
                .commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
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
    protected void onPause(){
        super.onPause();
        connectionController.resetConnectionState();
        connectionController.checkConnectionState();
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.setting_activity);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), getResources().getString(R.string.title_activity_setting_display)));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public ConnectionController getConnectionController() {
        return this.connectionController;
    }
}
