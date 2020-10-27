package com.taofeek.citaa;

public class EventDataModel {
    public String email,title,address,description,category,time,date;
    public int capacity;
    public String image_url;

    public EventDataModel(){}

    public EventDataModel(String email, String title, String address, String description,
                          String category, String time, String date, int capacity, String image_url) {
        this.email = email;
        this.title = title;
        this.address = address;
        this.description = description;
        this.category = category;
        this.time = time;
        this.date = date;
        this.capacity = capacity;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }



    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }
}
