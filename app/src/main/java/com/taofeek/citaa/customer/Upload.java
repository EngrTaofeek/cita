package com.taofeek.citaa.customer;

import java.io.Serializable;

public class Upload implements Serializable {
    private String name;
    private String imageurl;
    public Upload() {
        //empty constructor needed
    }
    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        imageurl = imageUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
