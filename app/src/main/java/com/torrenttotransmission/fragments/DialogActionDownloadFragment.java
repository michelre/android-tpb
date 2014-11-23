package com.torrenttotransmission.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.torrenttotransmission.activities.DownloadActivity;
import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.model.Download;

public class DialogActionDownloadFragment extends DialogFragment {

    private DownloadActivity activity;
    private View layoutDialog;
    private Download download;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            this.layoutDialog = this.activity.getLayoutInflater().inflate(R.layout.dialog_delete_download, null);
            TextView text = (TextView)layoutDialog.findViewById(R.id.remove_download_text);
            text.setText(text.getText() + " " + download.getName() + " ?");

        builder.setView(layoutDialog);

        builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    CheckBox checkBox = (CheckBox)layoutDialog.findViewById(R.id.checkbox_remove_local_data);
                    activity.doDelete(download, checkBox.isChecked());
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        return builder.create();
    }

    public void setDownload(Download download){
        this.download = download;
    }
    public void setActivity(DownloadActivity a){
        this.activity = a;
    }

}
