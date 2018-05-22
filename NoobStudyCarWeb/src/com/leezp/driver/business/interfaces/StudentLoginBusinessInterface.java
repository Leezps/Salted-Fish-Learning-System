package com.leezp.driver.business.interfaces;

import com.leezp.driver.entity.StudentEntity;

public interface StudentLoginBusinessInterface {
	public StudentEntity login(String phone, String password);
}
