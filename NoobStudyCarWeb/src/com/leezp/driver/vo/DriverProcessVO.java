package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.DriverProcessEntity;

public class DriverProcessVO {
	private int state;
	private String message;
	private List<DriverProcessEntity> entities;
	
	public DriverProcessVO(int state, String message, List<DriverProcessEntity> entities) {
		super();
		this.state = state;
		this.message = message;
		this.entities = entities;
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

	public List<DriverProcessEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<DriverProcessEntity> entities) {
		this.entities = entities;
	}
	
}
