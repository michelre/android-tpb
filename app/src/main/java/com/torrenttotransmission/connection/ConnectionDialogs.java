package com.torrenttotransmission.connection;

import android.app.Activity;
import android.app.ProgressDialog;

import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.fragments.DialogConfigFragment;

public class ConnectionDialogs {

    private ProgressDialog progressDialogConnecting;
    private DialogConfigFragment dialogConfigFragment;
    private Activity activity;

    public ConnectionDialogs(Activity activity){
        this.activity = activity;
        this.dialogConfigFragment = new DialogConfigFragment();
        this.dialogConfigFragment.setActivity(activity);
    }

    public void showProgressDialog(){
        if(this.progressDialogConnecting == null)
            this.progressDialogConnecting = ProgressDialog.show(this.activity, "", activity.getResources().getString(R.string.connecting_transmission));
    }

    public void dismissProgressDialogConnecting(){
        if(this.progressDialogConnecting != null){
            this.progressDialogConnecting.dismiss();
        }
    }

    public void showDialogConfig() {
        if (!this.dialogConfigFragment.isAdded()) {
            this.dialogConfigFragment.show(this.activity.getFragmentManager(), "settings_dialog");
        }
    }

    public DialogConfigFragment getDialogConfigFragment() {
        return dialogConfigFragment;
    }
}
