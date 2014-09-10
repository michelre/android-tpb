package com.remimichel.connection;

import android.app.Activity;
import android.util.Log;

import com.remimichel.model.Connection;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionStateController implements Runnable {

    private ScheduledFuture task;
    private ScheduledThreadPoolExecutor scheduledThread;
    private Activity activity;
    private ConnectionDialogs connectionDialogs;
    private CheckActiveConnection launcher;

    public ConnectionStateController(Activity a, CheckActiveConnection launcher) {
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
                if (Connection.state == "CONNECTED") {
                    ConnectionStateController.this.connectionDialogs.dismissProgressDialogConnecting();
                    ConnectionStateController.this.launcher.doTask();
                    ConnectionStateController.this.task.cancel(false);
                }
                if (Connection.state == "CONNECTING") {
                    Log.e("Connecting", "Connecting...");
                    ConnectionStateController.this.connectionDialogs.showProgressDialog();
                }
                if (Connection.state == "CONNECTION_ERROR") {
                    Log.e("Connection error", "Connection error");
                    ConnectionStateController.this.connectionDialogs.dismissProgressDialogConnecting();
                    ConnectionStateController.this.connectionDialogs.showDialogConfig();
                    ConnectionStateController.this.task.cancel(false);
                }
            }
        });
    }
}
