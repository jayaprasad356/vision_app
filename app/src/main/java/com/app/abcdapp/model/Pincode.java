package com.app.abcdapp.model;

public class Pincode {
    String id,city,pincode;
    public Pincode(){

    }

    public Pincode(String id, String city, String pincode) {
        this.id = id;
        this.city = city;
        this.pincode = pincode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
