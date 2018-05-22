package com.leezp.driver.entity;

public class GetPaymentEntity {
	private int id;
	private int studentID;
	private int paymentType;
	private String paymentName;
	
	public GetPaymentEntity(int id, int studentID, int paymentType, String paymentName) {
		super();
		this.id = id;
		this.studentID = studentID;
		this.paymentType = paymentType;
		this.paymentName = paymentName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
}
