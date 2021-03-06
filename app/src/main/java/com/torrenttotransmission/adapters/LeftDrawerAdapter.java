package com.torrenttotransmission.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.torrenttotransmission.activities.R;

public class LeftDrawerAdapter extends BaseAdapter {

    private Context context;
    private String currentActivity;
    private String[] titles = null;
    private int[] iconSrc = {R.drawable.ic_action_web_site, R.drawable.ic_action_labels, R.drawable.ic_action_download_white, R.drawable.ic_action_settings};

    public LeftDrawerAdapter(Context context, String currentActivity){
        this.context = context;
        String[] titles = {context.getString(R.string.title_activity_api), context.getString(R.string.title_category_activity), context.getString(R.string.title_activity_download), context.getString(R.string.title_activity_setting_display)};
        this.titles = titles;
        this.currentActivity = currentActivity;
    }

    @Override
    public int getCount() {
        return this.titles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.drawer_item, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_drawer_item);
        ImageView icon = ((ImageView)linearLayout.findViewById(R.id.drawer_icon));
        TextView label = ((TextView)linearLayout.findViewById(R.id.drawer_text));
        icon.setImageResource(iconSrc[position]);
        label.setText(titles[position]);
        if(titles[position].equals(this.currentActivity)){
            icon.setColorFilter(new LightingColorFilter(Color.DKGRAY, Color.DKGRAY));
            label.setTextColor(Color.DKGRAY);
        }
        return view;
    }
}
