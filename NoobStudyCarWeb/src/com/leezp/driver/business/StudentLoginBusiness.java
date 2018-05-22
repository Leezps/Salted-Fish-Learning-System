package com.leezp.driver.business;

import com.leezp.driver.business.interfaces.StudentLoginBusinessInterface;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.StudentEntity;

public class StudentLoginBusiness implements StudentLoginBusinessInterface{

	private StudentDao studentDao = new StudentDao();
	
	@Override
	public StudentEntity login(String phone, String password) {
		StudentEntity entity = studentDao.findStudentByPhone(phone);
		if(entity != null && entity.getPassword().equals(password)) {
			return entity;
		}
		return null;
	}

}
