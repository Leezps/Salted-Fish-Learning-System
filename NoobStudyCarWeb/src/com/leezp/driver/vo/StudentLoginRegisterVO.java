package com.leezp.driver.vo;

import com.leezp.driver.entity.StudentEntity;

public class StudentLoginRegisterVO {
	private int state;
	private String message;
	private String verification_id = null;
	private StudentEntity student;
	
	public StudentLoginRegisterVO(int state, String message, StudentEntity student) {
		super();
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

	public String getVerification_id() {
		return verification_id;
	}

	public void setVerification_id(String verification_id) {
		this.verification_id = verification_id;
	}

}
