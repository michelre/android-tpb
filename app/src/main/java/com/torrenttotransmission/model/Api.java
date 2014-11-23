package com.torrenttotransmission.model;

public class Api {

    private String host;
    private String port;
    private String name;
    private int logo;
    private String pseudo;
    private String password;
    private String authorization;

    public Api(String host, String port, String name, int logo){
        this.host = host;
        this.name = name;
        this.logo = logo;
        this.port = port;
    }

    public Api(String host, String name, int logo, String pseudo, String password) {
        this.host = host;
        this.name = name;
        this.logo = logo;
        this.pseudo = pseudo;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
