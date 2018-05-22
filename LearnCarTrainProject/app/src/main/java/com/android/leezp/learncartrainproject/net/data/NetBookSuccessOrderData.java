package com.android.leezp.learncartrainproject.net.data;

import com.android.leezp.learncartrainproject.entities.BookSuccessOrderEntity;

import java.util.List;

public class NetBookSuccessOrderData {
    private int state;
    private String message;
    private List<BookSuccessOrderEntity> complete_orders;

    public NetBookSuccessOrderData(int state, String message, List<BookSuccessOrderEntity> complete_orders) {
        this.state = state;
        this.message = message;
        this.complete_orders = complete_orders;
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

    public List<BookSuccessOrderEntity> getComplete_orders() {
        return complete_orders;
    }

    public void setComplete_orders(List<BookSuccessOrderEntity> complete_orders) {
        this.complete_orders = complete_orders;
    }
}
