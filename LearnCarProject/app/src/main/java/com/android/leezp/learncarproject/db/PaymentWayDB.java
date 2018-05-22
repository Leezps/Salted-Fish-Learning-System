package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.PaymentWayEntity;

import java.util.List;

public class PaymentWayDB {
    private int state;
    private String message;
    private List<PaymentWayEntity> payment_way;

    public PaymentWayDB(int state, String message, List<PaymentWayEntity> payment_way) {
        this.state = state;
        this.message = message;
        this.payment_way = payment_way;
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

    public List<PaymentWayEntity> getPayment_way() {
        return payment_way;
    }

    public void setPayment_way(List<PaymentWayEntity> payment_way) {
        this.payment_way = payment_way;
    }
}
