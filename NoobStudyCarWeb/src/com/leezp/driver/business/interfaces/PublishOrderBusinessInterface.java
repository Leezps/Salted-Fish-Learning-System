package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.PublishOrderEntity;

public interface PublishOrderBusinessInterface {
	//���뷢������
	int insertPublishOrder(String id, String phone, String password, PublishOrderEntity entity);
	//���ݽ���ID��ѯ���쿪ʼ�����ж���
	List<PublishOrderEntity> getPublishOrderByDriverID(String driverID);
	//ѧԱԤ������
	int studentBookOrder(String id, String phone, String password, PublishOrderEntity entity);
}
