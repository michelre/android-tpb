package com.torrenttotransmission.connection;

import android.app.Activity;

import com.torrenttotransmission.activities.R;
import com.torrenttotransmission.model.Connection;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionDialogsController implements Runnable {

    private ScheduledFuture task;
    private ScheduledThreadPoolExecutor scheduledThread;
    private Activity activity;
    private ConnectionDialogs connectionDialogs;
    private CheckActiveConnection launcher;

    public ConnectionDialogsController(Activity a, CheckActiveConnection launcher) {
        this.activity = a;
        this.connectionDialogs = new ConnectionDialogs(a);
        this.scheduledThread = new ScheduledThreadPoolExecutor(1);
        this.launcher = launcher;
    }

    public void checkConnectionState() {
        this.task = this.scheduledThread.scheduleAtFixedRate(this, 0, 2000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (Connection.state){
                    case CONNECTED:
                        connectionDialogs.dismissProgressDialogConnecting();
                        if(launcher != null) launcher.doTask();
                        task.cancel(true);
                        break;
                    case CONNECTING:
                        connectionDialogs.showProgressDialog();
                        break;
                    case CONNECTION_ERROR:
                        connectionDialogs.dismissProgressDialogConnecting();
                        connectionDialogs.getDialogConfigFragment().setTitle(activity.getResources().getString(R.string.transmission_server_configuration_dialog));
                        connectionDialogs.getDialogConfigFragment().setMessage(activity.getResources().getString(R.string.transmission_server_configuration_message_dialog));
                        connectionDialogs.showDialogConfig();
                        task.cancel(true);
                        break;
                    case NO_CONNECTION:
                        connectionDialogs.dismissProgressDialogConnecting();
                        task.cancel(true);
                        break;
                }
            }
        });
    }
}
