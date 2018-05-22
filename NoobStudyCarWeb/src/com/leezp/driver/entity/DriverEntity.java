package com.leezp.driver.entity;

public class DriverEntity {
	private int id;
	private String phone;
	private String password;
	private String name;
	private int sex;
	private String place;
	private float evaluate;
	private String identify_number;
	private String information;
	private String head_url;
	private String identify_image_url;
	private String trainer_image_url;
	private int taught_people;
	private float unit_price;
	
	public DriverEntity(int id, String phone, String password, String name, int sex, String place, float evaluate,
			String identify_number, String information, String head_url, String identify_image_url,
			String trainer_image_url, int taught_people, float unit_price) {
		super();
		this.id = id;
		this.phone = phone;
		this.password = password;
		this.name = name;
		this.sex = sex;
		this.place = place;
		this.evaluate = evaluate;
		this.identify_number = identify_number;
		this.information = information;
		this.head_url = head_url;
		this.identify_image_url = identify_image_url;
		this.trainer_image_url = trainer_image_url;
		this.taught_people = taught_people;
		this.unit_price = unit_price;
	}

	public DriverEntity(int id, String phone, String password) {
		super();
		this.id = id;
		this.phone = phone;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public float getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(float evaluate) {
		this.evaluate = evaluate;
	}

	public String getIdentify_number() {
		return identify_number;
	}

	public void setIdentify_number(String identify_number) {
		this.identify_number = identify_number;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getIdentify_image_url() {
		return identify_image_url;
	}

	public void setIdentify_image_url(String identify_image_url) {
		this.identify_image_url = identify_image_url;
	}

	public String getTrainer_image_url() {
		return trainer_image_url;
	}

	public void setTrainer_image_url(String trainer_image_url) {
		this.trainer_image_url = trainer_image_url;
	}

	public int getTaught_people() {
		return taught_people;
	}

	public void setTaught_people(int taught_people) {
		this.taught_people = taught_people;
	}

	public float getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(float unit_price) {
		this.unit_price = unit_price;
	}
	
}
