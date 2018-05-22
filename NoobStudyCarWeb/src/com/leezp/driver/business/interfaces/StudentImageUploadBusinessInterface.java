package com.leezp.driver.business.interfaces;

import com.leezp.driver.entity.StudentEntity;

public interface StudentImageUploadBusinessInterface {
	public boolean isRightStudent(String id, String phone, String password);
	public int changeImageUrl(String code, String path, String id);
	public StudentEntity getStudentInformation(String phone);
}
