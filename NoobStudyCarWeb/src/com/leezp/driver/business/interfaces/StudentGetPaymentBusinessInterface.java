package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.GetPaymentEntity;

public interface StudentGetPaymentBusinessInterface {
	//����֧����ʽ
	int insertPaymentWay(String id, String phone, String password, GetPaymentEntity entity);
	//��ȡ֧����ʽ
	List<GetPaymentEntity> getPaymentWay(String id, String phone, String password);
	//�Ƴ�֧����ʽ
	int removePaymentWay(String id, String phone, String password, String payment_id);
}
