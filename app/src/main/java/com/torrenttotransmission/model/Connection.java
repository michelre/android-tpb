package com.torrenttotransmission.model;

public final class Connection{

    //public static String hostApi = "http://michelreraspberry.ddns.net";
    public static String hostApi = "http://192.168.1.17";

    public static Api selectedAPI = null;

    public static String host;
    public static String port;
    public static String username;
    public static String password;
    public static boolean authentificationRequired;
    public static String sessionId;
    public static ConnectionState state = ConnectionState.NOT_CONNECTED;

}
