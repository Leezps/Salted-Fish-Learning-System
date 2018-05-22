package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.MessageCenterEntity;

public interface MessageCenterDaoInterface {
	//������ȡ��Ϣ����
	List<MessageCenterEntity> getDriverMessageCenter(String driverID);
	//����ɾ����Ϣ
	int removeDriverMessage(String driverID, String id);
	//�����˴���Ϣ
	int openDriverMessage(String driverID, String id);
	//ѧԱ��ȡ��Ϣ����
	List<MessageCenterEntity> getStudentMessageCenter(String studentID);
	//ѧԱɾ����Ϣ
	int removeStudentMessage(String studentID, String id);
	//ѧԱ����Ϣ
	int openStudentMessage(String studentID, String id);
	//��ѧԱ������Ϣ
	int sendStudentMessage(MessageCenterEntity entity);
	//�����������Ϣ
	int sendDriverMessage(MessageCenterEntity entity);
}
