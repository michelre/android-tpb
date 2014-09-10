package com.remimichel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class Connection{

    public static String hostApi = "http://92.129.147.77";
    //public static String hostApi = "http://192.168.1.17";

    public static String host;
    public static String port;
    public static String username;
    public static String password;
    public static String sessionId;
    public static String state = "NOT_CONNECTED";


}
