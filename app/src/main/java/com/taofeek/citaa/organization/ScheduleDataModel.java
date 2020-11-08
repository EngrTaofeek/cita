package com.taofeek.citaa.organization;

import androidx.annotation.Keep;

import java.io.Serializable;
@Keep
public class ScheduleDataModel implements Serializable {
    public String date;
    public String time;
    public String email;
    public String name;

    public String getStatus() {
        return status;
    }

    private String status;

    public String getName() {
        return name;
    }



    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEmail() {
        return email;
    }

    //private String image_url;
    public ScheduleDataModel(){
        //EMPTY CONSTRUCTOR

    }
    public ScheduleDataModel(String date, String time, String email, String name , String status){
        this.date = date;
        this.time = time;
        this.email = email;
        this.name = name;
        this.status = status;
    }


}
