package com.remimichel.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.remimichel.services.CheckConnectionService;

import java.util.List;

public final class Connection{

    public static String hostApi = "http://michelreraspberry.ddns.net";
    //public static String hostApi = "http://192.168.1.17";

    public static String host;
    public static String port;
    public static String username;
    public static String password;
    public static boolean authentificationRequired;
    public static String sessionId;
    public static ConnectionState state = ConnectionState.NOT_CONNECTED;

}
