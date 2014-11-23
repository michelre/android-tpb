package com.torrenttotransmission.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;

import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.listeners.ItemClickListener;
import com.torrenttotransmission.model.Torrent;

public class DialogInfoTorrentFragment extends DialogFragment {

    private Torrent torrent;
    private ItemClickListener listener;
    private Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.torrent.getTitle());
        builder.setMessage(Html.fromHtml(this.torrent.getDescription()));

        builder.setPositiveButton(activity.getResources().getString(R.string.download), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.doTask();
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        return builder.create();
    }

    public void setListener(ItemClickListener listener){
        this.listener = listener;
    }

    public void setTorrent(Torrent torrent) {
        this.torrent = torrent;
    }

    public void setActivity(Activity a){
        this.activity = a;
    }
}
