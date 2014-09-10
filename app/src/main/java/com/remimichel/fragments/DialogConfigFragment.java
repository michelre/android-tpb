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

public class DialogConfigFragment extends DialogFragment {

    private Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Transmission server configuration");
        builder.setMessage("Your transmission server seems not to be well configured yet. You should configure it now!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(DialogConfigFragment.this.getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //DialogConfigFragment.this.activity.dismissDialogConfig();
            }
        });
        return builder.create();
    }

    public void setActivity(Activity a){
        this.activity = a;
    }

}
