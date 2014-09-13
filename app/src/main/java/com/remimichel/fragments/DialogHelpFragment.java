package com.remimichel.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.remimichel.activities.DownloadActivity;
import com.remimichel.activities.SettingActivity;

public class DialogHelpFragment extends DialogFragment {

    private DownloadActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Simple click enables you to stop a download. Long click enables you to remove a download");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    public void setActivity(DownloadActivity activity) {
        this.activity = activity;
    }
}
