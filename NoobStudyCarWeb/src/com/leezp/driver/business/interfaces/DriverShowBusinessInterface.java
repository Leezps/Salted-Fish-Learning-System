package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.DriverEntity;

public interface DriverShowBusinessInterface {
	//ͨ����ַ��ѯ������ͬ��ص�Ľ���
	List<DriverEntity> getDriversByPlace(String place);
}
