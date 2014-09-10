package com.remimichel.services;

import android.app.IntentService;
import android.content.Intent;

import com.remimichel.connection.ConnectionStateTask;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class CheckConnectionService extends IntentService {

    public CheckConnectionService() {
        super("CheckConnectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectionStateTask connectionStateTask = new ConnectionStateTask(this);
        ScheduledThreadPoolExecutor scheduledThreadConnection = new ScheduledThreadPoolExecutor(1);
        scheduledThreadConnection.execute(connectionStateTask);
    }

}
