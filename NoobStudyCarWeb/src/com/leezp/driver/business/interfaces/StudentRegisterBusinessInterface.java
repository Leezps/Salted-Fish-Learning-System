package com.leezp.driver.business.interfaces;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leezp.driver.entity.StudentEntity;

public interface StudentRegisterBusinessInterface {
	/**
	 * 手机号是否存在
	 * @param phone
	 * 				手机号
	 * @return
	 * 			true:存在;false:不存在
	 */
	public boolean isExist(String phone);
	/**
	 * 注册学员
	 * 
	 * @param phone
	 * 				手机号
	 * @param password
	 * 				密码
	 * @return
	 */
	public int register(String phone, String password);
	/**
	 * 发送验证码
	 */
	public SendSmsResponse sendMessageVerification(String phone, String verification) throws ClientException;
	/**
	 * 获取短信验证码
	 */
	public QuerySendDetailsResponse getSendMessageDetail(String phone, String bizId) throws ClientException;
	/**
	 * 修改密码
	 * 
	 * @param phone
	 * 				手机号
	 * @param password
	 * 				密码
	 * @return
	 */
	public int changeStudentPassword(String phone, String password);
	/**
	 * 获取整个用户信息
	 * 
	 * @param phone
	 * 				手机号
	 * @return
	 */
	public StudentEntity getStudentInformation(String phone);
}
