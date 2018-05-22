package com.android.leezp.learncartrainproject.entities;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class HelpEntity extends DataSupport{
    @SerializedName("id")
    private int netId;
    private String title;
    private String content;

    public HelpEntity() {
    }

    public HelpEntity(int netId, String title, String content) {
        this.netId = netId;
        this.title = title;
        this.content = content;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
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
}
