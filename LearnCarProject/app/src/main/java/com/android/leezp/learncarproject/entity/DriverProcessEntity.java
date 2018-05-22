package com.android.leezp.learncarproject.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class DriverProcessEntity extends DataSupport {
    @SerializedName("id")
    private int netId;
    @Column(unique = true, defaultValue = "0")
    private int process_order;
    private String title;
    private String content;

    public DriverProcessEntity() {
    }

    public DriverProcessEntity(int netId, int process_order, String title, String content) {
        this.netId = netId;
        this.process_order = process_order;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getProcess_order() {
        return process_order;
    }

    public void setProcess_order(int process_order) {
        this.process_order = process_order;
    }
}
