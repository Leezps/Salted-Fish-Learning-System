package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.PublishOrderEntity;

public class PublishOrderVO {
	private int state;
	private String message;
	private List<PublishOrderEntity> entities;
	
	public PublishOrderVO(int state, String message, List<PublishOrderEntity> entities) {
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

	public List<PublishOrderEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<PublishOrderEntity> entities) {
		this.entities = entities;
	}
	
}
