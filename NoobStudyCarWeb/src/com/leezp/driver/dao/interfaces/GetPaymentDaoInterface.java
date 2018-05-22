package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.GetPaymentEntity;

public interface GetPaymentDaoInterface {
	//����֧����ʽ
	List<GetPaymentEntity> getPaymentWay(int studentID);
	int getPaymentNum(int studentID);
	
	//ɾ��֧����ʽ
	int removePaymentWay(int id, int studentID);
	//����֧����ʽ
	int insertPaymentWay(GetPaymentEntity entity);
}
