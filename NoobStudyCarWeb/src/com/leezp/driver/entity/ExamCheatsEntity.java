package com.leezp.driver.entity;

public class ExamCheatsEntity {
	private int id;
	private int process_order;
	private String content;
	
	public ExamCheatsEntity(int id, int process_order, String content) {
		super();
		this.id = id;
		this.process_order = process_order;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProcess_order() {
		return process_order;
	}

	public void setProcess_order(int process_order) {
		this.process_order = process_order;
	}

	public String getContents() {
		return content;
	}

	public void setContents(String content) {
		this.content = content;
	}
	
}
