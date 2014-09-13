package com.remimichel.connection;

import android.app.Activity;
import android.content.Intent;
import com.remimichel.model.Connection;
import com.remimichel.model.ConnectionState;
import com.remimichel.services.CheckConnectionService;

public class ConnectionController {

    private final ConnectionDialogsController connectionDialogsController;
    private final Intent intent;
    private Activity activity;

    public ConnectionController(Activity activity, ConnectionDialogsController connectionDialogsController){
        this.activity = activity;
        this.connectionDialogsController = connectionDialogsController;
        intent = new Intent(this.activity, CheckConnectionService.class);
    }

    public void checkConnectionState(){
        if(Connection.state == ConnectionState.NOT_CONNECTED || Connection.state == ConnectionState.CONNECTION_ERROR){            
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