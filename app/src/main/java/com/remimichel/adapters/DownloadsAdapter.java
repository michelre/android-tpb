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
import com.remimichel.listeners.DownloadShortClickListener;
import com.remimichel.model.Download;
import com.remimichel.utils.HumanReadableByteCount;

import java.util.List;

public class DownloadsAdapter extends BaseAdapter {

    private DownloadActivity activity;
    private LayoutInflater inflater;

    public DownloadsAdapter(DownloadActivity activity){
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.activity.getDownloads().size();
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
        Download download = this.activity.getDownloads().get(i);
        view = inflater.inflate(R.layout.download_item, null);
        TextView downloadName = (TextView)view.findViewById(R.id.download_name);
        TextView downloadRate = (TextView)view.findViewById(R.id.download_download_rate);
        TextView uploadRate = (TextView)view.findViewById(R.id.download_upload_rate);
        TextView downloadSize = (TextView)view.findViewById(R.id.download_size);
        ProgressBar downloadPercentDone = (ProgressBar)view.findViewById(R.id.download_percent_done);
        downloadName.setText(download.getName());
        downloadRate.setText(downloadRate.getText() + " " + HumanReadableByteCount.humanReadableByteCount(download.getRateDownload(), true) + "/s");
        uploadRate.setText(uploadRate.getText() + " " + HumanReadableByteCount.humanReadableByteCount(download.getRateUpload(), true) + "/s");
        downloadPercentDone.setProgress(Math.round(download.getPercentDone() * 100));
        downloadSize.setText(downloadSize.getText() + " " + HumanReadableByteCount.humanReadableByteCount((long)download.getSize(), true));
        if(download.getStatus() == 0){
            view.setBackgroundColor(Color.LTGRAY);
        }
        return view;
    }
}
