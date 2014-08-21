package com.remimichel.listeners;

import android.app.ProgressDialog;
import android.widget.AbsListView;

import com.remimichel.activities.SearchableActivity;

/**
 * Created by remimichel on 14/08/2014.
 */
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
        if(totalItemCount > visibleItemCount && lastItem == totalItemCount){// && !this.activity.isLoading()){
            this.activity.incrementCurrentPage();
            //this.activity.getProgressDialog().show(this.activity, "", "Searching torrents...", true, false);
            this.activity.showProgressDialog();
            ScrollSearchListener.this.activity.doSearch();
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //TODO --> Make the dialog disappeared correctly
                        ScrollSearchListener.this.activity.doSearch();
                    } catch (Exception e) {}
                }
            }).start();*/
        }
    }
}
