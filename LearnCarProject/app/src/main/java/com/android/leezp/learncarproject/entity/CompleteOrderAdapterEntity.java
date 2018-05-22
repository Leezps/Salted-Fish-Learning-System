package com.android.leezp.learncarproject.entity;

public class CompleteOrderAdapterEntity {
    private int netId;
    private String headUrl;
    private String name;
    private String information;
    private String dateTimeSlot;
    private String place;
    private String price;

    public CompleteOrderAdapterEntity(int netId, String dateTimeSlot, String place, String price) {
        this.netId = netId;
        this.dateTimeSlot = dateTimeSlot;
        this.place = place;
        this.price = price;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDateTimeSlot() {
        return dateTimeSlot;
    }

    public void setDateTimeSlot(String dateTimeSlot) {
        this.dateTimeSlot = dateTimeSlot;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
