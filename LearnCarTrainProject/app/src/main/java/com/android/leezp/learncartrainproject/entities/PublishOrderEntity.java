package com.android.leezp.learncartrainproject.entities;

import com.google.gson.annotations.SerializedName;

public class PublishOrderEntity {
    @SerializedName("id")
    private int netId;
    private int driverID;
    private String beginTime;
    private String endTime;

    public PublishOrderEntity(int netId, int driverID, String beginTime, String endTime) {
        this.netId = netId;
        this.driverID = driverID;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
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
}
