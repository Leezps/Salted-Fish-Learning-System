package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.GetPaymentEntity;

public interface GetPaymentDaoInterface {
	//查找支付方式
	List<GetPaymentEntity> getPaymentWay(int studentID);
	int getPaymentNum(int studentID);
	
	//删除支付方式
	int removePaymentWay(int id, int studentID);
	//插入支付方式
	int insertPaymentWay(GetPaymentEntity entity);
}
