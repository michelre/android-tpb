package com.remimichel.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.remimichel.activities.DownloadActivity;
import com.remimichel.activities.R;
import com.remimichel.listeners.DownloadLongClickListener;
import com.remimichel.model.Download;
import com.remimichel.utils.HumanReadableByteCount;

import java.util.List;

public class DownloadsAdapter extends BaseAdapter {

    private Activity activity;
    private List<Download> downloads;
    private LayoutInflater inflater;
    private DownloadLongClickListener downloadLongClickListener;

    public DownloadsAdapter(List<Download> downloads, Activity activity){
        this.activity = activity;
        this.downloads = downloads;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.downloadLongClickListener = new DownloadLongClickListener(this.downloads, (DownloadActivity)this.activity);
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
        TextView downloadRate = (TextView)view.findViewById(R.id.download_download_rate);
        TextView uploadRate = (TextView)view.findViewById(R.id.download_upload_rate);
        TextView downloadSize = (TextView)view.findViewById(R.id.download_size);
        ProgressBar downloadPercentDone = (ProgressBar)view.findViewById(R.id.download_percent_done);
        downloadName.setText(this.downloads.get(i).getName());
        downloadRate.setText(downloadRate.getText() + " " + HumanReadableByteCount.humanReadableByteCount(this.downloads.get(i).getRateDownload(), true) + "/s");
        uploadRate.setText(uploadRate.getText() + " " + HumanReadableByteCount.humanReadableByteCount(this.downloads.get(i).getRateUpload(), true) + "/s");
        downloadPercentDone.setProgress(Math.round(this.downloads.get(i).getPercentDone() * 100));
        downloadSize.setText(downloadSize.getText() + " " + HumanReadableByteCount.humanReadableByteCount((long)this.downloads.get(i).getSize(), true));
        return view;
    }

    public void updateDownloadsList(List<Download> downloads) {
        this.downloads.clear();
        this.downloads.addAll(downloads);
        this.notifyDataSetChanged();
        ((DownloadActivity)this.activity).getListView().setOnItemLongClickListener(this.downloadLongClickListener);
    }
}
