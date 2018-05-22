package com.leezp.driver.vo;

import com.leezp.driver.entity.StudentEntity;

public class StudentImageUploadVO {
	private int state;
	private String message;
	private StudentEntity student;
	
	public StudentImageUploadVO(int state, String message, StudentEntity student) {
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
	
}
