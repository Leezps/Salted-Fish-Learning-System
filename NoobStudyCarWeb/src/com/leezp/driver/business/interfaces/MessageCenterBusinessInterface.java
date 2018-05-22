package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.MessageCenterEntity;

public interface MessageCenterBusinessInterface {
	List<MessageCenterEntity> getMessageCenter(String role, String id, String phone, String password);
	int removeMessage(String role, String id, String phone, String password, String message_id);
	int openMessage(String role, String id, String phone, String password, String message_id);
}
