package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.PublishOrderEntity;

public interface PublishOrderDaoInterface {
	//���뷢������
	int insertPublishOrder(PublishOrderEntity entity);
	//���ݽ���ID�ʹ��ڵ��������ȡ������������ɾ������ʱ��С������ķ�������
	List<PublishOrderEntity> getPublishOrderByDriverAndDate(int driverID, String date);
	//����ʱ��ͽ���ID��ѯ�Ƿ��Ѿ����ڸ�ʱ��ζ���
	int isExistTimeSlotOrder(int id, int driverID, String beginTime, String endTime);
}
