package com.leezp.driver.business;

import com.leezp.driver.business.interfaces.StudentImageUploadBusinessInterface;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.StudentEntity;

public class StudentImageUploadBusiness implements StudentImageUploadBusinessInterface{
	StudentDao student = new StudentDao();

	@Override
	public int changeImageUrl(String code, String path, String id) {
		if(code.equals("0")) {
			return student.updateStudentHeadUrl(path, id);
		} else if(code.equals("1")) {
			return student.updateStudentIdentifyImageUrl(path, id);
		}
		return -1;
	}

	@Override
	public boolean isRightStudent(String id, String phone, String password) {
		StudentEntity entity = student.findStudentByPhone(phone);
		if(entity == null || !String.valueOf(entity.getId()).equals(id) || !entity.getPassword().equals(password)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public StudentEntity getStudentInformation(String phone) {
		return student.findStudentByPhone(phone);
	}

}
