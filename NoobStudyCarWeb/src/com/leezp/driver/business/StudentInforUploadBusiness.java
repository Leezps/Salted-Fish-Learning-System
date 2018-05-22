package com.leezp.driver.business;

import com.leezp.driver.business.interfaces.StudentInforUploadBusinessInterface;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.StudentEntity;

public class StudentInforUploadBusiness implements StudentInforUploadBusinessInterface{
	private StudentDao studentDao = new StudentDao();

	@Override
	public int updateStudentInfor(StudentEntity entity) {
		if (entity == null) {
			return -1;
		}
		StudentEntity student = studentDao.findStudentByPhone(entity.getPhone());
		if(student == null || student.getId() != entity.getId()) {
			return -1;
		} else {
			student.setName(entity.getName());
			student.setSex(entity.getSex());
			student.setAge(entity.getAge());
			student.setIdentify_number(entity.getIdentify_number());
			student.setDriver_process(entity.getDriver_process());
			return studentDao.updateStudent(student);
		}
	}

}
