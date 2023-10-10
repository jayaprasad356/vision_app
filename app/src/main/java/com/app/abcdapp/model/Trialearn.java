package com.app.abcdapp.model;

public class Trialearn {
    String id,name,mobile,regular_trial,champion_trial,valid;

    public Trialearn(String id, String name, String mobile, String regular_trial, String champion_trial, String valid) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.regular_trial = regular_trial;
        this.champion_trial = champion_trial;
        this.valid = valid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegular_trial() {
        return regular_trial;
    }

    public void setRegular_trial(String regular_trial) {
        this.regular_trial = regular_trial;
    }

    public String getChampion_trial() {
        return champion_trial;
    }

    public void setChampion_trial(String champion_trial) {
        this.champion_trial = champion_trial;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
