package com.remimichel.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.remimichel.activities.MainActivity;
import com.remimichel.activities.R;
import com.remimichel.model.Category;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {

    private Activity activity;

    public CategoriesAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return ((MainActivity)this.activity).getCategories().size();
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
        Category category = ((MainActivity)this.activity).getCategories().get(i);
        int resource = (category.isHasChildren()) ? R.layout.no_clickable_category : R.layout.clickable_category;
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, null);
        ImageView expandIcon = (ImageView) view.findViewById(R.id.expand_icon);
        Category clickedCategory = ((MainActivity)this.activity).getClickedCategory();
        if(category.getPath().equals(clickedCategory.getPath()) && !clickedCategory.isExpanded()){
            expandIcon.setImageResource(R.drawable.ic_action_collapse);
        }
        TextView categoryName = (TextView)view.findViewById(R.id.category_name);
        categoryName.setText(category.getName().toLowerCase().substring(0, 1).toUpperCase() + category.getName().toLowerCase().substring(1));
        return view;
    }
}
