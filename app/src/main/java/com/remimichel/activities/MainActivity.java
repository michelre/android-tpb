package com.remimichel.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.remimichel.adapters.CategoriesAdapter;
import com.remimichel.model.Category;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

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
    private ArrayList<Category> categories;
    private int firstIndex;
    private int lastIndex;
    private int indexOfClick;

    /**
     * ******************
     * ACTIVITY METHODS  *
     * *******************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            this.findCategories("CATEGORIES", 1);
        }
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
        if (id == R.id.action_download) {
            Intent intent = new Intent(this, DownloadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstance) {
        saveInstance.putInt("firstIndex", this.firstIndex);
        saveInstance.putInt("lastIndex", this.lastIndex);
        saveInstance.putInt("indexOfClick", this.indexOfClick);
        saveInstance.putParcelableArrayList("categories", this.categories);
        super.onSaveInstanceState(saveInstance);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.indexOfClick = i;
        if (this.categories.get(i).isHasChildren())
            this.findCategories(this.categories.get(i).getPath(), 1);
        else {
            Intent intent = new Intent(this, TopActivity.class);
            intent.putExtra(CATEGORY_SELECTED, this.categories.get(i).getPath());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    /**
     * ******************
     * SERVICES METHODS  *
     * *******************
     */
    private void findCategories(String childrenOf, int depth) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://82.122.122.250:9001/categories?children_of=" + childrenOf + "&depth=" + depth, null, new Response.Listener<JSONObject>() {
            //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://192.168.1.17:9001/categories?children_of="+childrenOf+"&depth="+depth, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Category category = new Gson().fromJson(String.valueOf(jsonObject), Category.class);
                if (category.isHasChildren().booleanValue()) {
                    MainActivity.this.setCategories(category);
                    CategoriesAdapter adapter = new CategoriesAdapter(MainActivity.this.categories, MainActivity.this);
                    MainActivity.this.setListAdapter(adapter);
                }
                MainActivity.this.getListView().setOnItemClickListener(MainActivity.this);

            }
        }, null);
        queue.add(jsonObjectRequest);

    }

    private void setCategories(Category category) {
        if (this.categories == null) {
            this.categories = new ArrayList<Category>(category.getCategories());
            this.firstIndex = 0;
            this.lastIndex = 0;
        } else {
            if (this.firstIndex != 0 && this.lastIndex != 0)
                this.categories.subList(this.firstIndex, this.lastIndex).clear();
            if (this.firstIndex != this.indexOfClick + 1) {
                this.firstIndex = (this.indexOfClick > (this.lastIndex - this.firstIndex)) ? this.indexOfClick - (this.lastIndex - this.firstIndex) + 1 : this.indexOfClick + 1;
                this.lastIndex = category.getCategories().size() + this.firstIndex;
                this.categories.addAll(this.firstIndex, category.getCategories());
            } else {
                this.firstIndex = 0;
                this.lastIndex = 0;
            }
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.categories = savedInstanceState.getParcelableArrayList("categories");
        CategoriesAdapter adapter = new CategoriesAdapter(this.categories, this);
        this.setListAdapter(adapter);
        this.getListView().setOnItemClickListener(MainActivity.this);
        this.firstIndex = savedInstanceState.getInt("firstIndex");
        this.lastIndex = savedInstanceState.getInt("lastIndex");
        this.indexOfClick = savedInstanceState.getInt("indexOfClick");
    }
}