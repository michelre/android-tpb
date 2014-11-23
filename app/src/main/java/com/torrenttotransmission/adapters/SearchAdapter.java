package com.torrenttotransmission.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.activities.SearchActivities;
import com.torrenttotransmission.model.Torrent;

public class SearchAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;

    public SearchAdapter(Activity activity){
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ((SearchActivities)this.activity).getTorrents().size();
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
        Torrent torrent = ((SearchActivities)this.activity).getTorrents().get(i);
        view = inflater.inflate(R.layout.result_search_row, null);
        TextView torrentTitle = (TextView)view.findViewById(R.id.torrent_title);
        TextView torrentSize = (TextView)view.findViewById(R.id.torrent_size);
        torrentTitle.setText(torrent.getTitle());
        torrentSize.setText(torrent.getSize());
        return view;
    }
}
