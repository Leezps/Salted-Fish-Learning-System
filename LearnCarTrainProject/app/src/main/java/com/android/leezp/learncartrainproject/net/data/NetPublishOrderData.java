package com.android.leezp.learncartrainproject.net.data;

import com.android.leezp.learncartrainproject.entities.PublishOrderEntity;

import java.util.List;

public class NetPublishOrderData {
    private int state;
    private String message;
    private List<PublishOrderEntity> entities;

    public NetPublishOrderData(int state, String message, List<PublishOrderEntity> entities) {
        this.state = state;
        this.message = message;
        this.entities = entities;
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

    public List<PublishOrderEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<PublishOrderEntity> entities) {
        this.entities = entities;
    }
}
