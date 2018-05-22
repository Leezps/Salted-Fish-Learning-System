package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.CompleteOrderEntity;

public class CompleteOrderVO {
	private int state;
	private String message;
	private List<CompleteOrderEntity> complete_orders;
	
	public CompleteOrderVO(int state, String message, List<CompleteOrderEntity> complete_orders) {
		super();
		this.state = state;
		this.message = message;
		this.complete_orders = complete_orders;
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

	public List<CompleteOrderEntity> getComplete_orders() {
		return complete_orders;
	}

	public void setComplete_orders(List<CompleteOrderEntity> complete_orders) {
		this.complete_orders = complete_orders;
	}
	
}
