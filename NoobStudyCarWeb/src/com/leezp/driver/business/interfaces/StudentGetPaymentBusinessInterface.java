package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.GetPaymentEntity;

public interface StudentGetPaymentBusinessInterface {
	//插入支付方式
	int insertPaymentWay(String id, String phone, String password, GetPaymentEntity entity);
	//获取支付方式
	List<GetPaymentEntity> getPaymentWay(String id, String phone, String password);
	//移除支付方式
	int removePaymentWay(String id, String phone, String password, String payment_id);
}
