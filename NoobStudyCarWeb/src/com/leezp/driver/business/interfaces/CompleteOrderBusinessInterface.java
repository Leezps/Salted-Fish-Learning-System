package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.CompleteOrderEntity;

public interface CompleteOrderBusinessInterface {
	List<CompleteOrderEntity> getCompleteOrders(String role, String userID, String phone, String password);
	int removeCompleteOrder(String role, String userID, String phone, String password, String orderID, String remove_message);
	//ͨ�����������Ż�ȡ����ɵĶ���������ʾ
	List<CompleteOrderEntity> getCompleteOrdersByPublishOrderId(String publishOrderId);
}
