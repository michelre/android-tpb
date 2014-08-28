package com.remimichel.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.remimichel.activities.R;
import com.remimichel.model.Torrent;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private Activity activity;
    private List<Torrent> torrents;
    private LayoutInflater inflater;

    public SearchAdapter(List<Torrent> torrents, Activity activity){
        this.activity = activity;
        this.torrents = torrents;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.torrents.size();
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
        view = inflater.inflate(R.layout.result_search_row, null);
        TextView torrentTitle = (TextView)view.findViewById(R.id.torrent_title);
        TextView torrentSize = (TextView)view.findViewById(R.id.torrent_size);
        torrentTitle.setText(this.torrents.get(i).getTitle());
        torrentSize.setText(this.torrents.get(i).getSize());
        return view;
    }
}
