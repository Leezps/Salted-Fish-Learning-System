package com.leezp.driver.dao.interfaces;

import java.util.List;
import java.util.Map;

import com.leezp.driver.entity.CompleteOrderEntity;

public interface CompleteOrderDaoInterface {
	//��ѯ������ɵĶ���
	List<CompleteOrderEntity> getStudentCompleteOrders(String studentID, String date);
	List<CompleteOrderEntity> getDriverCompleteOrders(String driverID, String date);
	
	//ɾ����ɵĶ���
	int removeStudentCompleteOrder(String id, String studentID);
	int removeDriverCompleteOrder(String id, String driverID);
	
	//��ѯ����״̬
	Map<String, Integer> selectCompleteOrder(boolean student, String id, String userID);
	//������ɶ���
	int insertCompleteOrder(CompleteOrderEntity entity);
	//���ݷ���������id�Լ���ʼ������ʱ���ѯ�Ƿ���ڳ�ͻ�Ķ�����
	boolean notExistsClashCompleteOrder(int publishOrderId, String beginTime, String endTime);
	//���ݷ���������id��ȡ������ɶ���
	List<CompleteOrderEntity> getCompleteOrdersByPublishOrder(int publishOrderId);
	//ͨ����ɶ���id��ѯѧԱid�����id
	Map<String, String> getStudentAndDriverById(int id);
}
