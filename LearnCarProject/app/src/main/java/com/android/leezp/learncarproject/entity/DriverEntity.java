package com.android.leezp.learncarproject.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class DriverEntity extends DataSupport{
    @SerializedName("id")
    private int netId;
    private String phone;
    private String name;
    private int sex;
    private String place;
    private float evaluate;
    private String information;
    private String head_url;
    private int taught_people;
    private float unit_price;

    public DriverEntity() {
    }

    public DriverEntity(int netId, String phone, String name, int sex, String place, float evaluate, String information, String head_url, int taught_people, float unit_price) {
        this.netId = netId;
        this.phone = phone;
        this.name = name;
        this.sex = sex;
        this.place = place;
        this.evaluate = evaluate;
        this.information = information;
        this.head_url = head_url;
        this.taught_people = taught_people;
        this.unit_price = unit_price;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(float evaluate) {
        this.evaluate = evaluate;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public int getTaught_people() {
        return taught_people;
    }

    public void setTaught_people(int taught_people) {
        this.taught_people = taught_people;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }
}
