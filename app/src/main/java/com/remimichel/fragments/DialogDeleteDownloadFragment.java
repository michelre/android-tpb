package com.remimichel.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.remimichel.activities.DownloadActivity;
import com.remimichel.activities.R;
import com.remimichel.activities.SettingActivity;
import com.remimichel.listeners.DownloadLongClickListener;

import org.w3c.dom.Text;

public class DialogDeleteDownloadFragment extends DialogFragment {

    private DownloadLongClickListener downloadLongClickListener;
    private Activity activity;
    private View layoutDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.layoutDialog = this.activity.getLayoutInflater().inflate(R.layout.dialog_delete_download, null);
        TextView text = (TextView)layoutDialog.findViewById(R.id.remove_download_text);
        text.setText(text.getText() + downloadLongClickListener.getDownloadToDelete().getName() + " ?");
        builder.setView(layoutDialog);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CheckBox checkBox = (CheckBox)layoutDialog.findViewById(R.id.checkbox_remove_local_data);
                DialogDeleteDownloadFragment.this.downloadLongClickListener.doDelete(checkBox.isChecked());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        return builder.create();
    }

    public void setDownloadLongClickListener(DownloadLongClickListener downloadLongClickListener){
        this.downloadLongClickListener = downloadLongClickListener;
    }

    public void setActivity(Activity a){
        this.activity = a;
    }

}
