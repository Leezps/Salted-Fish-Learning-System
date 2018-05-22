package com.android.leezp.learncarproject.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 支付方式的实体
 *
 * Created by Leezp on 2018/4/3.
 */

public class PaymentWayEntity extends DataSupport {
    @SerializedName("id")
    private int netId;
    private int studentID;
    private int paymentType;
    private String paymentName;
    @Column(defaultValue = "false")
    private boolean showRemove;

    public PaymentWayEntity() {
    }

    public PaymentWayEntity(int netId, int studentID, int paymentType, String paymentName) {
        this.netId = netId;
        this.studentID = studentID;
        this.paymentType = paymentType;
        this.paymentName = paymentName;
        showRemove = false;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public boolean isShowRemove() {
        return showRemove;
    }

    public void setShowRemove(boolean showRemove) {
        this.showRemove = showRemove;
    }
}
