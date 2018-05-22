package com.android.leezp.learncartrainproject.net.data;

import com.android.leezp.learncartrainproject.entities.DriverEntity;

public class NetDriverData {
    private int state;
    private String message;
    private String verification_id;
    private DriverEntity driver;

    public NetDriverData(int state, String message, String verification_id, DriverEntity driver) {
        this.state = state;
        this.message = message;
        this.verification_id = verification_id;
        this.driver = driver;
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

    public String getVerification_id() {
        return verification_id;
    }

    public void setVerification_id(String verification_id) {
        this.verification_id = verification_id;
    }

    public DriverEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
    }
}
