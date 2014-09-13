package com.remimichel.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.remimichel.activities.R;
import com.remimichel.listeners.DownloadLongClickListener;
import com.remimichel.listeners.DownloadShortClickListener;

public class DialogActionDownloadFragment extends DialogFragment {

    private Object downloadClickListener;
    private Activity activity;
    private View layoutDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(downloadClickListener instanceof DownloadLongClickListener){
            this.layoutDialog = this.activity.getLayoutInflater().inflate(R.layout.dialog_delete_download, null);
            TextView text = (TextView)layoutDialog.findViewById(R.id.remove_download_text);
            text.setText(text.getText() + ((DownloadLongClickListener) downloadClickListener).getDownloadToDelete().getName() + " ?");
        }
        if(downloadClickListener instanceof DownloadShortClickListener){
            DownloadShortClickListener downloadShortClickListener = (DownloadShortClickListener)DialogActionDownloadFragment.this.downloadClickListener;
            if(downloadShortClickListener.getSelectedDownload().getStatus() == 0)
                this.layoutDialog = this.activity.getLayoutInflater().inflate(R.layout.dialog_start_download, null);
            else
                this.layoutDialog = this.activity.getLayoutInflater().inflate(R.layout.dialog_pause_download, null);
            TextView text = (TextView)layoutDialog.findViewById(R.id.pause_download_text);
            text.setText(text.getText() + ((DownloadShortClickListener) downloadClickListener).getSelectedDownload().getName() + " ?");
        }
        builder.setView(layoutDialog);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(downloadClickListener instanceof DownloadLongClickListener){
                    CheckBox checkBox = (CheckBox)layoutDialog.findViewById(R.id.checkbox_remove_local_data);
                    ((DownloadLongClickListener)downloadClickListener).doDelete(checkBox.isChecked());
                }
                if(downloadClickListener instanceof DownloadShortClickListener){
                    DownloadShortClickListener downloadShortClickListener = (DownloadShortClickListener)downloadClickListener;
                    downloadShortClickListener.doStopOrRestart();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        return builder.create();
    }

    public void setDownloadClickListener(Object downloadClickListener){
        this.downloadClickListener = downloadClickListener;
    }

    public void setActivity(Activity a){
        this.activity = a;
    }

}
