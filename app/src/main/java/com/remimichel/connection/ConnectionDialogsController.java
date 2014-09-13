package com.remimichel.connection;

import android.app.Activity;

import com.remimichel.model.Connection;
import com.remimichel.model.ConnectionState;

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
                if (Connection.state == ConnectionState.CONNECTED) {
                    ConnectionDialogsController.this.connectionDialogs.dismissProgressDialogConnecting();
                    if(ConnectionDialogsController.this.launcher != null)
                        ConnectionDialogsController.this.launcher.doTask();
                    ConnectionDialogsController.this.task.cancel(false);
                }
                if (Connection.state == ConnectionState.CONNECTING) {
                    ConnectionDialogsController.this.connectionDialogs.showProgressDialog();
                }
                if (Connection.state == ConnectionState.CONNECTION_ERROR) {
                    ConnectionDialogsController.this.connectionDialogs.dismissProgressDialogConnecting();
                    ConnectionDialogsController.this.connectionDialogs.showDialogConfig();
                    ConnectionDialogsController.this.task.cancel(false);
                }
            }
        });
    }
}
