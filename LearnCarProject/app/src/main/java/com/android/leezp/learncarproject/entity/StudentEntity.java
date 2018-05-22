package com.android.leezp.learncarproject.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StudentEntity implements Serializable{

    @SerializedName("id")
    private int studentId;

    private String phone;

    private String password;

    private String name;

    private int sex;

    private int age;

    @SerializedName("identify_number")
    private String identifyNumber;

    @SerializedName("driver_process")
    private int driverProcess;

    @SerializedName("head_url")
    private String headUrl;

    @SerializedName("identify_image_url")
    private String identifyImageUrl;

    public String getIdentifyImageUrl() {
        return identifyImageUrl;
    }

    public void setIdentifyImageUrl(String identifyImageUrl) {
        this.identifyImageUrl = identifyImageUrl;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDriverProcess() {
        return driverProcess;
    }

    public void setDriverProcess(int driverProcess) {
        this.driverProcess = driverProcess;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdentifyNumber() {
        return identifyNumber;
    }

    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
    }
}
