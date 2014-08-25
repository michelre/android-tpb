package com.remimichel.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.remimichel.activities.R;
import com.remimichel.utils.Category;
import com.remimichel.utils.Torrent;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {

    private Activity activity;
    //private Category rootCategory;
    private List<Category> categories;
    private LayoutInflater inflater;

    public CategoriesAdapter(List<Category> categories, Activity activity){
        this.activity = activity;
        this.categories = categories;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.categories.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.no_clickable_category, null);
        TextView categoryName = (TextView)view.findViewById(R.id.category_name);
        categoryName.setText(this.categories.get(i).getName());
        return view;
    }
}
