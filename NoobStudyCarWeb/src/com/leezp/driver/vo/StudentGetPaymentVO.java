package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.GetPaymentEntity;

public class StudentGetPaymentVO {
	private int state;
	private String message;
	private List<GetPaymentEntity> payment_way;
	
	public StudentGetPaymentVO(int state, String message, List<GetPaymentEntity> payment_way) {
		super();
		this.state = state;
		this.message = message;
		this.payment_way = payment_way;
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

	public List<GetPaymentEntity> getPayment_way() {
		return payment_way;
	}

	public void setPayment_way(List<GetPaymentEntity> payment_way) {
		this.payment_way = payment_way;
	}
	
}
