package com.android.leezp.learncarproject.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class ExamCheatsEntity extends DataSupport{
    @SerializedName("id")
    private int netId;
    private int process_order;
    private String content;

    public ExamCheatsEntity() {
    }

    public ExamCheatsEntity(int netId, int process_order, String content) {
        this.netId = netId;
        this.process_order = process_order;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
