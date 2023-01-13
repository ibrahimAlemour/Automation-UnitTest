package com.firstaid.firstaidapp.model;

import java.io.Serializable;

public class Advance implements Serializable {
    private String id;
    private String title;
    private String des;
    public String image;
    public String urlYoutube;

    public Advance(String id, String title, String des, String image, String urlYoutube) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.image = image;
        this.urlYoutube = urlYoutube;
    }

    public Advance() {
    }

    public String getImage() {
        return image;
    }

    public String getUrlYoutube() {
        return urlYoutube;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public String getId() {
        return id;
    }
}
