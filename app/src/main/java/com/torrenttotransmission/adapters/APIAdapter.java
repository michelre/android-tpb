package com.torrenttotransmission.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;
import com.torrenttotransmission.activities.APIActivity;
import com.torrenttotransmission.activities.DownloadActivity;
import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.model.Api;
import com.torrenttotransmission.model.Download;
import com.torrenttotransmission.utils.HumanReadableByteCount;

public class APIAdapter extends BaseAdapter {

    private APIActivity activity;
    private LayoutInflater inflater;

    public APIAdapter(APIActivity activity){
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.activity.getApis().size();
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
        Api api = this.activity.getApis().get(i);
        view = inflater.inflate(R.layout.api_item, null);
        ImageView apiIcon = (ImageView)view.findViewById(R.id.api_icon);
        apiIcon.setImageResource(api.getLogo());
        return view;
    }
}
