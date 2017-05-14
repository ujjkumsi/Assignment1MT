package com.mindtickle.assignment1.models;

import java.io.Serializable;

/**
 * Created by beyonder on 14/5/17.
 */

public class Photo implements Serializable{

    private String id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private boolean ispublic;
    private boolean isfamily;
    private boolean isfriend;
    private String url_n;
    private boolean has_comment;
    private int height_n;
    private String width_n;

    public Photo(){

    }

    public Photo(String id, String owner, String secret, String server, int farm, String title, boolean ispublic, boolean isfamily, boolean isfriend, boolean url_n, String url_n1, int height_n, String width_n) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.isfamily = isfamily;
        this.isfriend = isfriend;
        this.url_n = url_n1;
        this.height_n = height_n;
        this.width_n = width_n;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean ispublic() {
        return ispublic;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public boolean isfamily() {
        return isfamily;
    }

    public void setIsfamily(boolean isfamily) {
        this.isfamily = isfamily;
    }

    public boolean isfriend() {
        return isfriend;
    }

    public void setIsfriend(boolean isfriend) {
        this.isfriend = isfriend;
    }

    public boolean isHas_comment() {
        return has_comment;
    }

    public void setHas_comment(boolean has_comment) {
        this.has_comment = has_comment;
    }


    public int getHeight_n() {
        return height_n;
    }

    public void setHeight_n(int height_n) {
        this.height_n = height_n;
    }

    public String getWidth_n() {
        return width_n;
    }

    public void setWidth_n(String width_n) {
        this.width_n = width_n;
    }

    public String getUrl_n() {
        return url_n;
    }

    public void setUrl_n(String url_n) {
        this.url_n = url_n;
    }
}
