package com.android.leezp.learncartrainproject.entities;

public class BookSuccessOrderAdapterEntity {
    private int netId;
    private String headUrl;
    private String name;
    private String phone;
    private String dateTimeSlot;
    private String place;
    private String price;

    public BookSuccessOrderAdapterEntity(int netId, String headUrl, String name, String phone, String dateTimeSlot, String place, String price) {
        this.netId = netId;
        this.headUrl = headUrl;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
