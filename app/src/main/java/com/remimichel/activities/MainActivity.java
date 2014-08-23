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
    private List<Category> noClickableCategories = new ArrayList<Category>();
    private List<Category> categoriesToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        setContentView(R.layout.activity_main);
        this.findAllCategories();
    }

    private void findAllCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://90.42.184.170:9001/categories", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                MainActivity.this.rootCategory = new Gson().fromJson(String.valueOf(jsonObject), Category.class);
                //MainActivity.this.categoriesToDisplay = new ArrayList<Category>(MainActivity.this.rootCategory.getCategories());
                //ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(MainActivity.this, R.layout.no_clickable_category, MainActivity.this.categoriesToDisplay);
                MainActivity.this.setNoClickableCategories();
                CategoriesAdapter adapter = new CategoriesAdapter(MainActivity.this.rootCategory, MainActivity.this);
                MainActivity.this.setListAdapter(adapter);
                //MainActivity.this.getListView().setOnItemClickListener(MainActivity.this);

            }
        }, null);
        queue.add(jsonObjectRequest);

    }

    private void setNoClickableCategories(ArrayList<Category> categories, ArrayList<Category> acc) {
        if(categories.size() != 0){
            this.setNoClickableCategories();
        }


        for(Category c: this.rootCategory.getCategories()){
            if(c.getCategories().size() != 0){
                this.noClickableCategories.add(c);
            }
        }
    }

    private void setClick


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
        if(!this.rootCategory.getCategories().isEmpty()){
            this.makeListToDisplay(this.categoriesToDisplay.get(i));
            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(MainActivity.this, android.R.layout.simple_list_item_1, this.categoriesToDisplay);
            this.setListAdapter(adapter);
        }
    }
}
