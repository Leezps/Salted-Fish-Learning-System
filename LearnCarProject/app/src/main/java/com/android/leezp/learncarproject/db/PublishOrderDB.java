package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.PublishOrderEntity;

import java.util.List;

public class PublishOrderDB {
    private int state;
    private String message;
    private List<PublishOrderEntity> entities;

    public PublishOrderDB(int state, String message, List<PublishOrderEntity> entities) {
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
