package com.torrenttotransmission.adapters;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.torrenttotransmission.activities.CategoryActivity;
import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.model.Category;


public class CategoriesAdapter implements ExpandableListAdapter {

    private CategoryActivity activity;

    public CategoriesAdapter(Activity activity) {
        this.activity = (CategoryActivity) activity;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return this.activity.getRootCategory().getCategories().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.activity.getRootCategory().getCategories().get(groupPosition).getCategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.activity.getRootCategory().getCategories().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.activity.getRootCategory().getCategories().get(groupPosition).getCategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = this.activity.getLayoutInflater();
        view = layoutInflater.inflate(R.layout.no_clickable_category, null);
        TextView textView = (TextView) view.findViewById(R.id.category_name);
        textView.setText(((Category) this.getGroup(groupPosition)).getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = this.activity.getLayoutInflater();
        view = layoutInflater.inflate(R.layout.clickable_category, null);
        TextView textView = (TextView) view.findViewById(R.id.category_name);
        textView.setText(((Category) this.getChild(groupPosition, childPosition)).getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l2) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}
