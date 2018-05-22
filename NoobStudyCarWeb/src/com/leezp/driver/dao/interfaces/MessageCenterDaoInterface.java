package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.MessageCenterEntity;

public interface MessageCenterDaoInterface {
	//教练获取消息中心
	List<MessageCenterEntity> getDriverMessageCenter(String driverID);
	//教练删除消息
	int removeDriverMessage(String driverID, String id);
	//教练端打开消息
	int openDriverMessage(String driverID, String id);
	//学员获取消息中心
	List<MessageCenterEntity> getStudentMessageCenter(String studentID);
	//学员删除消息
	int removeStudentMessage(String studentID, String id);
	//学员打开消息
	int openStudentMessage(String studentID, String id);
	//向学员发送消息
	int sendStudentMessage(MessageCenterEntity entity);
	//向教练发送消息
	int sendDriverMessage(MessageCenterEntity entity);
}
