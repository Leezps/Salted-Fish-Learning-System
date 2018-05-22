package com.leezp.driver.entity;

public class StudentEntity {

	private int id;
	private String phone;
	private String password;
	private String name;
	private int sex;
	private int age;
	private String identify_number;
	private int driver_process;
	private String head_url;
	private String identify_image_url;
	
	public StudentEntity(int id, String phone, String password, String name, int sex, int age, String identify_number,
			int driver_process, String head_url, String identify_image_url) {
		super();
		this.id = id;
		this.phone = phone;
		this.password = password;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.identify_number = identify_number;
		this.driver_process = driver_process;
		this.head_url = head_url;
		this.identify_image_url = identify_image_url;
	}

	public StudentEntity(String phone, String password) {
		super();
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getIdentify_number() {
		return identify_number;
	}

	public void setIdentify_number(String identify_number) {
		this.identify_number = identify_number;
	}

	public int getDriver_process() {
		return driver_process;
	}

	public void setDriver_process(int driver_process) {
		this.driver_process = driver_process;
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
	
	
	
}
