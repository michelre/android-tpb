package com.remimichel.listeners;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.remimichel.activities.R;
import com.remimichel.activities.SearchableActivity;
import com.remimichel.adapters.SearchAdapter;
import com.remimichel.utils.Torrent;
import com.remimichel.utils.TorrentDeserializer;
import com.remimichel.utils.Torrents;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by remimichel on 14/08/2014.
 */
public class ResponseSearchListener implements Response.Listener<JSONArray> {

    private Torrents torrents = null;
    private SearchableActivity activity;

    public ResponseSearchListener(SearchableActivity client){
        this.activity = client;
    }

    @Override
    public void onResponse(JSONArray response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Torrent.class, new TorrentDeserializer()).create();
        Type collectionType = new TypeToken<Collection<Torrent>>(){}.getType();
        Torrents torrents = new Torrents((Collection<Torrent>)gson.fromJson(response.toString(), collectionType));
        this.torrents = torrents;
        if(torrents.compareTo(this.activity.getCurrentListTorrents()) != 0){
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activity, R.layout.result_search_row, torrents.toStringArray());
            SearchAdapter adapter = new SearchAdapter(torrents, this.activity);
            this.activity.setListAdapter(adapter);
            //this.activity.setLoading(false);
            ListView listView = this.activity.getListView();
            //listView.setOnScrollListener(new ScrollSearchListener(this.activity));
            listView.setOnItemClickListener(new ItemClickListener(torrents, this.activity));
            this.activity.setCurrentListTorrents(torrents);
            this.activity.dismissProgressDialog();
        }
    }

}
