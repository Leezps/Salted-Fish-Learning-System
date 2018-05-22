package com.leezp.driver.entity;

public class MessageCenterEntity {
	private int id;
	private int userID;
	private String title;
	private String content;
	private String date;
	private int isOpen;
	
	public MessageCenterEntity(int id, int userID, String title, String content, String date,
			int isOpen) {
		super();
		this.id = id;
		this.setUserID(userID);
		this.title = title;
		this.content = content;
		this.date = date;
		this.isOpen = isOpen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	
}
