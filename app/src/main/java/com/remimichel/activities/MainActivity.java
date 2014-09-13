package com.remimichel.activities;

import android.app.ExpandableListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.remimichel.adapters.CategoriesAdapter;
import com.remimichel.adapters.LeftDrawerAdapter;
import com.remimichel.connection.ConnectionController;
import com.remimichel.connection.ConnectionDialogsController;
import com.remimichel.drawer.DrawerToggle;
import com.remimichel.listeners.DrawerItemClickListener;
import com.remimichel.model.Category;
import com.remimichel.model.Connection;

import org.json.JSONObject;


public class MainActivity extends ExpandableListActivity{

    /**
     * ***********
     * CONSTANTS  *
     * *************
     */
    public final static String CATEGORY_SELECTED = "CATEGORY_SELECTED";

    /**
     * ***********
     * ATTRIBUTES  *
     * *************
     */
    private Category rootCategory;
    private ActionBarDrawerToggle drawerToggle;
    private ConnectionController connectionController;
    private CategoriesAdapter adapter;

    /**
     * ******************
     * ACTIVITY METHODS  *
     * *******************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle("Categories");
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_main);
        this.init();
        if (savedInstanceState == null) {
            this.findCategories("CATEGORIES", 10);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        connectionController.resetConnectionState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        new ConnectionController(this, null).checkConnectionState();
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
        menu.findItem(R.id.action_help).setVisible(false);
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


    /**
     * ******************
     * SERVICES METHODS  *
     * *******************
     */
    private void init() {
        connectionController = new ConnectionController(this, new ConnectionDialogsController(this, null));
        this.adapter = new CategoriesAdapter(MainActivity.this);
        this.initDrawer();
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_activity);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), "Categories"));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void findCategories(String childrenOf, int depth) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Connection.hostApi + ":9001/categories?children_of=" + childrenOf + "&depth=" + depth, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                MainActivity.this.rootCategory = new Gson().fromJson(String.valueOf(jsonObject), Category.class);
                MainActivity.this.rootCategory.getCategories().remove(0);
                setListAdapter(adapter);
                getExpandableListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        Intent intent = new Intent(MainActivity.this, TopActivity.class);
                        intent.putExtra(CATEGORY_SELECTED, MainActivity.this.getRootCategory().getCategories().get(groupPosition).getCategories().get(childPosition).getPath());
                        startActivity(intent);
                        return true;
                    }
                });
            }
        }, null);
        queue.add(jsonObjectRequest);

    }

    /**
     * ******************
     * GETTERS & SETTERS  *
     * *******************
     */
    public Category getRootCategory() {
        return rootCategory;
    }
}