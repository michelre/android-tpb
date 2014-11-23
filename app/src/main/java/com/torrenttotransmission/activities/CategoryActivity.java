package com.torrenttotransmission.activities;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.torrenttotransmission.adapters.CategoriesAdapter;
import com.torrenttotransmission.adapters.LeftDrawerAdapter;
import com.torrenttotransmission.connection.ConnectionController;
import com.torrenttotransmission.connection.ConnectionDialogsController;
import com.torrenttotransmission.drawer.DrawerToggle;
import com.torrenttotransmission.listeners.DrawerItemClickListener;
import com.torrenttotransmission.model.Category;
import com.torrenttotransmission.model.Connection;

import org.json.JSONObject;


public class CategoryActivity extends ExpandableListActivity implements ConnectionControllerActivities{

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
    private ProgressDialog progressDialog;

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
        getActionBar().setTitle(getResources().getString(R.string.title_category_activity));
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_main);
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
    protected void onPause(){
        super.onPause();
        connectionController.resetConnectionState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.search_categories), true, false);
        this.findCategories("CATEGORIES", 10);
        this.connectionController.checkConnectionState();
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


    /**
     * ******************
     * SERVICES METHODS  *
     * *******************
     */
    private void init() {
        connectionController = new ConnectionController(this, new ConnectionDialogsController(this, null));
        this.adapter = new CategoriesAdapter(CategoryActivity.this);
        this.initDrawer();
    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_activity);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new LeftDrawerAdapter(this.getApplicationContext(), getResources().getString(R.string.title_category_activity)));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener(this));
        drawerToggle = new DrawerToggle(this, drawerLayout).getActionBarDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void findCategories(String childrenOf, int depth) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Connection.selectedAPI.getHost() + ":" + Connection.selectedAPI.getPort() + "/categories/" + Connection.selectedAPI.getName() + "?children_of=" + childrenOf + "&depth=" + depth;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                rootCategory = new Gson().fromJson(String.valueOf(jsonObject), Category.class);
                rootCategory.getCategories().remove(0);
                setListAdapter(adapter);
                getExpandableListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        Intent intent = new Intent(CategoryActivity.this, TopActivity.class);
                        intent.putExtra(CATEGORY_SELECTED, CategoryActivity.this.getRootCategory().getCategories().get(groupPosition).getCategories().get(childPosition).getPath());
                        startActivity(intent);
                        return true;
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error main", volleyError.toString());
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_error,
                        (ViewGroup)findViewById(R.id.toast_error));
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(R.string.error_api_connection);
                Toast toast = new Toast(CategoryActivity.this);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                progressDialog.dismiss();
                toast.show();
            }
        });
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

    public ConnectionController getConnectionController() {
        return connectionController;
    }
}