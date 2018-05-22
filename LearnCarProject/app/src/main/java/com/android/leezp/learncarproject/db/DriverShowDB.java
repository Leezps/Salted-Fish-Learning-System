package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.DriverEntity;

import java.util.List;

public class DriverShowDB {
    private int state;
    private String message;
    private List<DriverEntity> entities;

    public DriverShowDB(int state, String message, List<DriverEntity> entities) {
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

    public List<DriverEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<DriverEntity> entities) {
        this.entities = entities;
    }
}
