package com.leezp.driver.business.interfaces;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leezp.driver.entity.StudentEntity;

public interface StudentRegisterBusinessInterface {
	/**
	 * �ֻ����Ƿ����
	 * @param phone
	 * 				�ֻ���
	 * @return
	 * 			true:����;false:������
	 */
	public boolean isExist(String phone);
	/**
	 * ע��ѧԱ
	 * 
	 * @param phone
	 * 				�ֻ���
	 * @param password
	 * 				����
	 * @return
	 */
	public int register(String phone, String password);
	/**
	 * ������֤��
	 */
	public SendSmsResponse sendMessageVerification(String phone, String verification) throws ClientException;
	/**
	 * ��ȡ������֤��
	 */
	public QuerySendDetailsResponse getSendMessageDetail(String phone, String bizId) throws ClientException;
	/**
	 * �޸�����
	 * 
	 * @param phone
	 * 				�ֻ���
	 * @param password
	 * 				����
	 * @return
	 */
	public int changeStudentPassword(String phone, String password);
	/**
	 * ��ȡ�����û���Ϣ
	 * 
	 * @param phone
	 * 				�ֻ���
	 * @return
	 */
	public StudentEntity getStudentInformation(String phone);
}
