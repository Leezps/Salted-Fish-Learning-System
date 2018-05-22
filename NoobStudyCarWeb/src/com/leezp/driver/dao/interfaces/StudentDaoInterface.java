package com.leezp.driver.dao.interfaces;

import java.util.Map;

import com.leezp.driver.entity.StudentEntity;

public interface StudentDaoInterface {
	//查找学员
	public int countStudentByPhone(String phone);
	public StudentEntity findStudentByPhone(String phone);
	public int studentIDByPhone(String phone);
	
	//添加学员
	public int insertStudent(String phone, String password);
	//修改学员信息
	public int updateStudent(StudentEntity entity);
	//修改密码
	public int updateStudentPassword(String phone, String password);
	//修改头像地址
	public int updateStudentHeadUrl(String headUrl, String id);
	//修改身份证地址
	public int updateStudentIdentifyImageUrl(String identifyImageUrl, String id);
	//通过id获取学员的头像、姓名、手机号
	public Map<String, String> findHeadAndNamePhoneById(String id);
}
