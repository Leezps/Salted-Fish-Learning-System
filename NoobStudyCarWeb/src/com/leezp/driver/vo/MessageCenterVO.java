package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.MessageCenterEntity;

public class MessageCenterVO {
	private int state;
	private String message;
	private List<MessageCenterEntity> message_center;
	
	public MessageCenterVO(int state, String message, List<MessageCenterEntity> message_center) {
		super();
		this.state = state;
		this.message = message;
		this.setMessage_center(message_center);
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

	public List<MessageCenterEntity> getMessage_center() {
		return message_center;
	}

	public void setMessage_center(List<MessageCenterEntity> message_center) {
		this.message_center = message_center;
	}
	
}
