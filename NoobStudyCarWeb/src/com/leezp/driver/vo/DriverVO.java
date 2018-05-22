package com.leezp.driver.vo;

import com.leezp.driver.entity.DriverEntity;

public class DriverVO {
	private int state;
	private String message;
	private String verification_id = null;
	private DriverEntity driver;
	
	public DriverVO(int state, String message, DriverEntity driver) {
		super();
		this.state = state;
		this.message = message;
		this.driver = driver;
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

	public String getVerification_id() {
		return verification_id;
	}

	public void setVerification_id(String verification_id) {
		this.verification_id = verification_id;
	}

	public DriverEntity getDriver() {
		return driver;
	}

	public void setDriver(DriverEntity driver) {
		this.driver = driver;
	}
	
}
