package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.DriverEntity;

public class DriverShowVO {
	private int state;
	private String message;
	private List<DriverEntity> entities;
	
	public DriverShowVO(int state, String message, List<DriverEntity> entities) {
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

	public List<DriverEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<DriverEntity> entities) {
		this.entities = entities;
	}
	
}
