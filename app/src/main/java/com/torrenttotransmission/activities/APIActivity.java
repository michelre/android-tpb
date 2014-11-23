package com.torrenttotransmission.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.torrenttotransmission.adapters.APIAdapter;
import com.torrenttotransmission.adapters.LeftDrawerAdapter;
import com.torrenttotransmission.drawer.DrawerToggle;
import com.torrenttotransmission.listeners.ApiItemClickListener;
import com.torrenttotransmission.listeners.DrawerItemClickListener;
import com.torrenttotransmission.model.Api;
import com.torrenttotransmission.model.Connection;

import java.util.ArrayList;
import java.util.List;

public class APIActivity extends Activity {

    private ActionBarDrawerToggle drawerToggle;
    private APIAdapter adapter;
    private List<Api> apis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        getActionBar().setTitle(getResources().getString(R.string.title_activity_api));
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        this.init();
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void init() {
        this.apis = new ArrayList<Api>();
        Api t411Api = new Api(Connection.hostApi, "9002", "t411", R.drawable.t411);
        Api pirateBayApi = new Api(Connection.hostApi, "9002", "piratebay", R.drawable.piratebay);
        this.apis.add(t411Api);
        this.apis.add(pirateBayApi);
        Connection.selectedAPI = pirateBayApi;
        adapter = new APIAdapter(this);
        GridView gridView = (GridView)findViewById(R.id.api_gridview);
        gridView.setAdapter(adapter);
        initDrawer();
        gridView.setOnItemClickListener(new ApiItemClickListener(this));
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.api_activity);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), getResources().getString(R.string.title_activity_api)));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Api> getApis() {
        return apis;
    }
}
