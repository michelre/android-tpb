package com.remimichel.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import com.remimichel.activities.R;
import com.remimichel.connection.ConnectionController;
import com.remimichel.connection.ConnectionDialogsController;
import com.remimichel.fragments.SettingsFragment;
import com.remimichel.model.Connection;
import com.remimichel.model.ConnectionState;
import com.remimichel.services.CheckConnectionService;

public class SettingActivity extends Activity{

    private ConnectionController connectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        connectionController = new ConnectionController(this, new ConnectionDialogsController(this, null));
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        connectionController.resetConnectionState();
        connectionController.checkConnectionState();
    }
}
