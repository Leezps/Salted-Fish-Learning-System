package com.leezp.driver.dao.interfaces;

import java.util.List;
import java.util.Map;

import com.leezp.driver.entity.DriverEntity;

public interface DriverDaoInterface {
	//ͨ���ֻ��Ż�ȡ������Ϣ
	DriverEntity findDriverByPhone(String phone);
	//ͨ������id��ȡ�����ĵ�λ�۸��Լ�ѧ���ص�
	Map<String, Object> findUnitPriceAndPlaceById(int id);
	//ע������ֻ����Լ�����
	int insertBaseDriver(String phone, String password);
	//�ϴ����޸Ľ�����Ϣ
	int uploadOrUpdateDriverInformation(DriverEntity entity);
	//�޸�����
	int updateDriverPassword(String phone, String password);
	//���ݳ��а��������½���ʽ��ȡ������Ϣ����ԤԼ
	List<DriverEntity> findDriversByPlaceAndEvaluate(String place);
	//���ݽ���id��ȡ������ͷ���ַ���������ֻ���
	Map<String, String> findHeadAndNamePhoneById(int id);
}
