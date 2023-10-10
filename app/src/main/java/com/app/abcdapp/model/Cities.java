package com.app.abcdapp.model;

public class Cities {
    String id,city,mobile_no,name;
    public Cities(){

    }

    public Cities(String id, String city, String mobile_no, String name) {
        this.id = id;
        this.city = city;
        this.mobile_no = mobile_no;
        this.name = name;
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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
