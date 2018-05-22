package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.CompleteOrderEntity;

public interface CompleteOrderBusinessInterface {
	List<CompleteOrderEntity> getCompleteOrders(String role, String userID, String phone, String password);
	int removeCompleteOrder(String role, String userID, String phone, String password, String orderID, String remove_message);
	//通过发布订单号获取已完成的订单进行显示
	List<CompleteOrderEntity> getCompleteOrdersByPublishOrderId(String publishOrderId);
}
