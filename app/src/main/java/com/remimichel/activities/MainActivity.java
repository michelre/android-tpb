package com.remimichel.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.remimichel.adapters.CategoriesAdapter;
import com.remimichel.utils.Category;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private Category rootCategory;
    private List<Category> categoriesToDisplay;
    private int firstIndex;
    private int lastIndex;
    private int indexOfClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        setContentView(R.layout.activity_main);
        this.findCategories("CATEGORIES", 1);
    }

    private void findCategories(String childrenOf, int depth) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://90.42.143.2:9001/categories?children_of="+childrenOf+"&depth="+depth, null, new Response.Listener<JSONObject>() {
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://192.168.0.14:9001/categories?children_of="+childrenOf+"&depth="+depth, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //MainActivity.this.rootCategory = new Gson().fromJson(String.valueOf(jsonObject), Category.class);
                Category category = new Gson().fromJson(String.valueOf(jsonObject), Category.class);
                if(category.isHasChildren().booleanValue()){
                    MainActivity.this.setCategoriesToDisplay(category);
                    CategoriesAdapter adapter = new CategoriesAdapter(MainActivity.this.categoriesToDisplay, MainActivity.this);
                    MainActivity.this.setListAdapter(adapter);
                    MainActivity.this.getListView().setOnItemClickListener(MainActivity.this);
                }
                //MainActivity.this.categoriesToDisplay = new ArrayList<Category>(MainActivity.this.rootCategory.getCategories());
                //ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(MainActivity.this, R.layout.no_clickable_category, MainActivity.this.categoriesToDisplay);

            }
        }, null);
        queue.add(jsonObjectRequest);

    }

    private void setCategoriesToDisplay(Category category){
        if(this.categoriesToDisplay == null){
            this.categoriesToDisplay = new ArrayList<Category>(category.getCategories());
            this.firstIndex = 0;
            this.lastIndex = 0;
        }else{
            if(this.firstIndex != 0 && this.lastIndex != 0){
                this.categoriesToDisplay.subList(this.firstIndex, this.lastIndex).clear();
            }
            this.firstIndex = (this.indexOfClick > (this.lastIndex - this.firstIndex)) ? this.indexOfClick - (this.lastIndex - this.firstIndex) + 1 : this.indexOfClick + 1;
            this.lastIndex = category.getCategories().size() + this.firstIndex;
            this.categoriesToDisplay.addAll(this.firstIndex, category.getCategories());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
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

    public void makeListToDisplay(Category category){
        this.categoriesToDisplay.clear();
        for(Category c: this.rootCategory.getCategories()){
            this.categoriesToDisplay.add(c);
            if(category.compareTo(c) == 0){
                this.categoriesToDisplay.addAll(c.getCategories());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.indexOfClick = i;
        this.findCategories(this.categoriesToDisplay.get(i).getPath(), 1);
    }
}
