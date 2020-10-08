package com.taofeek.cita.organization;

public class ScheduleDataModel {
    private String date;
    private String time;
    private String email;
    private String name;

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
    public ScheduleDataModel(String date, String time, String email){
        this.date = date;
        this.time = time;
        this.email = email;
    }


}
