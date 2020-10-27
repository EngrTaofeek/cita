package com.taofeek.citaa.organization;

public class EventHistoryDataModel {
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
