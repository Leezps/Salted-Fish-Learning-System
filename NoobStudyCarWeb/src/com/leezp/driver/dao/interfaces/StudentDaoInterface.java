package com.leezp.driver.dao.interfaces;

import java.util.Map;

import com.leezp.driver.entity.StudentEntity;

public interface StudentDaoInterface {
	//����ѧԱ
	public int countStudentByPhone(String phone);
	public StudentEntity findStudentByPhone(String phone);
	public int studentIDByPhone(String phone);
	
	//���ѧԱ
	public int insertStudent(String phone, String password);
	//�޸�ѧԱ��Ϣ
	public int updateStudent(StudentEntity entity);
	//�޸�����
	public int updateStudentPassword(String phone, String password);
	//�޸�ͷ���ַ
	public int updateStudentHeadUrl(String headUrl, String id);
	//�޸����֤��ַ
	public int updateStudentIdentifyImageUrl(String identifyImageUrl, String id);
	//ͨ��id��ȡѧԱ��ͷ���������ֻ���
	public Map<String, String> findHeadAndNamePhoneById(String id);
}
