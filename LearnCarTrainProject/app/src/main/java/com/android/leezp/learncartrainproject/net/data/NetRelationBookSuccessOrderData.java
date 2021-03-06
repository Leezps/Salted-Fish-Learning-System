package com.android.leezp.learncartrainproject.net.data;

public class NetRelationBookSuccessOrderData {
    private int state;
    private String message;
    private String order_id;
    private String phone;
    private String name;
    private String head_url;

    public NetRelationBookSuccessOrderData(int state, String message, String order_id, String phone, String name, String head_url) {
        this.state = state;
        this.message = message;
        this.order_id = order_id;
        this.phone = phone;
        this.name = name;
        this.head_url = head_url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }
}
