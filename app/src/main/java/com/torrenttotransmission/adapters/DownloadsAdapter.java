package com.torrenttotransmission.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;
import com.torrenttotransmission.activities.DownloadActivity;
import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.model.Download;
import com.torrenttotransmission.utils.HumanReadableByteCount;

import org.w3c.dom.Text;

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
        final Download download = this.activity.getDownloads().get(i);
        view = inflater.inflate(R.layout.download_item, null);
        TextView downloadName = (TextView)view.findViewById(R.id.download_name);
        TextView downloadRate = (TextView)view.findViewById(R.id.download_download_rate);
        TextView uploadRate = (TextView)view.findViewById(R.id.download_upload_rate);
        TextView downloadSize = (TextView)view.findViewById(R.id.download_size);
        ImageButton playOrStopButton = (ImageButton)view.findViewById(R.id.button_play_or_stop);
        if(download.getStatus() == 0)
            playOrStopButton.setImageResource(R.drawable.ic_action_play);
        else
            playOrStopButton.setImageResource(R.drawable.ic_action_stop);
        playOrStopButton.setTag(activity.getDownloads().get(i));
        ImageButton removeButton = (ImageButton)view.findViewById(R.id.button_remove);
        removeButton.setTag(activity.getDownloads().get(i));
        ProgressWheel downloadPercentDone = (ProgressWheel)view.findViewById(R.id.download_percent_done);
        downloadPercentDone.setText(HumanReadableByteCount.formatFloat(download.getPercentDone() * 100) + "%");
        downloadName.setText(download.getName());
        downloadRate.setText(downloadRate.getText() + " " + HumanReadableByteCount.humanReadableByteCount(download.getRateDownload(), true) + "/s");
        uploadRate.setText(uploadRate.getText() + " " + HumanReadableByteCount.humanReadableByteCount(download.getRateUpload(), true) + "/s");
        downloadPercentDone.setProgress(Math.round(download.getPercentDone() * 360));
        if(download.getPercentDone() < 1.0){
            downloadPercentDone.incrementProgress();
        }
        else{
            downloadPercentDone.stopSpinning();
            downloadPercentDone.setRimColor(activity.getResources().getColor(R.color.darkgreen));
        }
        downloadSize.setText(downloadSize.getText() + " " + HumanReadableByteCount.humanReadableByteCount((long)download.getSize(), true));
        if(download.getStatus() == 0){
            view.setBackgroundColor(Color.LTGRAY);
        }
        if(activity.getStopOrRestartProgress() != null)
            activity.getStopOrRestartProgress().dismiss();
        return view;
    }
}
