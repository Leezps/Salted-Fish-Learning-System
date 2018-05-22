package com.android.leezp.learncartrainproject.entities;

import com.google.gson.annotations.SerializedName;

public class BookSuccessOrderEntity {
    @SerializedName("id")
    private int netId;
    private int publishOrderID;
    private int driverID;
    private int studentID;
    private String beginTime;
    private String endTime;
    private String place;
    private float price;

    public BookSuccessOrderEntity(int netId, int publishOrderID, int driverID, int studentID, String beginTime, String endTime, String place, float price) {
        this.netId = netId;
        this.publishOrderID = publishOrderID;
        this.driverID = driverID;
        this.studentID = studentID;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.place = place;
        this.price = price;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getPublishOrderID() {
        return publishOrderID;
    }

    public void setPublishOrderID(int publishOrderID) {
        this.publishOrderID = publishOrderID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
