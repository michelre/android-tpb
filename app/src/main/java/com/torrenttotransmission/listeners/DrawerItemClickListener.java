package com.torrenttotransmission.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.torrenttotransmission.activities.APIActivity;
import com.torrenttotransmission.activities.CategoryActivity;
import com.torrenttotransmission.activities.DownloadActivity;
import com.torrenttotransmission.activities.SettingActivity;

public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private Activity activity;

    public DrawerItemClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = null;
        switch (position){
            case 0:
                intent = new Intent(this.activity, APIActivity.class);
                this.activity.startActivity(intent);
                break;
            case 1:
                intent = new Intent(this.activity, CategoryActivity.class);
                this.activity.startActivity(intent);
                break;
            case 2:
                intent = new Intent(this.activity, DownloadActivity.class);
                this.activity.startActivity(intent);
                break;
            case 3:
                intent = new Intent(this.activity, SettingActivity.class);
                this.activity.startActivity(intent);
                break;
        }
    }
}
