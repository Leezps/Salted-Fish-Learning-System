package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.StudentEntity;
import com.google.gson.annotations.SerializedName;

public class RegisterDB {
    private int state;
    private String message;
    @SerializedName("verification_id")
    private String verificationId;
    private StudentEntity student;

    public RegisterDB(int state, String message, String verificationId, StudentEntity student) {
        this.state = state;
        this.message = message;
        this.verificationId = verificationId;
        this.student = student;
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

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }
}
