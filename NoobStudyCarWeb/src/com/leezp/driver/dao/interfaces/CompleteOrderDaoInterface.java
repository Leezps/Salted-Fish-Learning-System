package com.leezp.driver.dao.interfaces;

import java.util.List;
import java.util.Map;

import com.leezp.driver.entity.CompleteOrderEntity;

public interface CompleteOrderDaoInterface {
	//查询所有完成的订单
	List<CompleteOrderEntity> getStudentCompleteOrders(String studentID, String date);
	List<CompleteOrderEntity> getDriverCompleteOrders(String driverID, String date);
	
	//删除完成的订单
	int removeStudentCompleteOrder(String id, String studentID);
	int removeDriverCompleteOrder(String id, String driverID);
	
	//查询订单状态
	Map<String, Integer> selectCompleteOrder(boolean student, String id, String userID);
	//插入完成订单
	int insertCompleteOrder(CompleteOrderEntity entity);
	//根据发布订单的id以及开始、结束时间查询是否存在冲突的订单号
	boolean notExistsClashCompleteOrder(int publishOrderId, String beginTime, String endTime);
	//根据发布订单的id获取所有完成订单
	List<CompleteOrderEntity> getCompleteOrdersByPublishOrder(int publishOrderId);
	//通过完成订单id查询学员id与教练id
	Map<String, String> getStudentAndDriverById(int id);
}
