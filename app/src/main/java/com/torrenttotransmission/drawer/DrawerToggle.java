package com.torrenttotransmission.drawer;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.torrenttotransmission.activities.R;

public class DrawerToggle {

    private ActionBarDrawerToggle actionBarDrawerToggle;

    public DrawerToggle(Activity activity, DrawerLayout drawerLayout){
        this.actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.drawable.ic_drawer, 0, 0){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return actionBarDrawerToggle;
    }
}
