package com.torrenttotransmission.listeners;

import android.app.ListActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.torrenttotransmission.activities.R;

public class ErrorResponseSearchListener implements Response.ErrorListener{

    private ListActivity activity;

    public ErrorResponseSearchListener(ListActivity activity){
        this.activity = activity;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_error,
                (ViewGroup) this.activity.findViewById(R.id.toast_error));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(activity.getResources().getString(R.string.error_api_connection));

        Toast toast = new Toast(this.activity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
