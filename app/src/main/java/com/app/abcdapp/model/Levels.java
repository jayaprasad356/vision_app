package com.app.abcdapp.model;

public class Levels {
    String id,level,codes,cost,total_refers;
    public Levels(){

    }

    public Levels(String id, String level, String codes, String cost, String total_refers) {
        this.id = id;
        this.level = level;
        this.codes = codes;
        this.cost = cost;
        this.total_refers = total_refers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTotal_refers() {
        return total_refers;
    }

    public void setTotal_refers(String total_refers) {
        this.total_refers = total_refers;
    }
}
