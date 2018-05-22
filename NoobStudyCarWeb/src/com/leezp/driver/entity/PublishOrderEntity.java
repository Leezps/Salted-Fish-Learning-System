package com.leezp.driver.entity;

public class PublishOrderEntity {
	private int id;
	private int driverID;
	private String beginTime;
	private String endTime;
	
	public PublishOrderEntity(int id, int driverID, String beginTime, String endTime) {
		super();
		this.id = id;
		this.driverID = driverID;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDriverID() {
		return driverID;
	}

	public void setDriverID(int driverID) {
		this.driverID = driverID;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
