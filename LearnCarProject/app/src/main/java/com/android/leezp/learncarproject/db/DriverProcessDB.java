package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.DriverProcessEntity;

import java.util.List;

public class DriverProcessDB {
    private int state;
    private String message;
    private List<DriverProcessEntity> entities;

    public DriverProcessDB(int state, String message, List<DriverProcessEntity> entities) {
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

    public List<DriverProcessEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<DriverProcessEntity> entities) {
        this.entities = entities;
    }
}
