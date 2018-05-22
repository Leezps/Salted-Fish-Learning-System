package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.PublishOrderEntity;

public interface PublishOrderBusinessInterface {
	//插入发布订单
	int insertPublishOrder(String id, String phone, String password, PublishOrderEntity entity);
	//根据教练ID查询明天开始的所有订单
	List<PublishOrderEntity> getPublishOrderByDriverID(String driverID);
	//学员预定订单
	int studentBookOrder(String id, String phone, String password, PublishOrderEntity entity);
}
