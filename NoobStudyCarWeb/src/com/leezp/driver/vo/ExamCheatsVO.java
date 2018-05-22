package com.leezp.driver.vo;

import java.util.List;

import com.leezp.driver.entity.ExamCheatsEntity;

public class ExamCheatsVO {
	private int state;
	private String message;
	private List<ExamCheatsEntity> examCheats;
	
	public ExamCheatsVO(int state, String message, List<ExamCheatsEntity> examCheats) {
		super();
		this.state = state;
		this.message = message;
		this.examCheats = examCheats;
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

	public List<ExamCheatsEntity> getExamCheats() {
		return examCheats;
	}

	public void setExamCheats(List<ExamCheatsEntity> examCheats) {
		this.examCheats = examCheats;
	}
	
}
