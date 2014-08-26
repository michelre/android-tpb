package com.remimichel.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.remimichel.activities.R;
import com.remimichel.utils.Category;
import com.remimichel.utils.Torrent;

import java.util.List;
import java.util.Locale;

public class CategoriesAdapter extends BaseAdapter {

    private Activity activity;
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
        int resource = (this.categories.get(i).isHasChildren()) ? R.layout.no_clickable_category : R.layout.clickable_category;
        view = inflater.inflate(resource, null);
        TextView categoryName = (TextView)view.findViewById(R.id.category_name);
        categoryName.setText(this.categories.get(i).getName().toLowerCase().substring(0, 1).toUpperCase() + this.categories.get(i).getName().toLowerCase().substring(1));
        return view;
    }
}
