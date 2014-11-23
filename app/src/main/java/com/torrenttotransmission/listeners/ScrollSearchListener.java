package com.torrenttotransmission.listeners;

import android.widget.AbsListView;

import com.torrenttotransmission.activities.SearchableActivity;

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
