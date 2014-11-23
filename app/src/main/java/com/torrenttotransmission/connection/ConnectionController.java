package com.torrenttotransmission.connection;

import android.app.Activity;
import android.content.Intent;

import com.torrenttotransmission.model.Connection;
import com.torrenttotransmission.model.ConnectionState;
import com.torrenttotransmission.services.CheckConnectionService;

public class ConnectionController {

    private ConnectionDialogsController connectionDialogsController;
    private Intent intent;
    private Activity activity;

    public ConnectionController(Activity activity, ConnectionDialogsController connectionDialogsController){
        this.activity = activity;
        this.connectionDialogsController = connectionDialogsController;
        intent = new Intent(this.activity, CheckConnectionService.class);
    }

    public void checkConnectionState(){
        if(Connection.state == ConnectionState.NOT_CONNECTED || Connection.state == ConnectionState.CONNECTION_ERROR || Connection.state == ConnectionState.NO_CONNECTION){
            this.activity.startService(intent);
        }
    }

    public void notifyConnectionState(){
        this.connectionDialogsController.checkConnectionState();
    }

    public void resetConnectionState() {
        Connection.state = ConnectionState.NOT_CONNECTED;
    }

}