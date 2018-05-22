package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.StudentEntity;

public class LoginDB {
    private int state;
    private String message;
    private StudentEntity student;

    public LoginDB(int state, String message, StudentEntity student) {
        this.state = state;
        this.message = message;
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

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }
}
