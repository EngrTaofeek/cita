package com.taofeek.cita;

public class FacilityDataModel {
    private  String name;
    private String address;
    private String email;
    private String phone;
    private String overview;
    private String capacity;
    private String others;

    private String image_url;

    public FacilityDataModel() {
        //empty constructor
    }
    public FacilityDataModel (String name,String address, String email, String phone, String overview, String capacity,
                              String others,  String image_url){
        this.address = address;
        this.capacity = capacity;
        this.email = email;
        this.image_url = image_url;
        this.others = others;
        this.overview = overview;
        this.name = name;
        //this.permissible_capacity = permissible_capacity;
        this.phone = phone;

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getOverview() {
        return overview;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getOthers() {
        return others;
    }

//    //public Long getPermissible_capacity() {
//        return permissible_capacity;
//    }

    public String getImage_url() {
        return image_url;
    }

}
