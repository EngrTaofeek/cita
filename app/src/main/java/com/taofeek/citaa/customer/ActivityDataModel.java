package com.taofeek.citaa.customer;

import androidx.annotation.Keep;

import java.io.Serializable;
@Keep
public class ActivityDataModel implements Serializable {
    public String date,time,name,status;
    public ActivityDataModel(){

    }

    public ActivityDataModel(String date, String time, String name, String status) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
