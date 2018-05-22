package com.leezp.driver.business.interfaces;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leezp.driver.entity.DriverEntity;

public interface DriverRegisterBusinessInterface {
	boolean isExist(String phone);
	int register(String phone, String password);
	SendSmsResponse sendMessageVerification(String phone, String verification) throws ClientException;
	QuerySendDetailsResponse getSendMessageDetail(String phone, String bizId) throws ClientException;
	int changeDriverPassword(String phone, String password);
	DriverEntity getDriverInformation(String phone);
}
