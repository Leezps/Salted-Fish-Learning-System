package com.leezp.driver.business;

import java.util.List;

import com.leezp.driver.business.interfaces.MessageCenterBusinessInterface;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.dao.MessageCenterDao;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.entity.MessageCenterEntity;
import com.leezp.driver.entity.StudentEntity;

public class MessageCenterBusiness implements MessageCenterBusinessInterface{
	StudentDao student = new StudentDao();
	DriverDao driver = new DriverDao();
	MessageCenterDao messageCenter = new MessageCenterDao();

	@Override
	public List<MessageCenterEntity> getMessageCenter(String role, String id, String phone, String password) {
		if(role.equals("0")) {
			if(isExistStudent(id, phone, password)) {
				return messageCenter.getStudentMessageCenter(id);
			}
		} else if(role.equals("1")) {
			if(isExistDriver(id, phone, password)) {
				return messageCenter.getDriverMessageCenter(id);
			}
		}
		return null;
	}

	@Override
	public int removeMessage(String role, String id, String phone, String password, String message_id) {
		if (role.equals("0")) {
			if(isExistStudent(id, phone, password)) {
				return messageCenter.removeStudentMessage(id, message_id);
			}
		} else if(role.equals("1")) {
			if(isExistDriver(id, phone, password)) {
				return messageCenter.removeDriverMessage(id, message_id);
			}
		}
		return -1;
	}
	
	@Override
	public int openMessage(String role, String id, String phone, String password, String message_id) {
		if (role.equals("0")) {
			if (isExistStudent(id, phone, password)) {
				return messageCenter.openStudentMessage(id, message_id);
			}
		} else if (role.equals("1")) {
			if (isExistDriver(id, phone, password)) {
				return messageCenter.openDriverMessage(id, message_id);
			}
		}
		return -1;
	}

	private boolean isExistStudent(String id, String phone, String password) {
		StudentEntity entity = student.findStudentByPhone(phone);
		if(entity == null || !String.valueOf(entity.getId()).equals(id) || !entity.getPassword().equals(password)) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isExistDriver(String id, String phone, String password) {
		DriverEntity entity = driver.findDriverByPhone(phone);
		if(entity == null || !String.valueOf(entity.getId()).equals(id) || !entity.getPassword().equals(password)) {
			return false;
		} else {
			return true;
		}
	}

}
