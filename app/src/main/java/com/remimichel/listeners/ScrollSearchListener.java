package com.remimichel.listeners;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.AbsListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.remimichel.activities.SearchableActivity;

import java.net.URLEncoder;

public class ScrollSearchListener implements AbsListView.OnScrollListener {

    private SearchableActivity activity;

    public ScrollSearchListener(SearchableActivity searchableActivity){
        this.activity = searchableActivity;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {}

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = firstVisibleItem + visibleItemCount;
        if(totalItemCount > visibleItemCount && lastItem == totalItemCount && !this.activity.isLoading()){
            this.activity.incrementCurrentPage();
            this.activity.doSearch();
        }
    }
}
