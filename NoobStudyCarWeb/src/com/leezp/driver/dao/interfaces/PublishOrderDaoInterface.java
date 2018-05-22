package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.PublishOrderEntity;

public interface PublishOrderDaoInterface {
	//插入发布订单
	int insertPublishOrder(PublishOrderEntity entity);
	//根据教练ID和大于等于明天获取发布订单，并删除结束时间小于明天的发布订单
	List<PublishOrderEntity> getPublishOrderByDriverAndDate(int driverID, String date);
	//根据时间和教练ID查询是否已经存在该时间段订单
	int isExistTimeSlotOrder(int id, int driverID, String beginTime, String endTime);
}
