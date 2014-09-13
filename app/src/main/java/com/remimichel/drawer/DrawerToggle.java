package com.remimichel.drawer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.remimichel.activities.R;

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
