package com.android.leezp.learncarproject.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by Leezp on 2018/4/4.
 */

public class MessageCenterEntity extends DataSupport {
    @SerializedName("id")
    private int netId;
    private int userID;
    private String title;
    private String content;
    private String date;
    private int isOpen;

    public MessageCenterEntity() {
    }

    public MessageCenterEntity(int netId, int userID, String title, String content, String date, int isOpen) {
        this.netId = netId;
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.date = date;
        this.isOpen = isOpen;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}
