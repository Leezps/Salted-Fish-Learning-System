package com.android.leezp.learncartrainproject.net.data;

import com.android.leezp.learncartrainproject.entities.MessageCenterEntity;

import java.util.List;

public class NetMessageCenterData {
    private int state;
    private String message;
    private List<MessageCenterEntity> message_center;

    public NetMessageCenterData(int state, String message, List<MessageCenterEntity> message_center) {
        this.state = state;
        this.message = message;
        this.message_center = message_center;
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

    public List<MessageCenterEntity> getMessage_center() {
        return message_center;
    }

    public void setMessage_center(List<MessageCenterEntity> message_center) {
        this.message_center = message_center;
    }
}
