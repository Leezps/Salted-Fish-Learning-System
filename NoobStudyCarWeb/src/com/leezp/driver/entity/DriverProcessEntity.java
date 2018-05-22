package com.leezp.driver.entity;

public class DriverProcessEntity {
	private int id;
	private int process_order;
	private String title;
	private String content;
	
	public DriverProcessEntity(int id, int process_order, String title, String content) {
		super();
		this.id = id;
		this.process_order = process_order;
		this.title = title;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
