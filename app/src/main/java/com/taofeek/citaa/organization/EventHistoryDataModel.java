package com.taofeek.citaa.organization;

import androidx.annotation.Keep;

import java.io.Serializable;
@Keep
public class EventHistoryDataModel implements Serializable {
    public String title,date,time;

    public EventHistoryDataModel(String title, String date, String time) {
        this.title = title;

        this.date = date;
        this.time = time;
    }

    public EventHistoryDataModel() {
    }

    public String getTitle() {
        return title;
    }


    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
