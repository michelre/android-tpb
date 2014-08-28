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
import com.remimichel.utils.Download;

import java.util.List;

public class DownloadsAdapter extends BaseAdapter {

    private Activity activity;
    private List<Download> downloads;
    private LayoutInflater inflater;

    public DownloadsAdapter(List<Download> downloads, Activity activity){
        this.activity = activity;
        this.downloads = downloads;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.downloads.size();
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
        view = inflater.inflate(R.layout.download_item, null);
        TextView downloadName = (TextView)view.findViewById(R.id.download_name);
        TextView downloadPercentDone = (TextView)view.findViewById(R.id.download_percent_done);
        downloadName.setText(this.downloads.get(i).getName());
        downloadPercentDone.setText(String.valueOf(this.downloads.get(i).getPercentDone()*100)+"%");
        return view;
    }

    public void updateDownloadsList(List<Download> downloads) {
        this.downloads.clear();
        this.downloads.addAll(downloads);
        this.notifyDataSetChanged();
    }
}